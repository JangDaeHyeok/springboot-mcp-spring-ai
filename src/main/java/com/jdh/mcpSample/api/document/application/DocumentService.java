package com.jdh.mcpSample.api.document.application;

import com.jdh.mcpSample.api.document.domain.entity.Document;

public interface DocumentService {

    Document getDocumentById(long documentId);

}
