package com.jdh.mcpSample.api.document.domain.repository;

import com.jdh.mcpSample.api.document.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
