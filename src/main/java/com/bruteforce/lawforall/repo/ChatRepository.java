package com.bruteforce.lawforall.repo;

import com.bruteforce.lawforall.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByConversationIdAndUserIdOrderByUpdatedAtAsc(UUID conversationId, UUID userId);


}
