package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private IProductService iProductService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/allProduct")
    public String showProductList(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                  Model model) {
        Pageable pageable = PageRequest.of(page,10);
        Page<Product> products =  iProductService.findAll(pageable);
        model.addAttribute("products", products);
        return "allProduct";
    }
}
