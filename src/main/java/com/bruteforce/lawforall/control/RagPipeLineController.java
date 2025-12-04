package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.dto.RagMetaDataInfo;
import com.bruteforce.lawforall.model.RagMetaData;
import com.bruteforce.lawforall.service.PdfLoaderService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class RagPipeLineController {

    private final PdfLoaderService pdfLoaderService;
    private final EmbeddingModel embeddingModel;

    public RagPipeLineController(PdfLoaderService pdfLoaderService, EmbeddingModel embeddingModel) {
        this.pdfLoaderService = pdfLoaderService;
        this.embeddingModel = embeddingModel;
    }

    @PostMapping("/rag-upload-pdf")
    public ResponseEntity<?> uploadPdf(@RequestPart String filename, @RequestPart(value = "file", required = true) MultipartFile file) {

        if (file == null || file.isEmpty() || !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed");
        }

        try {
            // Process directly from MultipartFile
            RagMetaData response = pdfLoaderService.loadPdfFromBytes(file.getBytes(),filename);

            return ResponseEntity.ok(response);

        } catch (IOException e) {

            throw new RuntimeException("Processing failed "+ e.getMessage());
        }
    }

    @GetMapping("/rag-files")
    public List<RagMetaDataInfo> getRagFileNames(){
        return pdfLoaderService.getRagFileNames();
    }

    @GetMapping("/embeddings")
    public ResponseEntity<?>  embed(@RequestParam String text1, @RequestParam String text2) {
        EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(text1,text2));
        return new ResponseEntity<>(embeddingResponse, HttpStatus.OK);
    }


    @DeleteMapping("/remove-rag-file")
    public ResponseEntity<?> deleteRagFile(@RequestParam String fileName){
        String response = pdfLoaderService.deleteRagFile(fileName);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
