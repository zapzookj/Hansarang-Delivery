package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.AiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiResponseDto {
    private String request;
    private String response;

    public AiResponseDto(AiResponse aiResponse) {
        this.request = aiResponse.getRequest();
        this.response = aiResponse.getResponse();
    }
}
