package com.waduclay.springdocs;


import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
public class ReferenceDocsLoader {
    private static final Logger log = LoggerFactory.getLogger(ReferenceDocsLoader.class);
    private final JdbcClient jdbcClient;
    private final VectorStore vectorStore;
    @Value("classpath:docs/spring-boot-reference.pdf")
    private Resource pdfResource;

    public ReferenceDocsLoader(JdbcClient jdbcClient, VectorStore vectorStore) {
        this.jdbcClient = jdbcClient;
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    private void init(){
        Integer count = 0;

        try {
            count = jdbcClient.sql("select count(*) from vector_store")
                    .query(Integer.class)
                    .single();
            log.info("Vector store count: {}", count);
        } catch (Exception e) {
            log.info("Vector store table doesn't exist yet, will create it by loading documents");
        }


        if(count == 0){
            log.info("Loading Spring Boot Reference PDF into Vector Store");
            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(
                            new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
                                    .withNumberOfTopPagesToSkipBeforeDelete(0)
                                    .build()
                    ).withPagesPerDocument(1)
                    .build();

            PagePdfDocumentReader documentReader = new PagePdfDocumentReader(pdfResource, config);
            TokenTextSplitter textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(documentReader.get()));
            log.info("Vector store loaded");

        }

    }
}
