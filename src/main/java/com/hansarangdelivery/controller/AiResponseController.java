//package com.hansarangdelivery.controller;
//
//import com.hansarangdelivery.dto.AiRequestDto;
//import com.hansarangdelivery.dto.AiResponseDto;
//import com.hansarangdelivery.dto.ResultResponseDto;
//import com.hansarangdelivery.security.UserDetailsImpl;
//import com.hansarangdelivery.service.AiResponseService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/ai")
//public class AiResponseController {
//
//    private final AiResponseService aiResponseService;
//
//    // AI 응답 생성 API
//    @PostMapping()
//    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
//    public ResultResponseDto<String> createAiResponse(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                      @RequestBody AiRequestDto requestDto) {
//        String response = aiResponseService.createAiResponse(userDetails.getUser().getId(), requestDto);
//        return new ResultResponseDto<>("Ai 응답 생성 성공", 200, response);
//    }
//
//    // AI 응답 단건 조회 API (Permission : 본인이 생성한 응답이거나 권한이 MANAGER 인 경우)
//    @GetMapping("/{aiResponseId}")
//    public ResultResponseDto<AiResponseDto> getAiResponse(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                          @PathVariable("aiResponseId") UUID aiResponseId) {
//        AiResponseDto aiResponseDto = aiResponseService.getAiResponse(userDetails.getUser(), aiResponseId);
//        return new ResultResponseDto<>("조회 성공", 200, aiResponseDto);
//    }
//
//    // 본인이 생성한 AI 응답 전체 조회 API
//    @GetMapping()
//    public ResultResponseDto<Page<AiResponseDto>> searchAiResponses(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                                    @RequestParam("page") int page,
//                                                                    @RequestParam("size") int size,
//                                                                    @RequestParam("isAsc") boolean isAsc) {
//        Page<AiResponseDto> aiResponseDtos = aiResponseService.searchAiResponses(userDetails.getUser(), page-1, size, isAsc);
//        return new ResultResponseDto<>("조회 성공", 200, aiResponseDtos);
//    }
//
//    // AI 응답 삭제 API (Permission : 본인이 생성한 응답이거나 권한이 MANAGER 인 경우)
//    @DeleteMapping("/{aiResponseId}")
//    public ResultResponseDto<Void> deleteAiResponse(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                    @PathVariable("aiResponseId") UUID aiResponseId) {
//        aiResponseService.deleteAiResponse(userDetails.getUser(), aiResponseId);
//        return new ResultResponseDto<>("삭제 성공", 200);
//    }
//}
