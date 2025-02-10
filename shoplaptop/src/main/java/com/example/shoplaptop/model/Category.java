package com.example.shoplaptop.model;

import jakarta.persistence.*;
import java.util.List;


@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, columnDefinition = "varchar(50) not null")
    private String name;

    @Column(nullable = false)
    private String logo;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category() {}

    public Category(Integer id, String name, String logo, List<Product> products) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
