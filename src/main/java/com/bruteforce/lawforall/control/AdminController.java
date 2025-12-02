package com.bruteforce.lawforall.control;

import com.bruteforce.lawforall.service.PdfLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PdfLoaderService pdfLoaderService;

    public AdminController(PdfLoaderService pdfLoaderService) {
        this.pdfLoaderService = pdfLoaderService;
    }

    @PostMapping("/rag-upload-pdf")
    public ResponseEntity<String> uploadPdf(@RequestParam(value = "file", required = true) MultipartFile file) {

        if (file == null || file.isEmpty() || !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed");
        }

        try {
            // Process directly from MultipartFile
            String response = pdfLoaderService.loadPdfFromBytes(file.getBytes(), file.getOriginalFilename());

            return ResponseEntity.ok(response);

        } catch (IOException e) {

            throw new RuntimeException("Processing failed "+ e.getMessage());
        }
    }


}
