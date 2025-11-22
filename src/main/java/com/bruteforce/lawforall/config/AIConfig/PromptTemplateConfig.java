package com.bruteforce.lawforall.config.AIConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class PromptTemplateConfig {


    private static final Logger log = LoggerFactory.getLogger(PromptTemplateConfig.class);

    @Value("classpath:prompts/AiAssistance.st")
    private Resource promptTemplate;

    @Bean
    public PromptTemplate aiAssistPromptTemplate() throws IOException{

        String templateContent = StreamUtils.copyToString(promptTemplate.getInputStream(), StandardCharsets.UTF_8);
        log.info("Loaded AI Assistant prompt template with size: {} characters", templateContent.length());
        return new PromptTemplate(templateContent);



    }
}
