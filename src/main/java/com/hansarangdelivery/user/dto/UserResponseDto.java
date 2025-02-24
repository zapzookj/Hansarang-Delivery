package com.hansarangdelivery.user.dto;

import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    @Schema(description = "사용자 ID", example = "1")
    private Long id;
    @Schema(description = "사용자 이름", example = "user123")
    private String username;

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 역할", example = "CUSTOMER", allowableValues = {"CUSTOMER", "OWNER", "MANAGER"})
    private UserRole role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

}
