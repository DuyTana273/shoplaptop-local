package com.example.shoplaptop.controller;


import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.model.dto.CategoryDTO;
import com.example.shoplaptop.repository.ICategoryRepository;
import com.example.shoplaptop.service.ICategoryService;
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
@RequestMapping("/dashboard/productTypes")
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("")
    public String category() {
        return "redirect:/dashboard/productTypes/list";
    }

    @GetMapping("/list")
    public String showCategoryPage(@RequestParam(name = "page", defaultValue = "0", required = false) int page, Model model) {
        Pageable pageable = PageRequest.of(page,3);
        Page<Category> categories = iCategoryService.findAll(pageable);
        model.addAttribute("categories", categories);
        return "dashboard/category/list";
    }

    @GetMapping("/create")
    public String showCreateCategoryPage(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "/dashboard/category/create";
    }

    @PostMapping("/add")
    public String addCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (iCategoryService.existsByName(categoryDTO.getName())) {
            bindingResult.rejectValue("name", "", "Category name already exists");
        }
        new CategoryDTO().validate(categoryDTO,bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDTO", categoryDTO);
            return "/dashboard/category/create";
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        iCategoryService.save(category);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Category created successfully");
        return "redirect:/dashboard/productTypes/list";
    }

    @GetMapping("/{id}/edit")
    public String showEditCategoryPage(@PathVariable int id, Model model) {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(iCategoryService.getById(id),categoryDTO);
        model.addAttribute("categoryDTO", categoryDTO);
        return "/dashboard/category/edit";
    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Category category = iCategoryService.getById(categoryDTO.getId());
        if (!category.getName().equals(categoryDTO.getName()) && iCategoryService.existsByName(categoryDTO.getName())) {
            bindingResult.rejectValue("name", "", "Category name already exists");
        }
        new CategoryDTO().validate(categoryDTO,bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDTO", categoryDTO);
            return "/dashboard/category/edit";
        }
        BeanUtils.copyProperties(categoryDTO,category);
        iCategoryService.save(category);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Category updated successfully");
        return "redirect:/dashboard/productTypes/list";
    }

    @GetMapping("/{id}/remove")
    public String removeCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Category category = iCategoryService.getById(id);
        if (category == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Category not found");
        } else {
            iCategoryService.delete(category);
            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Category removed successfully");
        }
        return "redirect:/dashboard/productTypes/list";
    }
}
