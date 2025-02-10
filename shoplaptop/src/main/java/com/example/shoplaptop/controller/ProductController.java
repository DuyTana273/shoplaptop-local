package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Product;
import com.example.shoplaptop.model.dto.ProductDTO;
import com.example.shoplaptop.service.ICategoryService;
import com.example.shoplaptop.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/dashboard/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("")
    public String directToProductList(){
        return "redirect:/dashboard/products/list";
    }

    @GetMapping("/list")
    public String showProductList(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                  Model model) {
        Pageable pageable = PageRequest.of(page,10);
        Page<Product> products =  iProductService.findAll(pageable);
        model.addAttribute("products", products);
        return "dashboard/products/list";
    }

    @GetMapping("/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", iCategoryService.findAll());
        return "dashboard/products/create";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes ) {
        if (iProductService.existsByName(productDTO.getName())) {
            bindingResult.rejectValue("name", "", "Product name already exists");
        }

        new ProductDTO().validate(productDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", iCategoryService.findAll());
            model.addAttribute("product", productDTO);
            return "dashboard/products/create";
        }

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        iProductService.save(product);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Product added successfully");
        return "redirect:/dashboard/products/list";
    }

    @GetMapping("/{id}/detail")
    public String showProductDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Product product = iProductService.getById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Product not found");
            return "redirect:/dashboard/products/list";
        } else {
            model.addAttribute("product", product);
            return "dashboard/products/detail";
        }
    }

    @GetMapping("/{id}/edit")
    public String showProductEditForm(@PathVariable Integer id, Model model) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(iProductService.getById(id), productDTO);
        System.out.println("productDTO: "+productDTO.toString());
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categories", iCategoryService.findAll());
        return "dashboard/products/edit";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Product product = iProductService.getById(productDTO.getId());
        System.out.println("updated productDTO: "+productDTO.toString());
        if (!product.getName().equals(productDTO.getName()) && iProductService.existsByName(productDTO.getName())) {
            bindingResult.rejectValue("name", "", "Product name already exists");
        }

        new ProductDTO().validate(productDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", iCategoryService.findAll());
            model.addAttribute("product", productDTO);
            return "dashboard/products/edit";
        }

        BeanUtils.copyProperties(productDTO, product);
        iProductService.save(product);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Product updated successfully");
        return "redirect:/dashboard/products/list";
    }

    @GetMapping("/{id}/remove")
    public String removeProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Product product = iProductService.getById(id);
        if (product == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Product not found");
        } else {
            iProductService.delete(product);
            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Product removed successfully");
        }
        return "redirect:/dashboard/products/list";
    }

}
