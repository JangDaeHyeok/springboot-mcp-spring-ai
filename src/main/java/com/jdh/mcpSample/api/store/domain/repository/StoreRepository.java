package com.jdh.mcpSample.api.store.domain.repository;

import com.jdh.mcpSample.api.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByName(String name);

    Optional<Store> findByRegionId(long regionId);

}
