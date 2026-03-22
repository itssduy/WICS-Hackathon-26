package com.ireflect.controller;

import com.ireflect.document.CategoryDocument;
import com.ireflect.repository.CategoryRepository;
import com.ireflect.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CategoryRepository categoryRepository;

    public AdminController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDocument category) {
        if (category.getSlug() == null || category.getSlug().isBlank()) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", "slug is required", "/api/admin/categories"));
        }
        category.setActive(true);
        CategoryDocument saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        var opt = categoryRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CategoryDocument cat = opt.get();
        if (updates.containsKey("name")) cat.setName((String) updates.get("name"));
        if (updates.containsKey("description")) cat.setDescription((String) updates.get("description"));
        if (updates.containsKey("isPremium")) cat.setPremium((Boolean) updates.get("isPremium"));
        if (updates.containsKey("isActive")) cat.setActive((Boolean) updates.get("isActive"));

        categoryRepository.save(cat);
        return ResponseEntity.ok(cat);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> listAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
}
