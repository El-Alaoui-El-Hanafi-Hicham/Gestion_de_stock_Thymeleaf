package com.TP_thymeleaf.controller;

import com.TP_thymeleaf.dao.CategoryRepository;
import com.TP_thymeleaf.entity.Category;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public String listCategories(Model model,
                                 @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<Category> categories;
        if (keyword.isEmpty()) {
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findByNameContains(keyword);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        return "categories";
    }

    @GetMapping("/deleteCategory")
    public String deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/formCategory")
    public String formCategory(Model model) {
        model.addAttribute("category", new Category());
        return "formCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "formCategory";
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/editCategory")
    public String editCategory(Model model, Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) throw new RuntimeException("Category not found");
        model.addAttribute("category", category);
        return "editCategory";
    }
}