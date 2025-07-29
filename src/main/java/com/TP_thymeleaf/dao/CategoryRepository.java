package com.TP_thymeleaf.dao;

import com.TP_thymeleaf.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContains(String keyword);
}