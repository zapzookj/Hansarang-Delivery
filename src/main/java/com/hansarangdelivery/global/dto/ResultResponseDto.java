package com.hansarangdelivery.global.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ResultResponseDto<D> {
    private String message;
    private int statusCode;
    private D data;

    public ResultResponseDto(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
        this.data = null;
    }
}
