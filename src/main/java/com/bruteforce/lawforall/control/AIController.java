package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.model.User;
import com.bruteforce.lawforall.repo.UserRepository;
import com.bruteforce.lawforall.security.UserPriciple;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ai")
public class AIController {


    private final GoogleGenAiChatModel googleGenAiChatModel;


    public AIController(GoogleGenAiChatModel googleGenAiChatModel) {
        this.googleGenAiChatModel = googleGenAiChatModel;
    }


    @PostMapping("/ask/{query}")
    public ResponseEntity<?> askAI(@PathVariable String query) {

        return new ResponseEntity<>(googleGenAiChatModel.call(query), HttpStatus.OK);
    }
}
