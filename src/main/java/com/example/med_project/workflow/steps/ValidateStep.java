package com.example.med_project.workflow.steps;

import com.example.med_project.model.IngestContext;
import com.example.med_project.workflow.WorkflowStep;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class ValidateStep implements WorkflowStep {

    @Override
    public IngestContext execute(IngestContext context) {
        if (context.request().patientId() == null || context.request().patientId().isBlank()) {
            throw new IllegalArgumentException("patientId is required");
        }
        if (context.file() == null || context.file().isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }
        return context;
    }
}
