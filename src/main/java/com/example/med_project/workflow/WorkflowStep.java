package com.example.med_project.workflow;

import com.example.med_project.model.IngestContext;

public interface WorkflowStep {
    IngestContext execute(IngestContext context);
}
