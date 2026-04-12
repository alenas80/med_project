package com.example.med_project.vectorstore;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;

    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> search(String query) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .build()
        );
    }
}