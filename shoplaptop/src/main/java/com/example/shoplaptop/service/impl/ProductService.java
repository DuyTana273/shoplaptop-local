package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.repository.IProductRepository;
import com.example.shoplaptop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository iProductRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return iProductRepository.findAll(pageable);
    }

    @Override
    public Product getById(Integer id) {
        return iProductRepository.getById(id);
    }

    @Override
    public void save(Product product) {
        iProductRepository.save(product);
    }

    @Override
    public boolean existsByName(String name) {
        return iProductRepository.existsByName(name);
    }

    @Override
    public void delete(Product product) {
        iProductRepository.delete(product);
    }
}
