package com.hansarangdelivery.global.dto;

import com.hansarangdelivery.ai.dto.AiResponseDto;
import com.hansarangdelivery.category.dto.CategoryResponseDto;
import com.hansarangdelivery.location.dto.LocationResponseDto;
import com.hansarangdelivery.menu.dto.MenuItemResponseDto;
import com.hansarangdelivery.order.dto.OrderResponseDto;
import com.hansarangdelivery.restaurant.dto.RestaurantResponseDto;
import com.hansarangdelivery.review.dto.ReviewResponseDto;
import com.hansarangdelivery.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "API 응답 결과를 담는 DTO")
public class ResultResponseDto<D> {

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int statusCode;

    @Schema(description = "응답 데이터",
        anyOf = {
            AiResponseDto.class,
            CategoryResponseDto.class,
            LocationResponseDto.class,
            MenuItemResponseDto.class,
            OrderResponseDto.class,
            ReviewResponseDto.class,
            UserResponseDto.class,
            RestaurantResponseDto.class,
            PageResponseDto.class
    })
    private D data;

    public ResultResponseDto(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
        this.data = null;
    }
}
