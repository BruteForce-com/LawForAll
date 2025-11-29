package com.bruteforce.lawforall.service;

import com.bruteforce.lawforall.Utils.DtoConverter;
import com.bruteforce.lawforall.config.AIConfig.PromptTemplateConfig;
import com.bruteforce.lawforall.dto.ChatRequestDto;
import com.bruteforce.lawforall.dto.ChatResponseDto;
import com.bruteforce.lawforall.exception.NoChatSessionFoundException;
import com.bruteforce.lawforall.model.ChatMessage;
import com.bruteforce.lawforall.model.Role;
import com.bruteforce.lawforall.model.User;
import com.bruteforce.lawforall.repo.ChatRepository;
import com.bruteforce.lawforall.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AiService {

    private final Logger log = LoggerFactory.getLogger(AiService.class);

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final PromptTemplate promptTemplateConfig;
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    public AiService(UserRepository userRepository, ChatRepository chatRepository,
                     PromptTemplate promptTemplateConfig, GoogleGenAiChatModel chatModel, ChatMemory chatMemory) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.promptTemplateConfig = promptTemplateConfig;
        this.chatClient = ChatClient.create(chatModel);
        this.chatMemory = chatMemory;
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false)
    public ChatResponseDto askAI(ChatRequestDto requestDto, UUID userId) {
        log.info("Chat request received by user with : {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or Chat session not belongs to User with id: " + userId));

        log.info("user with id: {} found and allowed to use AIService", userId);


        // Check that Session Already created or not
       Boolean isChatMessagesExists = chatRepository.existsByConversationIdAndUserId(requestDto.getConversationId(),userId);

        // conversationId for creating a new session if not exist. Otherwise, use existing conversationId
        UUID conversationId;
        if(!isChatMessagesExists) {
            log.info("Chat session not found for user with id: {} and conversation id: {}", requestDto.getUserId(), requestDto.getConversationId());
            log.info("Creating new Chat session for user with id: {} and conversation id: {}", requestDto.getUserId(), requestDto.getConversationId());
            conversationId = UUID.randomUUID();
        } else {
            log.info("Chat session already exist for user with id: {} and conversation id: {}", requestDto.getUserId(), requestDto.getConversationId());
            conversationId = requestDto.getConversationId();
        }

        log.info("Chat session created or updated for user with id: {} and conversation id: {}", requestDto.getUserId(), conversationId);
        // Save ChatMessage sent by a user in the database
        ChatMessage chatMessage = ChatMessage.builder()
                .conversationId(conversationId)
                .userId(userId)
                .role(user.getRole())
                .message(requestDto.getMessage())
                .build();
        ChatMessage savedChatMessage = chatRepository.save(chatMessage);
        log.info("Saved ChatMessage in database for user with id: {} and conversation id: {} requested for query: {} ", requestDto.getUserId(), conversationId, savedChatMessage.getMessage());



        // From this the AI RESPONSE will be generated to answer the User Question
        log.info("Generating LLM Prompt response for user with id: {} and conversation id: {}", requestDto.getUserId(), conversationId);

        Map<String, Object> variables = new HashMap<>();

        variables.put("fullname", user.getFullName());
        variables.put("conversationId", conversationId);
        variables.put("role", user.getRole());
        variables.put("message", requestDto.getMessage());
        String overAllPrompt = promptTemplateConfig.create(variables).getContents();
        log.info("Prompt sent to llm with user query: {}", overAllPrompt);

        // Generate the llm response for the requestDto.getMessage()
        String llmResponse = chatClient.prompt(overAllPrompt)
                .advisors(PromptChatMemoryAdvisor.builder(chatMemory).conversationId(conversationId.toString() + userId).build())
                .call().content();

        // Create and save the new chat message in the database
        ChatMessage chat = ChatMessage.builder()
                .conversationId(conversationId)
                .userId(userId)
                .role(Role.LLM)
                .message(llmResponse)
                .build();
        ChatMessage savedChat = chatRepository.save(chat);
        log.info("Saved LLM response in database for user with id: {} and conversation id: {} requested for query: {} ", userId, conversationId, savedChat.getMessage());
        // Return the response by converting ChatMessage to ChatResponseDto
        return DtoConverter.convertChatMessageToChatResponseDto(savedChat);

    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<ChatResponseDto> getAllChatsOfSession(UUID conversationId, UUID userId) {
        List<ChatMessage> chatMessages =  chatRepository.findAllByConversationIdAndUserIdOrderByUpdatedAtAsc(conversationId, userId);
        return chatMessages.stream().map(DtoConverter::convertChatMessageToChatResponseDto).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false)
    public String deleteChatSession(UUID conversationID,UUID userId){

        log.info("Deleting chat session for conversation ID: {}", conversationID);

        if(chatRepository.deleteAllByConversationIdAndUserId(conversationID, userId) > 0){
            log.info("Chat session deleted successfully for conversation ID: {}", conversationID);
            return "Chat session deleted successfully "+ conversationID;
        }

        log.info("No chat session found for conversation ID: {}", conversationID);

        throw new NoChatSessionFoundException("No chat session found for user" + userId + "with conversationID " + conversationID);

    }

}