package com.hansarangdelivery.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserUpdateDto {
    private String username;
    private String email;
}
