package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.AiRequestDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.AiResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiResponseController {

    private final AiResponseService aiResponseService;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<String> createAiResponse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestBody AiRequestDto requestDto) {
        String response = aiResponseService.createAiResponse(userDetails.getUser().getId(), requestDto);
        return new ResultResponseDto<>("Ai 응답 생성 성공", 200, response);
    }
}
