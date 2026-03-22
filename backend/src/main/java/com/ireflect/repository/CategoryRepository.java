package com.ireflect.repository;

import com.ireflect.document.CategoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<CategoryDocument, String> {
    Optional<CategoryDocument> findBySlug(String slug);
    List<CategoryDocument> findByIsActiveTrue();
    List<CategoryDocument> findByIsActiveTrueAndIsPremiumFalse();
}
