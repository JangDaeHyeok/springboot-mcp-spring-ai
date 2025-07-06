package com.jdh.mcpSample.api.document.api;

import com.jdh.mcpSample.api.document.application.DocumentService;
import com.jdh.mcpSample.api.document.domain.entity.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController {

    @Value("${app.document.temp-dir}")
    private String tempDir;

    private final DocumentService documentService;

    @GetMapping("/download/excel/{id}")
    public ResponseEntity<Resource> downloadExcelFile(@PathVariable long id) {
        Document document = documentService.getDocumentById(id);

        File file = Paths.get(tempDir, document.getFileName()).toFile();
        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = URLEncoder.encode(document.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"\"; filename*=UTF-8''" + encodedFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @GetMapping("/download/ppt/{id}")
    public ResponseEntity<Resource> downloadPptFile(@PathVariable long id) {
        Document document = documentService.getDocumentById(id);

        File file = Paths.get(tempDir, document.getFileName()).toFile();
        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = URLEncoder.encode(document.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"\"; filename*=UTF-8''" + encodedFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation"))
                .body(resource);
    }

}
