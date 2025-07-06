package com.jdh.mcpSample.api.store.application.impl;

import com.jdh.mcpSample.api.store.application.StoreService;
import com.jdh.mcpSample.api.store.domain.entity.Store;
import com.jdh.mcpSample.api.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public List<Store> getStoreListByRegionId(long regionId) {
        return storeRepository.findAll().stream()
                .filter(store -> store.getRegionId() == regionId)
                .toList();
    }

    @Override
    public Store getStoreById(long storeId) {
        return storeRepository.findById(storeId).orElse(null);
    }

    @Override
    public Long getStoreIdByName(String name) {
        return storeRepository.findByName(name)
                .orElse(Store.builder().id(0L).build())
                .getId();
    }

    @Override
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

}
