package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.AiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AiResponseDto {
    private UUID id;
    private String request;
    private String response;

    public AiResponseDto(AiResponse aiResponse) {
        this.id = aiResponse.getId();
        this.request = aiResponse.getRequest();
        this.response = aiResponse.getResponse();
    }
}
