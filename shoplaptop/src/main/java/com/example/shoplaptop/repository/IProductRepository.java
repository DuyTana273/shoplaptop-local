package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);
    Product getById(Integer id);
    Product save(Product product);
    boolean existsByName(String name);
    void delete(Product product);
}
