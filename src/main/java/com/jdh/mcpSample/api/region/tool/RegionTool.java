package com.jdh.mcpSample.api.region.tool;

import com.jdh.mcpSample.api.region.application.RegionService;
import com.jdh.mcpSample.api.region.domain.entity.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionTool {

    private final RegionService regionService;

    @Tool(name = "getRegionIdByNameTool", description = "지역명으로 지역 검색")
    public Long getRegionIdByNameTool(@ToolParam(description = "region name") String name) {
        log.info("getRegionIdByNameTool Tool called with name: {}", name);

        var result = regionService.getRegionIdByName(name);
        log.info("getRegionIdByNameTool Tool result: {}", result);
        return result;
    }

    @Tool(name = "getRegionById", description = "지역 id로 지역 검색")
    public Region getRegionById(Long regionId) {
        log.info("getRegionByIdTool Tool called with regionId: {}", regionId);

        return regionService.getRegionById(regionId);
    }

    @Tool(name = "getRegions", description = "전제 지역 목록 검색")
    public List<Region> getRegions() {
        log.info("getRegions Tool called");

        return regionService.getRegions();
    }

}
