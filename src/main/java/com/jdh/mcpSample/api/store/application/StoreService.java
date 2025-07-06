package com.jdh.mcpSample.api.store.application;

import com.jdh.mcpSample.api.store.domain.entity.Store;

import java.util.List;

public interface StoreService {

    List<Store> getStoreListByRegionId(long regionId);

    Store getStoreById(long storeId);

    Long getStoreIdByName(String name);

    List<Store> getStores();

}
