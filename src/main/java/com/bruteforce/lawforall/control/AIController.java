package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.dto.ChatRequestDto;
import com.bruteforce.lawforall.security.UserPriciple;
import com.bruteforce.lawforall.service.AiService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final Logger logger = LoggerFactory.getLogger(AIController.class);

    private final AiService aiService;

    public AIController(AiService aiService) {
        this.aiService = aiService;
    }


    @PostMapping("/ask")
    public ResponseEntity<?> askAI(@AuthenticationPrincipal UserPriciple user, @Valid @RequestBody ChatRequestDto requestDto) {

        logger.info("{} asked AI: {}", user.getUsername(), requestDto.getMessage());

        try{
            return ResponseEntity.ok(aiService.askAI(requestDto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
