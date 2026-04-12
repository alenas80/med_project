package com.example.med_project.vectorstore;

import com.example.med_project.model.IngestContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorStoreWriter {

    private final VectorStore vectorStore;

    public VectorStoreWriter(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void save(IngestContext context) {
        Document document = new Document(context.textMeta(), context.meta());
        vectorStore.add(List.of(document));
    }
}