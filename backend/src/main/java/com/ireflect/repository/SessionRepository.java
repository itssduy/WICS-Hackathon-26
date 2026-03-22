package com.ireflect.repository;

import com.ireflect.document.SessionDocument;
import com.ireflect.domain.SessionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends MongoRepository<SessionDocument, String> {
    Optional<SessionDocument> findByUserIdAndStatus(String userId, SessionStatus status);
    List<SessionDocument> findByUserIdAndSessionDate(String userId, LocalDate date);
    long countByUserIdAndSessionDate(String userId, LocalDate date);
    Optional<SessionDocument> findFirstByUserIdOrderByCreatedAtDesc(String userId);
    List<SessionDocument> findByUserIdOrderByCreatedAtDesc(String userId);
}
