package com.example.med_project.workflow.steps;

import com.example.med_project.embedding.MultimodalEmbeddingService;
import com.example.med_project.model.IngestContext;
import com.example.med_project.workflow.WorkflowStep;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(40)
public class EmbedStep implements WorkflowStep {

    private final MultimodalEmbeddingService embeddingService;

    public EmbedStep(MultimodalEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Override
    public IngestContext execute(IngestContext context) {
        float[] embedding = (context.imagesPng() != null && !context.imagesPng().isEmpty())
                ? embeddingService.embedImages(context.textMeta(), context.imagesPng())
                : embeddingService.embedText(context.textMeta());

        context.setEmbedding(embedding);
        return context;
    }
}