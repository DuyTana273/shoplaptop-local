package com.example.shoplaptop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(50)")
    private String email;

    @Column(columnDefinition = "varchar(15)")
    private String phone;

    @Column(name = "full-name",columnDefinition = "varchar(50)")
    private String fullName;

    @Column
    private Boolean gender;

    @Column(columnDefinition = "varchar(100)")
    private String address;

    @Column(columnDefinition = "varchar(255)")
    private String avatar;

    @Column(name = "status")
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private Role role;

    // Constructors
    public Users() {}

    public Users(Long id, String username, String password, String email, String phone, String fullName, Boolean gender, String address, String avatar, Boolean status, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.gender = gender;
        this.address = address;
        this.avatar = avatar;
        this.status = status;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
