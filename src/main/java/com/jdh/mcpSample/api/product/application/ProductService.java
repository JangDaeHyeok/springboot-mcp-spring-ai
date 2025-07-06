package com.jdh.mcpSample.api.product.application;

import com.jdh.mcpSample.api.product.domain.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getProductListByStoreId(long storeId);

    List<Product> getProductsByName(String name);

}
