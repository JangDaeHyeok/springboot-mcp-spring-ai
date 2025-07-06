package com.jdh.mcpSample.api.product.tool;

import com.jdh.mcpSample.api.product.application.ProductService;
import com.jdh.mcpSample.api.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductTool {

    private final ProductService productService;

    @Tool(name = "getProductsListByKeywordTool", description = "검색 키워드로 상품 목록 조회")
    public List<Product> getProductsListByKeywordTool(@ToolParam(description = "search keyword") String keyword) {
        log.info("getProductsListByKeywordTool Tool called with keyword: {}", keyword);

        return productService.getAllProducts().stream()
                .filter(o -> keyword.equals(o.getName()))
                .toList();
    }

    @Tool(name = "getProductsListByStoreIdTool", description = "상점 id로 상품 목록 조회")
    public List<Product> getProductsByStoreIdTool(@ToolParam(description = "store id") long storeId) {
        log.info("getProductsByStoreIdTool Tool called with storeId: {}", storeId);

        return productService.getProductListByStoreId(storeId);
    }

    @Tool(name = "getProductByNameTool", description = "상품명으로 상품 목록 검색")
    public List<Product> getProductsByNameTool(@ToolParam(description = "products name") String name) {
        log.info("getProductsByNameTool Tool called with name: {}", name);

        return productService.getProductsByName(name);
    }

}
