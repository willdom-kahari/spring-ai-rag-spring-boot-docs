package com.waduclay.springdocs;

import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {OpenAiEmbeddingAutoConfiguration.class})
public class SpringDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDocsApplication.class, args);
    }

}
