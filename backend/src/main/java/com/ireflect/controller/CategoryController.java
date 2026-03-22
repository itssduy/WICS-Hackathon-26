package com.ireflect.controller;

import com.ireflect.document.CategoryDocument;
import com.ireflect.dto.CategoryResponse;
import com.ireflect.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> categories = categoryRepository.findByIsActiveTrue()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CategoryResponse>> getAvailable() {
        // For now, return non-premium only. Entitlement checks will be added in Phase 3.
        List<CategoryResponse> categories = categoryRepository.findByIsActiveTrueAndIsPremiumFalse()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    private CategoryResponse toResponse(CategoryDocument doc) {
        List<String> samplePrompts = doc.getPromptStages().getOpening()
            .stream()
            .limit(2)
            .map(CategoryDocument.PromptItem::getText)
            .collect(Collectors.toList());

        return new CategoryResponse(
            doc.getSlug(), doc.getName(), doc.getDescription(),
            doc.isPremium(), samplePrompts
        );
    }
}
