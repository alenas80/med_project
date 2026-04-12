package com.example.med_project.embedding;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OllamaMultimodalEmbeddingService implements MultimodalEmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final String multimodalEmbeddingModel;

    public OllamaMultimodalEmbeddingService(
            EmbeddingModel embeddingModel,
            @Value("${app.ollama.multimodal-embedding-model:}") String multimodalEmbeddingModel
    ) {
        this.embeddingModel = embeddingModel;
        this.multimodalEmbeddingModel = multimodalEmbeddingModel;
    }

    @Override
    public float[] embedText(String text) {
        return embeddingModel.embed(text);
    }

    @Override
    public float[] embedImages(String textPrompt, List<byte[]> imagesPng) {
        String fallback = textPrompt
                + "\nimages_count=" + imagesPng.size()
                + "\nmultimodalModel=" + multimodalEmbeddingModel;

        return embedText(fallback);
    }
}