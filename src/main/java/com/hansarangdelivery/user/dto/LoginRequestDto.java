package com.hansarangdelivery.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    @Schema(description = "사용자 이름", example = "user123", required = true)
    private String username;

    @Schema(description = "비밀번호", example = "password123", required = true, format = "password")
    private String password;
}
