package com.example.shoplaptop.service;

import com.example.shoplaptop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    Page<Category> findAll(Pageable pageable);
    List<Category> findAll();
    void save(Category category);
    void delete(Category category);
    boolean existsByName(String name);
    Category getById(int id);
}
