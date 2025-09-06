package com.waduclay.springdocs;

import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication(exclude = {OpenAiEmbeddingAutoConfiguration.class})
public class SpringDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDocsApplication.class, args);
    }

}
