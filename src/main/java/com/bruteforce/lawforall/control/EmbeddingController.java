package com.bruteforce.lawforall.control;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/ai")
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/embeddings")
    public ResponseEntity<?>  embed(@RequestParam String text1, @RequestParam String text2) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(text1,text2));
        return new ResponseEntity<>(embeddingResponse, HttpStatus.OK);
    }
}
