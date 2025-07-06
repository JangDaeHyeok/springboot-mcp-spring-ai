package com.jdh.mcpSample.api.region.application;

import com.jdh.mcpSample.api.region.domain.entity.Region;

import java.util.List;

public interface RegionService {

    Long getRegionIdByName(String name);

    Region getRegionById(Long id);

    List<Region> getRegions();

}
