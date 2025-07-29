package com.TP_thymeleaf.controller;

import com.TP_thymeleaf.dao.CategoryRepository;
import com.TP_thymeleaf.dao.ProductRepository;
import com.TP_thymeleaf.entity.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/products")
    public String listProducts(Model model,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<Product> products = productRepository.findByNameContains(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }



    @PostMapping("/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "formProduct";
        productRepository.save(product);
        return "redirect:/products";
    }
    @GetMapping("/formProduct")
    public String formProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "formProduct";
    }

    @GetMapping("/editProduct")
    public String editProduct(Model model, Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) throw new RuntimeException("Product not found");
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "editProduct";
    }
}
