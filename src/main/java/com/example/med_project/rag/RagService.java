package com.example.med_project.rag;

import com.example.med_project.vectorstore.VectorSearchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorSearchService vectorSearchService;

    public RagService(ChatClient.Builder chatClientBuilder,
                      VectorSearchService vectorSearchService) {
        this.chatClient = chatClientBuilder.build();
        this.vectorSearchService = vectorSearchService;
    }

    public String answer(String question) {
        List<Document> docs = vectorSearchService.search(question);

        String context = docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        return chatClient.prompt()
                .user("""
                        Ты медицинский AI-ассистент.
                        Отвечай только на основе предоставленного контекста.
                        Если данных недостаточно, так и скажи.

                        Вопрос:
                        %s

                        Контекст:
                        %s
                        """.formatted(question, context))
                .call()
                .content();
    }
}