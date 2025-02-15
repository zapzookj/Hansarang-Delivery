package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String email;
    private UserRole role;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
