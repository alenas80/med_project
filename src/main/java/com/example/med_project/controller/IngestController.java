package com.example.med_project.controller;

import com.example.med_project.model.IngestContext;
import com.example.med_project.model.IngestRequest;
import com.example.med_project.modality.Modality;
import com.example.med_project.workflow.IngestWorkflow;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/ingest")
public class IngestController {

    private final IngestWorkflow ingestWorkflow;

    public IngestController(IngestWorkflow ingestWorkflow) {
        this.ingestWorkflow = ingestWorkflow;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> ingest(
            @RequestParam String patientId,
            @RequestParam Modality modality,
            @RequestParam String sourceName,
            @RequestPart MultipartFile file
    ) {
        IngestRequest request = new IngestRequest(patientId, modality, sourceName);
        IngestContext context = new IngestContext(request, file);

        IngestContext result = ingestWorkflow.run(context);

        return Map.of(
                "status", "OK",
                "patientId", result.request().patientId(),
                "modality", result.request().modality(),
                "metadata", result.meta()
        );
    }
}