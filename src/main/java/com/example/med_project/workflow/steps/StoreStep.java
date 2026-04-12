package com.example.med_project.workflow.steps;

import com.example.med_project.model.IngestContext;
import com.example.med_project.vectorstore.VectorStoreWriter;
import com.example.med_project.workflow.WorkflowStep;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(50)
public class StoreStep implements WorkflowStep {

    private final VectorStoreWriter vectorStoreWriter;

    public StoreStep(VectorStoreWriter vectorStoreWriter) {
        this.vectorStoreWriter = vectorStoreWriter;
    }

    @Override
    public IngestContext execute(IngestContext context) {
        vectorStoreWriter.save(context);
        return context;
    }
}