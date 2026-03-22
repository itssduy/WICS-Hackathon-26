package com.ireflect.repository;

import com.ireflect.document.UserProfileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfileDocument, String> {
    Optional<UserProfileDocument> findByUserId(String userId);
}
