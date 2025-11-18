package com.bruteforce.lawforall.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ChatRequestDto {


    private UUID conversationId;
    // user query
    @NotNull
    private String message;
    private UUID userId;

    public ChatRequestDto() {

    }

    public ChatRequestDto(UUID conversationId, String message, UUID userId) {
        this.conversationId = conversationId;
        this.message = message;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public static ChatRequestDto.ChatRequestBuilder builder() {
        return new ChatRequestDto.ChatRequestBuilder();
    }

    public static class ChatRequestBuilder {
        private UUID conversationId;
        private String message;
        private UUID userId;

        public ChatRequestDto.ChatRequestBuilder conversationId(UUID conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public ChatRequestDto.ChatRequestBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ChatRequestDto.ChatRequestBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public ChatRequestDto build() {
            return new ChatRequestDto(conversationId, message, userId);
        }
    }
}
