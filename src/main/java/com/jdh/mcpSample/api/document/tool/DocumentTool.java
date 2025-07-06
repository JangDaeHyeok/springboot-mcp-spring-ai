package com.jdh.mcpSample.api.document.tool;

import com.jdh.mcpSample.api.document.application.DocumentGenerateService;
import com.jdh.mcpSample.api.document.domain.entity.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentTool {

    private final DocumentGenerateService excelGenerateServiceImpl;

    private final DocumentGenerateService pptGenerateServiceImpl;

    public DocumentTool(@Qualifier("excelGenerateServiceImpl") DocumentGenerateService excelGenerateServiceImpl,
                        @Qualifier("pptGenerateServiceImpl") DocumentGenerateService pptGenerateServiceImpl) {
        this.excelGenerateServiceImpl = excelGenerateServiceImpl;
        this.pptGenerateServiceImpl = pptGenerateServiceImpl;
    }

    @Tool(name = "generateDocument", description = "엑셀 혹은 파워포인트(ppt) 문서를 생성한다.")
    public Document generateDocument(@ToolParam(description = "문서의 제목") String title, @ToolParam(description = "문서의 내용") String content, @ToolParam(description = "문서 타입 (엑셀, 파워포인트(ppt))") String type) {
        log.info("generateDocument called with title: {}, content: {}, type: {}", title, content, type);

        return switch (type) {
            case "excel" -> excelGenerateServiceImpl.generateDocument(title, content);
            case "ppt" -> pptGenerateServiceImpl.generateDocument(title, content);
            default -> throw new IllegalArgumentException("Invalid document type");
        };
    }

}
