package com.example.basicauth.dto;

import lombok.*;

@Getter
@Setter
public class LoginRequestDto {
    private String username;
    private String password;
}
