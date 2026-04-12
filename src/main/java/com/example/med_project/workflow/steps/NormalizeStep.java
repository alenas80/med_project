package com.example.med_project.workflow.steps;

import com.example.med_project.model.IngestContext;
import com.example.med_project.workflow.WorkflowStep;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class NormalizeStep implements WorkflowStep {

    @Override
    public IngestContext execute(IngestContext context) {
        String textMeta = """
                patientId=%s
                modality=%s
                sourceName=%s
                metadata=%s
                """.formatted(
                context.request().patientId(),
                context.request().modality(),
                context.request().sourceName(),
                context.meta()
        );
        context.setTextMeta(textMeta);
        return context;
    }
}