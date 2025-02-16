package com.example.gotrip.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String passport;
}
