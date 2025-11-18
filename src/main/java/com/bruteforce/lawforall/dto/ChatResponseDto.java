package com.bruteforce.lawforall.dto;

import java.util.UUID;

public class ChatResponseDto {

    private UUID conversationId;
    private String message;
    private String title;
    private String role;
    private String userId;

    public ChatResponseDto() {
    }
    public ChatResponseDto(UUID conversationId, String message, String title, String role, String userId) {
        this.conversationId = conversationId;
        this.message = message;
        this.title = title;
        this.role = role;
        this.userId = userId;
    }
    public UUID getConversationId() {
        return conversationId;
    }
    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class ChatResponseDtoBuilder {
        private UUID conversationId;
        private String message;
        private String title;
        private String role;
        private String userId;

        public ChatResponseDtoBuilder conversationId(UUID conversationId) {
            this.conversationId = conversationId;
            return this;
        }
        public ChatResponseDtoBuilder message(String message) {
            this.message = message;
            return this;
        }
        public ChatResponseDtoBuilder title(String title) {
            this.title = title;
            return this;
        }
        public ChatResponseDtoBuilder role(String role) {
            this.role = role;
            return this;
        }
        public ChatResponseDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }
        public ChatResponseDto build() {
            return new ChatResponseDto(conversationId, message, title, role, userId);
        }
    }
}
