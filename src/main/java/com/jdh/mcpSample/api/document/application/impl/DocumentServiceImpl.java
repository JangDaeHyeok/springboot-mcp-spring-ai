package com.jdh.mcpSample.api.document.application.impl;

import com.jdh.mcpSample.api.document.application.DocumentService;
import com.jdh.mcpSample.api.document.domain.entity.Document;
import com.jdh.mcpSample.api.document.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public Document getDocumentById(long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
