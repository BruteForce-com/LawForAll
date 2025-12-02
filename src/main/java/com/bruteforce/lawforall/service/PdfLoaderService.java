package com.bruteforce.lawforall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This service is responsible for loading a PDF file into a vector store.
 * This is achieved by using the PagePdfDocumentReader to read the PDF file,
 * a TokenTextSplitter to transform the text into tokens, and then the vector store
 * to load the tokens into the vector store.
 *
 * @author Brute Force
 */
@Component
public class PdfLoaderService {

    private static final Logger log = LoggerFactory.getLogger(PdfLoaderService.class);
    private final VectorStore vectorStore;

    /**
     * Constructs a new PdfLoaderService with the given vector store.
     *
     * @param vectorStore the vector store to load the PDF file into
     */
    public PdfLoaderService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * Loads a PDF file into the vector store.
     * This is achieved by using the PagePdfDocumentReader to read the PDF file,
     * a TokenTextSplitter to transform the text into tokens, and then the vector store
     * to load the tokens into the vector store.
     *
     * @see PagePdfDocumentReader
     * @see TokenTextSplitter
     * @see VectorStore
     */
    public void loadPdfToVectorStore(String filePath) {
        // 1.Step => Create PDF reader
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                filePath,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPagesPerDocument(1)
                        .build()
        );

        // 2.Step => Create text-splitter
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        // 3.Step => ETL Pipeline: Read -> Transform -> Load
        vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));

        // Alternative method syntax
        // vectorStore.write(tokenTextSplitter.split(pdfReader.read()));
    }


    public String loadPdfFromBytes(byte[] extractedBytesFromPdf, String fileName) {

        // convert bytes to resources
        log.info("Entered into loadPdfFromBytes(param(2)=> bytes[],string) method in {}", PdfLoaderService.class.getName());
        Resource resource = new ByteArrayResource(extractedBytesFromPdf);
        log.info("Extracted PDF file name: {}", fileName);
        //1.STEP: Extract => Create a PDF Reader
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPagesPerDocument(1)
                        .build()
        );

        //2. STEP: Transform => Create text-splitter
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        //3. STEP: Load => ETL Pipeline: Read -> Transform -> Load
        try{
            vectorStore.accept(tokenTextSplitter.apply(reader.get()));
        }catch (Exception e){
            throw new RuntimeException("Error while loading PDF file into vectorstore " + e.getMessage());
        }

        return "PDF indexed into vectorstore successfully: " + fileName;
    }
}