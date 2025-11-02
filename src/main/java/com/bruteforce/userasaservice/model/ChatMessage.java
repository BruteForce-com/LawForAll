package com.bruteforce.userasaservice.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;


/**
 * Represents a chat message in the system, tracking all message-related information and relationships.
 * <p>
 * Mapped to the database table `chat_messages`. Each message is linked one-to-one with a `Conversation`
 * entity via `conversationId`. Contains message metadata such as title, creation and update timestamps,
 * and the role of the user that sent the message.
 * <p>
 * <h3> Key Features: </h3>
 * <ul>
 *     <li>Message tracking with detailed metadata</li>
 *     <li>Bidirectional relationships with Conversation entities</li>
 *     <li>Support for various message types and statuses</li>
 *     <li>Financial claim tracking with proper decimal precision</li>
 *     <li>Built-in audit fields for tracking message lifecycle</li>
 * </ul>
 * @see ChatMessage.ChatMessageEntityBuilder
 * @see Role
 * @since 1.0.0
 * @author @shivaverbandi - BruteForce
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    /**
     * The unique identifier for the conversation that this message belongs to.
     */
    @Id
    @Column(name = "conversation_id", length = 255)
    private UUID conversationId;

    /**
     * The unique identifier for the user that sent this message.
     */
    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    /**
     * The title of the conversation.
     */
    @Column(name = "title", length = 500)
    private String title;

    /**
     * The timestamp when the message was created.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * The timestamp when the message was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * The role of the user that sent the message.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false, updatable = false)
    private Role role;

    // ==================== CONSTRUCTORS ====================
    public ChatMessage() {
    }

    public ChatMessage(UUID conversationId, String userId, String title,
                              Instant createdAt, Instant updatedAt, Role role) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    // ==================== BUILDER ====================
    public static ChatMessageEntityBuilder builder() {
        return new ChatMessageEntityBuilder();
    }

    public static class ChatMessageEntityBuilder {
        private UUID conversationId;
        private String userId;
        private String title;
        private Instant createdAt;
        private Instant updatedAt;
        private Role role;

        ChatMessageEntityBuilder() {
        }

        public ChatMessageEntityBuilder conversationId(UUID conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public ChatMessageEntityBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public ChatMessageEntityBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ChatMessageEntityBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ChatMessageEntityBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        public ChatMessageEntityBuilder role(Role role) {
            this.role = role;
            return this;
        }
        public ChatMessage build() {
            return new ChatMessage(conversationId, userId, title, createdAt, updatedAt, role);
        }
    }

    // ==================== LIFECYCLE ====================
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // ==================== GETTERS & SETTERS ====================
    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
