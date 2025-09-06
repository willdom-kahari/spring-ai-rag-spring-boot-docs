package com.waduclay.springdocs;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Command
public class SpringAssistantCommand {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:prompts/spring-boot-reference.st")
    private Resource sbPromptTemplate;

    public SpringAssistantCommand(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Command(command = "q")
    public String question(@DefaultValue("What is Spring Boot") String message) {
        Prompt prompt = PromptTemplate.builder()
                .resource(sbPromptTemplate)
                .variables(Map.of("input", message, "documents", String.join("\n", findSimilarDocuments(message))))
                .build()
                .create();
        return chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    private List<String> findSimilarDocuments(String message) {
        List<Document> documents = vectorStore.similaritySearch(message);
        return documents.stream()
                .map(Document::getText)
                .toList();
    }
}
