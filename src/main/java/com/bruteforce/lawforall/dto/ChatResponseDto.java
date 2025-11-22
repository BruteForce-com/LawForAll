package com.bruteforce.lawforall.dto;

import com.bruteforce.lawforall.model.Role;

import java.util.UUID;

public class ChatResponseDto {

    private UUID conversationId;
    private String message;
    private String title;
    private Role role;

    public ChatResponseDto() {
    }
    public ChatResponseDto(UUID conversationId, String message, String title, Role role) {
        this.conversationId = conversationId;
        this.message = message;
        this.title = title;
        this.role = role;
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
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public static class ChatResponseDtoBuilder {
        private UUID conversationId;
        private String message;
        private String title;
        private Role role;

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
        public ChatResponseDtoBuilder role(Role role) {
            this.role = role;
            return this;
        }
        public ChatResponseDto build() {
            return new ChatResponseDto(conversationId, message, title, role);
        }
    }
}
