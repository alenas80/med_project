package com.example.med_project.embedding;

import java.util.List;

public interface MultimodalEmbeddingService {
    float[] embedText(String text);
    float[] embedImages(String textPrompt, List<byte[]> imagesPng);
}