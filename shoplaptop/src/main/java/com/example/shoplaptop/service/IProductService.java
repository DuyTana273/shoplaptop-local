package com.example.shoplaptop.service;

import com.example.shoplaptop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<Product> findAll(Pageable pageable);
    Product getById(Integer id);
    void save(Product product);
    boolean existsByName(String name);
    void delete(Product product);
}
