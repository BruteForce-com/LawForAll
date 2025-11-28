package com.bruteforce.lawforall.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.PostgresChatMemoryRepositoryDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;


@Configuration
public class ChatMemoryConfig {

    private final Logger log = LoggerFactory.getLogger(ChatMemoryConfig.class);
    private final JdbcTemplate jdbcTemplate;

    public ChatMemoryConfig(JdbcTemplate jdbcTemplate) {
        log.info("ChatMemoryConfig object created at {}", LocalDateTime.now());
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public ChatMemory getChatMemory(){

        log.info("ChatMemory object created at {}", LocalDateTime.now());

        ChatMemoryRepository chatMemoryRepository = getChatMemoryRepository();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
        return chatMemory;
    }

    @Bean
    public ChatMemoryRepository getChatMemoryRepository(){

        log.info("ChatMemoryRepository object created at {}", LocalDateTime.now());
        ChatMemoryRepository chatMemoryRepository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(new PostgresChatMemoryRepositoryDialect())
                .build();

        return chatMemoryRepository;
    }

}
