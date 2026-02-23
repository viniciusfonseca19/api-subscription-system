package com.subscription.system.dto.response;

public class AuthResponse {

    private Long userId;
    private String name;
    private String email;

    public AuthResponse(Long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}