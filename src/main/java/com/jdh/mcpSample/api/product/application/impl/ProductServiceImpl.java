package com.jdh.mcpSample.api.product.application.impl;

import com.jdh.mcpSample.api.product.application.ProductService;
import com.jdh.mcpSample.api.product.domain.entity.Product;
import com.jdh.mcpSample.api.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductListByStoreId(long storeId) {
        return productRepository.findAllByStoreId(storeId);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findAllByName(name);
    }

}
