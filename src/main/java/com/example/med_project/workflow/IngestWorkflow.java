package com.example.med_project.workflow;

import com.example.med_project.model.IngestContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestWorkflow {

    private final List<WorkflowStep> steps;

    public IngestWorkflow(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    public IngestContext run(IngestContext context) {
        IngestContext current = context;
        for (WorkflowStep step : steps) {
            current = step.execute(current);
        }
        return current;
    }
}
