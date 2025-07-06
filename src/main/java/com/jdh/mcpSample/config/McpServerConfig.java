package com.jdh.mcpSample.config;

import com.jdh.mcpSample.api.document.tool.DocumentTool;
import com.jdh.mcpSample.api.product.tool.ProductTool;
import com.jdh.mcpSample.api.region.tool.RegionTool;
import com.jdh.mcpSample.api.store.tool.StoreTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class McpServerConfig {

    private final ProductTool productTool;

    private final RegionTool regionTool;

    private final StoreTool storeTool;

    private final DocumentTool documentTool;

    @Bean
    public ToolCallbackProvider toolCallbackProvider() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        productTool,
                        regionTool,
                        storeTool,
                        documentTool
                )
                .build();
    }

}
