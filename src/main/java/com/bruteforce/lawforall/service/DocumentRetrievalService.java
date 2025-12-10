package com.bruteforce.lawforall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStoreRetriever;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentRetrievalService {

    private final Logger logger = LoggerFactory.getLogger(DocumentRetrievalService.class);

    private final VectorStoreRetriever vectorStoreRetriever;

    public DocumentRetrievalService(VectorStoreRetriever vectorStoreRetriever) {
        this.vectorStoreRetriever = vectorStoreRetriever;
    }


    private List<Document> search(String query) {

        logger.info("Enter into search(query) => Searching for: {}", query);

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(4)
                .similarityThreshold(0.7)
                .build();

        List<Document> relevantDocs =  vectorStoreRetriever.similaritySearch(searchRequest);

        logger.info("Relevant Documents: ");
        relevantDocs.forEach(doc -> logger.info(doc.getMetadata().toString()));
        return relevantDocs;
    }


    public String getContext(String query){
        logger.info("Enter into getContext(query) => Query: {}", query);
        List<Document> relevantDocs = search(query);
        String context = relevantDocs.stream().map(Document::getFormattedContent).collect(Collectors.joining("\n\n"));
        logger.info("Context fetch from RAG {VectorStore}: {}", context);
        return context;
    }


}
