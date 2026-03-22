package com.ireflect.service;

import com.ireflect.document.CategoryDocument;
import com.ireflect.document.CategoryDocument.PromptItem;
import com.ireflect.document.CategoryDocument.PromptStages;
import com.ireflect.document.SessionDocument;
import com.ireflect.document.SessionDocument.PromptRecord;
import com.ireflect.document.SessionDocument.ResponseRecord;
import com.ireflect.document.UserProfileDocument;
import com.ireflect.domain.FinalEmotion;
import com.ireflect.domain.SessionStatus;
import com.ireflect.repository.CategoryRepository;
import com.ireflect.repository.SessionRepository;
import com.ireflect.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class SessionOrchestrationService {

    private final SessionRepository sessionRepository;
    private final CategoryRepository categoryRepository;
    private final UserProfileRepository profileRepository;
    private final SafetyService safetyService;

    @Value("${ireflect.plan.free.daily-refresh-limit:5}")
    private int freeRefreshLimit;

    @Value("${ireflect.plan.free.daily-session-limit:3}")
    private int freeSessionLimit;

    private static final String[] STAGE_ORDER = {"OPENING", "DEEPENING", "REFRAMING", "RELEASE", "CLOSURE"};
    private static final int MIN_PROMPTS = 4;
    private static final int MAX_PROMPTS = 7;

    public SessionOrchestrationService(SessionRepository sessionRepository,
                                       CategoryRepository categoryRepository,
                                       UserProfileRepository profileRepository,
                                       SafetyService safetyService) {
        this.sessionRepository = sessionRepository;
        this.categoryRepository = categoryRepository;
        this.profileRepository = profileRepository;
        this.safetyService = safetyService;
    }

    /**
     * Start a new session.
     */
    public Map<String, Object> startSession(String userId, String categorySlug, String plan) {
        // Check daily session limit for free plan
        if ("FREE".equals(plan)) {
            long todaySessions = sessionRepository.countByUserIdAndSessionDate(userId, LocalDate.now());
            if (todaySessions >= freeSessionLimit) {
                throw new LimitReachedException("Daily session limit reached for free plan");
            }
        }

        // Check for existing in-progress session
        var existingOpen = sessionRepository.findByUserIdAndStatus(userId, SessionStatus.IN_PROGRESS);
        if (existingOpen.isPresent()) {
            // End the old one early
            SessionDocument old = existingOpen.get();
            old.setStatus(SessionStatus.ENDED_EARLY);
            old.setEndedAt(Instant.now());
            old.setUpdatedAt(Instant.now());
            sessionRepository.save(old);
        }

        CategoryDocument category = categoryRepository.findBySlug(categorySlug)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categorySlug));

        SessionDocument session = new SessionDocument(userId, categorySlug, category.getName());

        // Select first prompt
        PromptItem firstPrompt = selectPrompt(category, "OPENING", session.getUsedPromptIds(), userId);
        PromptRecord record = new PromptRecord(1, "OPENING", firstPrompt.getId(), firstPrompt.getText());
        session.getPromptHistory().add(record);
        session.getUsedPromptIds().add(firstPrompt.getId());

        session = sessionRepository.save(session);

        long todaySessions = sessionRepository.countByUserIdAndSessionDate(userId, LocalDate.now());
        int dailyRefreshesUsed = countDailyRefreshes(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", session.getId());
        result.put("status", session.getStatus().name());
        result.put("categorySlug", categorySlug);
        result.put("questionNumber", 1);
        result.put("questionTotalHint", MAX_PROMPTS);

        Map<String, Object> prompt = new LinkedHashMap<>();
        prompt.put("stepNumber", 1);
        prompt.put("promptStage", "OPENING");
        prompt.put("promptText", firstPrompt.getText());
        result.put("prompt", prompt);

        Map<String, Object> usage = new LinkedHashMap<>();
        usage.put("refreshesUsed", dailyRefreshesUsed);
        usage.put("refreshesLimit", freeRefreshLimit);
        usage.put("sessionsStarted", todaySessions);
        usage.put("sessionsLimit", freeSessionLimit);
        result.put("dailyUsage", usage);

        return result;
    }

    /**
     * Advance to next question.
     */
    public Map<String, Object> nextQuestion(String sessionId, String userId, String responseType, String responseText) {
        SessionDocument session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (!session.getUserId().equals(userId)) throw new IllegalArgumentException("Not your session");
        if (session.getStatus() != SessionStatus.IN_PROGRESS) throw new IllegalArgumentException("Session is not active");

        // Safety check on response text
        if (safetyService.containsDangerLanguage(responseText)) {
            session.setStatus(SessionStatus.SAFETY_INTERRUPTED);
            session.setUpdatedAt(Instant.now());
            sessionRepository.save(session);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("sessionId", sessionId);
            result.put("status", "SAFETY_INTERRUPTED");
            result.put("safetyMessage", safetyService.getSupportiveMessage());
            result.put("crisisResources", safetyService.getCrisisResources());
            return result;
        }

        // Record response
        ResponseRecord response = new ResponseRecord(session.getQuestionNumber(),
            responseType != null ? responseType : "REFLECTION_NOTE", responseText);
        session.getResponseHistory().add(response);

        // Determine next stage
        int nextStep = session.getQuestionNumber() + 1;
        String nextStage = determineNextStage(session);

        // Check if session should end
        if (nextStep > MAX_PROMPTS || "DONE".equals(nextStage)) {
            session.setStatus(SessionStatus.COMPLETED);
            session.setEndedAt(Instant.now());
            session.setUpdatedAt(Instant.now());
            session.setSummaryText(generateTemplateSummary(session));
            sessionRepository.save(session);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("sessionId", sessionId);
            result.put("status", "COMPLETED");
            result.put("summaryText", session.getSummaryText());
            result.put("endedAt", session.getEndedAt().toString());
            return result;
        }

        CategoryDocument category = categoryRepository.findBySlug(session.getCategorySlug())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        PromptItem nextPrompt = selectPrompt(category, nextStage, session.getUsedPromptIds(), userId);

        PromptRecord record = new PromptRecord(nextStep, nextStage, nextPrompt.getId(), nextPrompt.getText());
        session.getPromptHistory().add(record);
        session.getUsedPromptIds().add(nextPrompt.getId());
        session.setQuestionNumber(nextStep);
        session.setUpdatedAt(Instant.now());
        sessionRepository.save(session);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", sessionId);
        result.put("questionNumber", nextStep);
        result.put("questionTotalHint", MAX_PROMPTS);
        Map<String, Object> prompt = new LinkedHashMap<>();
        prompt.put("stepNumber", nextStep);
        prompt.put("promptStage", nextStage);
        prompt.put("promptText", nextPrompt.getText());
        result.put("prompt", prompt);
        result.put("status", "IN_PROGRESS");
        return result;
    }

    /**
     * Refresh current question.
     */
    public Map<String, Object> refreshQuestion(String sessionId, String userId, String plan) {
        SessionDocument session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (!session.getUserId().equals(userId)) throw new IllegalArgumentException("Not your session");
        if (session.getStatus() != SessionStatus.IN_PROGRESS) throw new IllegalArgumentException("Session is not active");

        // Check refresh limit
        int dailyRefreshes = countDailyRefreshes(userId);
        if ("FREE".equals(plan) && dailyRefreshes >= freeRefreshLimit) {
            throw new LimitReachedException("Daily refresh limit reached for free plan");
        }

        // Get current stage
        PromptRecord lastPrompt = session.getPromptHistory().getLast();
        String currentStage = lastPrompt.getPromptStage();

        CategoryDocument category = categoryRepository.findBySlug(session.getCategorySlug())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        PromptItem replacement = selectPrompt(category, currentStage, session.getUsedPromptIds(), userId);

        // Replace last prompt
        session.getPromptHistory().removeLast();
        PromptRecord newRecord = new PromptRecord(
            session.getQuestionNumber(), currentStage, replacement.getId(), replacement.getText());
        session.getPromptHistory().add(newRecord);
        session.getUsedPromptIds().add(replacement.getId());
        session.setRefreshesUsed(session.getRefreshesUsed() + 1);
        session.setUpdatedAt(Instant.now());
        sessionRepository.save(session);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", sessionId);
        Map<String, Object> prompt = new LinkedHashMap<>();
        prompt.put("stepNumber", session.getQuestionNumber());
        prompt.put("promptStage", currentStage);
        prompt.put("promptText", replacement.getText());
        result.put("prompt", prompt);

        Map<String, Object> usage = new LinkedHashMap<>();
        usage.put("refreshesUsed", dailyRefreshes + 1);
        usage.put("refreshesLimit", freeRefreshLimit);
        result.put("dailyUsage", usage);

        return result;
    }

    /**
     * End session with closure data.
     */
    public Map<String, Object> endSession(String sessionId, String userId,
                                           String finalEmotion, String finalCheckin, String takeaway) {
        SessionDocument session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (!session.getUserId().equals(userId)) throw new IllegalArgumentException("Not your session");

        // Safety check on closure text
        String allText = (finalCheckin != null ? finalCheckin : "") + " " + (takeaway != null ? takeaway : "");
        if (safetyService.containsDangerLanguage(allText)) {
            session.setStatus(SessionStatus.SAFETY_INTERRUPTED);
            session.setUpdatedAt(Instant.now());
            sessionRepository.save(session);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("sessionId", sessionId);
            result.put("status", "SAFETY_INTERRUPTED");
            result.put("safetyMessage", safetyService.getSupportiveMessage());
            result.put("crisisResources", safetyService.getCrisisResources());
            return result;
        }

        if (finalEmotion != null) {
            try { session.setFinalEmotion(FinalEmotion.valueOf(finalEmotion)); } catch (Exception ignored) {}
        }
        session.setFinalCheckin(finalCheckin);
        session.setTakeaway(takeaway);
        session.setStatus(SessionStatus.COMPLETED);
        session.setEndedAt(Instant.now());
        session.setUpdatedAt(Instant.now());
        session.setSummaryText(generateTemplateSummary(session));
        sessionRepository.save(session);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", sessionId);
        result.put("status", "COMPLETED");
        result.put("summaryText", session.getSummaryText());
        result.put("endedAt", session.getEndedAt().toString());
        return result;
    }

    // ---------- Internal helpers ----------

    private String determineNextStage(SessionDocument session) {
        int step = session.getQuestionNumber();
        if (step >= MAX_PROMPTS) return "DONE";
        if (step <= 1) return "DEEPENING";
        if (step == 2) return "DEEPENING";
        if (step == 3) return "REFRAMING";
        if (step == 4) return "RELEASE";
        return "CLOSURE";
    }

    private PromptItem selectPrompt(CategoryDocument category, String stage,
                                     List<String> usedIds, String userId) {
        PromptStages stages = category.getPromptStages();
        List<PromptItem> pool = switch (stage) {
            case "OPENING"   -> stages.getOpening();
            case "DEEPENING" -> stages.getDeepening();
            case "REFRAMING" -> stages.getReframing();
            case "RELEASE"   -> stages.getRelease();
            case "CLOSURE"   -> stages.getClosure();
            default          -> stages.getOpening();
        };

        // Filter out used prompts and topic-blocked prompts
        List<String> topicsToAvoid = getTopicsToAvoid(userId);
        List<PromptItem> available = pool.stream()
            .filter(p -> !usedIds.contains(p.getId()))
            .filter(p -> p.getBlockedTopics().stream().noneMatch(topicsToAvoid::contains))
            .toList();

        if (available.isEmpty()) {
            // Fallback: use any from pool even if used before
            available = pool;
        }

        // Random selection
        return available.get(new Random().nextInt(available.size()));
    }

    private List<String> getTopicsToAvoid(String userId) {
        return profileRepository.findByUserId(userId)
            .map(UserProfileDocument::getTopicsToAvoid)
            .orElse(List.of());
    }

    private int countDailyRefreshes(String userId) {
        List<SessionDocument> todaySessions = sessionRepository
            .findByUserIdAndSessionDate(userId, LocalDate.now());
        return todaySessions.stream().mapToInt(SessionDocument::getRefreshesUsed).sum();
    }

    private String generateTemplateSummary(SessionDocument session) {
        String category = session.getCategoryName();
        String emotion = session.getFinalEmotion() != null ? session.getFinalEmotion().name().toLowerCase() : "reflective";
        int steps = session.getQuestionNumber();

        return String.format(
            "You explored %s through %d prompts and ended feeling %s. " +
            "Take this sense of clarity with you.",
            category.toLowerCase(), steps, emotion
        );
    }

    public static class LimitReachedException extends RuntimeException {
        public LimitReachedException(String message) { super(message); }
    }
}
