package com.example.med_project.controller;

import com.example.med_project.model.QaRequest;
import com.example.med_project.model.QaResponse;
import com.example.med_project.rag.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qa")
public class QaController {

    private final RagService ragService;

    public QaController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping
    public QaResponse ask(@RequestBody QaRequest request) {
        return new QaResponse(ragService.answer(request.question()));
    }
}