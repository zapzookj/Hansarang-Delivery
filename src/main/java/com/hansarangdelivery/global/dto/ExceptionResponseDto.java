package com.hansarangdelivery.global.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDto {
    private String message;
    private int statusCode;
}
