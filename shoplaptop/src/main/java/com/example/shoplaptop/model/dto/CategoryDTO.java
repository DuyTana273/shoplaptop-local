package com.example.shoplaptop.model.dto;

import com.example.shoplaptop.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class CategoryDTO implements Validator {
    private Integer id;
    private String name;
    //@NotBlank(message = "This field must not be blank")
    private String logo;
    private List<Product> products;

    public CategoryDTO() {}

    public CategoryDTO(Integer id, String name, String logo, List<Product> products) {
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", products=" + products +
                '}';
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDTO category = (CategoryDTO) target;

        String name = category.getName();
        if (name.trim().isEmpty()) {
            errors.rejectValue("name", "input.null");
        }

        String logo = category.getLogo();
        if (logo == null || logo.isEmpty()) {
            errors.rejectValue("logo", "input.null");
        } else if (logo.length() > 255) {
            errors.rejectValue("logo", "", "Image Link is too long");
        }
    }
}
