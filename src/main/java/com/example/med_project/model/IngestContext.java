package com.example.med_project.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngestContext {

    private final IngestRequest request;
    private final MultipartFile file;
    private final Map<String, Object> meta = new HashMap<>();
    private final List<byte[]> imagesPng = new ArrayList<>();

    private String textMeta = "";
    private float[] embedding;

    public IngestContext(IngestRequest request, MultipartFile file) {
        this.request = request;
        this.file = file;
    }

    public IngestRequest request() {
        return request;
    }

    public MultipartFile file() {
        return file;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    public List<byte[]> imagesPng() {
        return imagesPng;
    }

    public String textMeta() {
        return textMeta;
    }

    public void setTextMeta(String textMeta) {
        this.textMeta = textMeta;
    }

    public float[] embedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public void putMeta(String key, Object value) {
        this.meta.put(key, value);
    }
}