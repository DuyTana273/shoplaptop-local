package com.example.shoplaptop.service;

import com.example.shoplaptop.model.Role;
import com.example.shoplaptop.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    Page<Users> findAll(Pageable pageable);

    Users findByUsername(String username);

    Users findByEmail(String email);

    Users getById(Long id);

    void save(Users user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteById(Long id);

    List<Users> findAllByRole(Role role);

    Page<Users> searchUsers(String keyword, Role role, Pageable pageable);

    long countUsers();

    String encryptPassword(String password);
}
