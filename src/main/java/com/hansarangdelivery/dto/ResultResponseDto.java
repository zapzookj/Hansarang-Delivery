package com.hansarangdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultResponseDto {
    private String message;
    private int statusCode;

    public ResultResponseDto(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
}
