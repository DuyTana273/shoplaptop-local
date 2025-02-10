package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.repository.ICategoryRepository;
import com.example.shoplaptop.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository iCategoryRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return iCategoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findAll() {
        return iCategoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        iCategoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        iCategoryRepository.delete(category);
    }

    @Override
    public boolean existsByName(String name) {
        return iCategoryRepository.existsByName(name);
    }

    @Override
    public Category getById(int id) {
        return iCategoryRepository.getById(id);
    }
}
