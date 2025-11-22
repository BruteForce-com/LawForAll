package com.bruteforce.lawforall.service;

import com.bruteforce.lawforall.Utils.DtoConverter;
import com.bruteforce.lawforall.config.AIConfig.PromptTemplateConfig;
import com.bruteforce.lawforall.dto.ChatRequestDto;
import com.bruteforce.lawforall.dto.ChatResponseDto;
import com.bruteforce.lawforall.model.ChatMessage;
import com.bruteforce.lawforall.model.Role;
import com.bruteforce.lawforall.model.User;
import com.bruteforce.lawforall.repo.ChatRepository;
import com.bruteforce.lawforall.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
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
    private final PromptTemplateConfig promptTemplateConfig;
    private final ChatClient chatClient;
    public AiService(UserRepository userRepository, ChatRepository chatRepository,
                     PromptTemplateConfig promptTemplateConfig, GoogleGenAiChatModel chatModel) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.promptTemplateConfig = promptTemplateConfig;
        this.chatClient = ChatClient.create(chatModel);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false)
    public ChatResponseDto askAI(ChatRequestDto requestDto, UUID userId) {
        log.info("Chat request received by user with : {}", requestDto.getUserId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found or Chat session not belongs to User with id: " + requestDto.getUserId()));

        log.info("user with id: {} found and allowed to use AIService", requestDto.getUserId());


        // Check that Session Already created or not
        List<ChatMessage> chatMessages = chatRepository.findAllByConversationIdAndUserIdOrderByUpdatedAtAsc(requestDto.getConversationId(),userId);

        // conversationId for creating a new session if not exist. Otherwise, use existing conversationId
        UUID conversationId;
        if(chatMessages == null || chatMessages.isEmpty()) {
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
        String overAllPrompt = "";
        try {
            PromptTemplate promptTemplate = promptTemplateConfig.aiAssistPromptTemplate();
            variables.put("fullname", user.getFullName());
            variables.put("conversationId", conversationId);
            variables.put("role", user.getRole());
            variables.put("message", requestDto.getMessage());
            overAllPrompt = promptTemplate.create(variables).getContents();
            log.info("Prompt sent to llm with user query: {}", overAllPrompt);
        } catch (IOException e) {
            log.error("Error reading prompt template file: {}", e.getMessage());
            throw new RuntimeException("Error reading prompt template file", e);
        }





        // Generate the llm response for the requestDto.getMessage()
        String llmResponse = chatClient.prompt(overAllPrompt)
                .call().content();

        // Create and save the new chat message in the database
        ChatMessage chat = ChatMessage.builder()
                .conversationId(conversationId)
                .userId(userId)
                .role(Role.LLM)
                .message(llmResponse)
                .build();
        ChatMessage savedChat = chatRepository.save(chat);

        // Return the response by converting ChatMessage to ChatResponseDto
        return DtoConverter.convertChatMessageToChatResponseDto(savedChat);

    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<ChatResponseDto> getAllChatsOfSession(UUID conversationId, UUID userId) {
        List<ChatMessage> chatMessages =  chatRepository.findAllByConversationIdAndUserIdOrderByUpdatedAtAsc(conversationId, userId);
        return chatMessages.stream().map(DtoConverter::convertChatMessageToChatResponseDto).toList();
    }

}
