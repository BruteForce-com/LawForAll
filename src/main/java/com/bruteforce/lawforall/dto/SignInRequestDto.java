package com.bruteforce.lawforall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignInRequestDto {

    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase letter, one special character, and no whitespace"
    )
    private String password;

    // Getters and Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Constructors
    public SignInRequestDto() {
    }

    public SignInRequestDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Helper method to check if the input is an email
    public boolean isEmail() {
        return usernameOrEmail != null && usernameOrEmail.contains("@");
    }

    // Builder pattern
    public static class SignInRequestBuilder {
        private String usernameOrEmail;
        private String password;

        public SignInRequestBuilder usernameOrEmail(String usernameOrEmail) {
            this.usernameOrEmail = usernameOrEmail;
            return this;
        }

        public SignInRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SignInRequestDto build() {
            return new SignInRequestDto(usernameOrEmail, password);
        }
    }
}