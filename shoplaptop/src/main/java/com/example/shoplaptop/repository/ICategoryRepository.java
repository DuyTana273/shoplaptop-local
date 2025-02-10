package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAll(Pageable pageable);
    List<Category> findAll();
    Category save(Category category);
    void delete(Category category);
    boolean existsByName(String name);
    Category getById(int id);
}
