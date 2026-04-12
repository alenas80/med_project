package com.example.med_project.model;

import com.example.med_project.modality.Modality;

public record IngestRequest(
        String patientId,
        Modality modality,
        String sourceName
) {
}
