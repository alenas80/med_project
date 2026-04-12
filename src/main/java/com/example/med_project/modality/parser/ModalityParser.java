package com.example.med_project.modality.parser;

import com.example.med_project.model.IngestContext;
import com.example.med_project.modality.Modality;

public interface ModalityParser {
    boolean supports(Modality modality);
    IngestContext parse(IngestContext context);
}
