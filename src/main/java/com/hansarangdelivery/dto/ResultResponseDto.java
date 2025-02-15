package com.hansarangdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    }
}
