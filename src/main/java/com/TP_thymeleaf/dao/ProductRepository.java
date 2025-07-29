package com.TP_thymeleaf.dao;

import com.TP_thymeleaf.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContains(String keyword);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
