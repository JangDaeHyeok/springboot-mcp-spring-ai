package com.jdh.mcpSample.api.region.application.impl;

import com.jdh.mcpSample.api.region.application.RegionService;
import com.jdh.mcpSample.api.region.domain.entity.Region;
import com.jdh.mcpSample.api.region.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public Long getRegionIdByName(String name) {
        return regionRepository.findByName(name)
                .orElse(Region.builder().id(0L).build())
                .getId();
    }

    @Override
    public Region getRegionById(Long id) {
        return regionRepository.findById(id)
                .orElse(Region.builder().id(0L).build());
    }

    @Override
    public List<Region> getRegions() {
        return regionRepository.findAll();
    }

}
