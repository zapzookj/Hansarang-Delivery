package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.AiRequestDto;
import com.hansarangdelivery.dto.AiApiResponseDto;
import com.hansarangdelivery.dto.AiResponseDto;
import com.hansarangdelivery.entity.AiResponse;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.exception.ForbiddenActionException;
import com.hansarangdelivery.exception.ResourceNotFoundException;
import com.hansarangdelivery.repository.AiResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiResponseService {

    private final AiResponseRepository aiResponseRepository;
    private final RestTemplate restTemplate;

    @Value("${google.ai.api.key}")
    private String apiKey;

    @Value("${google.ai.api.url}")
    private String apiUrl;


    @Transactional
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

    @Transactional(readOnly = true)
    public AiResponseDto readAiResponse(User user, UUID aiResponseId) {
        AiResponse aiResponse = findAiResponse(aiResponseId);

        checkPermissions(aiResponse, user);

        return new AiResponseDto(aiResponse);
    }

    @Transactional(readOnly = true)
    public Page<AiResponseDto> searchAiResponses(User user, Pageable pageable) {

        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return aiResponseRepository.findByUserId(user.getId(), sortedPageable).map(AiResponseDto::new);
    }

    @Transactional
    public void deleteAiResponse(User user, UUID aiResponseId) {
        AiResponse aiResponse = findAiResponse(aiResponseId);

        checkPermissions(aiResponse, user);

        aiResponseRepository.deleteById(aiResponseId);
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

    private AiResponse findAiResponse(UUID aiResponseId) {
        return aiResponseRepository.findById(aiResponseId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 Id를 가진 Ai 응답을 찾을 수 없습니다."));
    }

    private void checkPermissions(AiResponse aiResponse, User user) {
        if (!aiResponse.getUserId().equals(user.getId()) || !user.getRole().equals(UserRole.MANAGER)) {
            throw new ForbiddenActionException("권한이 없습니다.");
        }
    }
}
