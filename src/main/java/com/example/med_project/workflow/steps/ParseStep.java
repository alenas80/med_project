package com.example.med_project.workflow.steps;

import com.example.med_project.model.IngestContext;
import com.example.med_project.modality.parser.ModalityParser;
import com.example.med_project.workflow.WorkflowStep;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(20)
public class ParseStep implements WorkflowStep {

    private final List<ModalityParser> parsers;

    public ParseStep(List<ModalityParser> parsers) {
        this.parsers = parsers;
    }

    @Override
    public IngestContext execute(IngestContext context) {
        return parsers.stream()
                .filter(parser -> parser.supports(context.request().modality()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No parser for modality: " + context.request().modality()))
                .parse(context);
    }
}