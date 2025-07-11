package com.jdh.mcpSample.api.region.domain.repository;

import com.jdh.mcpSample.api.region.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByName(String name);

}
