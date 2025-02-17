package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.AiRequestDto;
import com.hansarangdelivery.dto.AiApiResponseDto;
import com.hansarangdelivery.entity.AiResponse;
import com.hansarangdelivery.repository.AiResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiResponseService {

    private final AiResponseRepository aiResponseRepository;
    private final RestTemplate restTemplate;

    @Value("${google.ai.api.key}")
    private String apiKey;

    @Value("${google.ai.api.url}")
    private String apiUrl;


    public String createAiResponse(Long userId, AiRequestDto requestDto) {

        // Google Ai Studio의 리퀘스트 형식에 맞게 데이터 변환
        Map<String, Object> requestBody = setRequest(requestDto.getRequest());

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request 객체 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AiApiResponseDto> responseEntity = restTemplate.exchange(
            apiUrl + apiKey,
            HttpMethod.POST,
            requestEntity,
            AiApiResponseDto.class
        );

        AiApiResponseDto responseDto = responseEntity.getBody();

        // Google Ai Studio의 응답에서 필요한 데이터(Ai 답변)만 추출
        String response = extractResponseText(responseDto);

        AiResponse airesponse = new AiResponse(userId, requestDto.getRequest(), response);
        aiResponseRepository.save(airesponse);

        return response;
    }

    private Map<String, Object> setRequest(String requestText) {
        requestText += " 답변을 최대한 간결하게 50자 이하로";
        return Map.of(
            "contents", Collections.singletonList(
                Map.of("parts", Collections.singletonList(
                    Map.of("text", requestText)
                ))
            )
        );
    }

    private String extractResponseText(AiApiResponseDto responseDto) {
        return responseDto.getCandidates()
            .get(0)
            .getContent()
            .getParts()
            .get(0)
            .getText();
    }
}
