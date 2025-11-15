package com.bruteforce.lawforall.dto;

import com.bruteforce.lawforall.model.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class SignUpResponseDto {

    private UUID userId;
    private String userName;
    private String email;
    private String token;
    private LocalDateTime createdAt;
    private Role role;

    public SignUpResponseDto() {

    }


    public SignUpResponseDto(UUID userId, String userName, String email, String token, LocalDateTime createdAt) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.token = token;
        this.createdAt = createdAt;

    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
