package com.ireflect.controller;

import com.ireflect.document.SessionDocument;
import com.ireflect.domain.SessionStatus;
import com.ireflect.dto.ApiError;
import com.ireflect.repository.SessionRepository;
import com.ireflect.repository.UserRepository;
import com.ireflect.service.SessionOrchestrationService;
import com.ireflect.service.SessionOrchestrationService.LimitReachedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionOrchestrationService orchestrationService;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionController(SessionOrchestrationService orchestrationService,
                             SessionRepository sessionRepository,
                             UserRepository userRepository) {
        this.orchestrationService = orchestrationService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startSession(Authentication auth, @RequestBody Map<String, String> body) {
        String userId = (String) auth.getPrincipal();
        String categorySlug = body.get("categorySlug");
        if (categorySlug == null || categorySlug.isBlank()) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", "categorySlug is required", "/api/sessions/start"));
        }

        String plan = userRepository.findById(userId).map(u -> u.getPlan().name()).orElse("FREE");

        try {
            Map<String, Object> result = orchestrationService.startSession(userId, categorySlug, plan);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (LimitReachedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiError(403, "PLAN_LIMIT_REACHED", e.getMessage(), "/api/sessions/start"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", e.getMessage(), "/api/sessions/start"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSession(Authentication auth, @PathVariable String id) {
        String userId = (String) auth.getPrincipal();
        Optional<SessionDocument> opt = sessionRepository.findById(id);
        if (opt.isEmpty() || !opt.get().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiError(404, "SESSION_NOT_FOUND", "Session not found", "/api/sessions/" + id));
        }
        return ResponseEntity.ok(opt.get());
    }

    @PostMapping("/{id}/next-question")
    public ResponseEntity<?> nextQuestion(Authentication auth, @PathVariable String id,
                                           @RequestBody(required = false) Map<String, String> body) {
        String userId = (String) auth.getPrincipal();
        String responseType = body != null ? body.getOrDefault("responseType", "REFLECTION_NOTE") : "REFLECTION_NOTE";
        String responseText = body != null ? body.getOrDefault("responseText", "") : "";

        try {
            Map<String, Object> result = orchestrationService.nextQuestion(id, userId, responseType, responseText);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", e.getMessage(), "/api/sessions/" + id + "/next-question"));
        }
    }

    @PostMapping("/{id}/refresh-question")
    public ResponseEntity<?> refreshQuestion(Authentication auth, @PathVariable String id) {
        String userId = (String) auth.getPrincipal();
        String plan = userRepository.findById(userId).map(u -> u.getPlan().name()).orElse("FREE");

        try {
            Map<String, Object> result = orchestrationService.refreshQuestion(id, userId, plan);
            return ResponseEntity.ok(result);
        } catch (LimitReachedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiError(403, "PLAN_LIMIT_REACHED", e.getMessage(), "/api/sessions/" + id + "/refresh-question"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", e.getMessage(), "/api/sessions/" + id + "/refresh-question"));
        }
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<?> endSession(Authentication auth, @PathVariable String id,
                                         @RequestBody(required = false) Map<String, String> body) {
        String userId = (String) auth.getPrincipal();
        String finalEmotion = body != null ? body.get("finalEmotion") : null;
        String finalCheckin = body != null ? body.get("finalCheckin") : null;
        String takeaway = body != null ? body.get("takeaway") : null;

        try {
            Map<String, Object> result = orchestrationService.endSession(id, userId, finalEmotion, finalCheckin, takeaway);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", e.getMessage(), "/api/sessions/" + id + "/end"));
        }
    }

    @GetMapping("/open")
    public ResponseEntity<?> getOpenSession(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        Optional<SessionDocument> open = sessionRepository.findByUserIdAndStatus(userId, SessionStatus.IN_PROGRESS);

        Map<String, Object> result = new LinkedHashMap<>();
        if (open.isPresent()) {
            SessionDocument s = open.get();
            result.put("hasOpenSession", true);

            Map<String, Object> session = new LinkedHashMap<>();
            session.put("sessionId", s.getId());
            session.put("categorySlug", s.getCategorySlug());
            session.put("questionNumber", s.getQuestionNumber());
            session.put("lastActiveAt", s.getUpdatedAt().toString());
            if (!s.getPromptHistory().isEmpty()) {
                session.put("previewPrompt", s.getPromptHistory().getLast().getPromptText());
            }
            result.put("session", session);
        } else {
            result.put("hasOpenSession", false);
            result.put("session", null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestSession(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        // Return the latest in-progress session if any
        Optional<SessionDocument> open = sessionRepository.findByUserIdAndStatus(userId, SessionStatus.IN_PROGRESS);
        if (open.isPresent()) {
            SessionDocument s = open.get();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("sessionId", s.getId());
            result.put("categorySlug", s.getCategorySlug());
            result.put("categoryName", s.getCategoryName());
            result.put("status", s.getStatus().name());
            result.put("questionNumber", s.getQuestionNumber());
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(Map.of());
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        var sessions = sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(sessions);
    }
}
