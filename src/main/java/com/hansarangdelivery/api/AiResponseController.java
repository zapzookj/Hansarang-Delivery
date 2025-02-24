package com.hansarangdelivery.api;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.ai.dto.AiRequestDto;
import com.hansarangdelivery.ai.dto.AiResponseDto;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.ai.service.AiResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiResponseController {
    private final AiResponseService aiResponseService;

    @Operation(summary = "AI 응답 생성", description = "새로운 AI 응답을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "AI 응답 생성 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER')")
    public ResultResponseDto<String> createAiResponse(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = AiRequestDto.class),
                examples = {
                    @ExampleObject(
                        name = "AI 응답 요청 예시",
                        value = """
                    {
                      "prompt": "고객 리뷰에 대한 응답을 작성해주세요."
                    }
                    """
                    )
                }
            )
        )
        @RequestBody AiRequestDto requestDto) {
        String response = aiResponseService.createAiResponse(userDetails.getUser().getId(), requestDto);
        return new ResultResponseDto<>("AI 응답 생성 성공", 200, response);
    }

    @Operation(summary = "AI 응답 조회", description = "특정 AI 응답을 조회합니다. (본인 생성 또는 MANAGER 권한 필요)")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "응답을 찾을 수 없음", content = @Content)
    @GetMapping("/{aiResponseId}")
    public ResultResponseDto<AiResponseDto> readAiResponse(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "AI 응답 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("aiResponseId") UUID aiResponseId) {
        AiResponseDto aiResponseDto = aiResponseService.readAiResponse(userDetails.getUser(), aiResponseId);
        return new ResultResponseDto<>("조회 성공", 200, aiResponseDto);
    }

    @Operation(summary = "AI 응답 목록 조회", description = "사용자가 생성한 AI 응답 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", example = "createdAt,desc")
    })
    @GetMapping()
    public ResultResponseDto<PageResponseDto<AiResponseDto>> searchAiResponses(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(hidden = true) Pageable pageable) {
        PageableConfig.validatePageSize(pageable);
        PageResponseDto<AiResponseDto> aiResponseDtos = aiResponseService.searchAiResponses(userDetails.getUser(), pageable);
        return new ResultResponseDto<>("조회 성공", 200, aiResponseDtos);
    }

    @Operation(summary = "AI 응답 삭제", description = "특정 AI 응답을 삭제합니다. (본인 생성 또는 MANAGER 권한 필요)")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "응답을 찾을 수 없음", content = @Content)
    @DeleteMapping("/{aiResponseId}")
    public ResultResponseDto<Void> deleteAiResponse(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "AI 응답 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("aiResponseId") UUID aiResponseId) {
        aiResponseService.deleteAiResponse(userDetails.getUser(), aiResponseId);
        return new ResultResponseDto<>("삭제 성공", 200);
    }

}
