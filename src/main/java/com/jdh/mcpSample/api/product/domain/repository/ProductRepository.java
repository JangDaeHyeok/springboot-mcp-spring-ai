package com.jdh.mcpSample.api.product.domain.repository;

import com.jdh.mcpSample.api.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String name);

    List<Product> findAllByStoreId(long storeId);

}
