package com.example.shoplaptop.service.impl;

import com.example.shoplaptop.common.EncryptPasswordUtils;
import com.example.shoplaptop.model.Role;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.repository.IUserRepository;
import com.example.shoplaptop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EncryptPasswordUtils encryptPasswordUtils;

    @Override
    public Page<Users> findAll(Pageable pageable) {
        return iUserRepository.findAll(pageable);
    }

    @Override
    public Users getById(Long id) {
        return iUserRepository.getById(id);
    }

    @Override
    public void save(Users user) {
        try {
            if (user.getRole() == null) {
                user.setRole(Role.CUSTOMER);
            }

            if (user.getStatus() == null) {
                user.setStatus(true);
            }

            if (user.getAvatar() == null) {
                user.setAvatar("https://www.chem.indiana.edu/wp-content/uploads/2023/09/defaultpic.jpg");
            }

            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(encryptPasswordUtils.encryptPasswordUtils(user.getPassword()));
            }

            iUserRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi khi thêm người dùng : " + e.getMessage());
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return iUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return iUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        iUserRepository.deleteById(id);
    }

    @Override
    public List<Users> findAllByRole(Role role) {
        return iUserRepository.findAllByRole(role);
    }

    @Override
    public Page<Users> searchUsers(String keyword, Role role, Pageable pageable) {
        return iUserRepository.searchByKeywordAndRole(keyword, role, pageable);
    }

    @Override
    public long countUsers() {
        return iUserRepository.count();
    }

    @Override
    public String encryptPassword(String password) {
        return encryptPasswordUtils.encryptPasswordUtils(password);
    }

    @Override
    public Users findByUsername(String username) {
        return iUserRepository.findByUsername(username);
    }

    @Override
    public Users findByEmail(String email) {
        return iUserRepository.findByEmail(email);
    }
}