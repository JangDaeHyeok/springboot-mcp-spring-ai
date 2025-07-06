package com.jdh.mcpSample.api.store.tool;

import com.jdh.mcpSample.api.store.application.StoreService;
import com.jdh.mcpSample.api.store.domain.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreTool {

    private final StoreService storeService;

    @Tool(name = "getStoreIdByNameTool", description = "상점 이름으로 상점 id 검색")
    public Long getStoreIdByName(@ToolParam(description = "store name") String name) {
        log.info("getStoreIdByName Tool called with name: {}", name);

        return storeService.getStoreIdByName(name);
    }

    @Tool(name = "getStoreById", description = "상점 id로 상점 검색")
    public Store getStoreById(long storeId) {
        log.info("getStoreById Tool called with storeId: {}", storeId);

        return storeService.getStoreById(storeId);
    }

    @Tool(name = "getStoreListByRegionIdTool", description = "지역 id로 상점 목록 검색")
    public List<Store> getStoreListByRegionId(@ToolParam(description = "region id") long regionId) {
        log.info("getStoreListByRegionId Tool called with regionId: {}", regionId);

        return storeService.getStoreListByRegionId(regionId);
    }

    @Tool(name = "getStores", description = "전체 지역 목록 검색")
    public List<Store> getStores() {
        log.info("getStores Tool called ");

        return storeService.getStores();
    }

}
