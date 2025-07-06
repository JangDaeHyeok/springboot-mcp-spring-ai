package com.jdh.mcpSample.api.document.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String fileName;

    private String fileUrl;

    private String downloadUrl;

    private DocumentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private String errorMessage;

    public enum DocumentStatus {
        PROCESSING,
        COMPLETED,
        FAILED
    }

    public static Document processing(String title) {
        return Document.builder()
                .title(title)
                .status(DocumentStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Document completed(long id, String title, String fileName, String fileUrl, String downloadUrl) {
        return Document.builder()
                .id(id)
                .title(title)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .downloadUrl(downloadUrl)
                .status(DocumentStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .build();
    }

    public static Document failed(long id, String title, String errorMessage) {
        return Document.builder()
                .id(id)
                .title(title)
                .status(DocumentStatus.FAILED)
                .errorMessage(errorMessage)
                .createdAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .build();
    }

}
