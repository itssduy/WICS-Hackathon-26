package com.ireflect.document;

import com.ireflect.domain.FinalEmotion;
import com.ireflect.domain.SessionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "sessions")
@CompoundIndex(name = "user_date_idx", def = "{'userId': 1, 'sessionDate': 1}")
public class SessionDocument {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String categorySlug;
    private String categoryName;

    private SessionStatus status = SessionStatus.IN_PROGRESS;

    private int questionNumber = 1;
    private int questionTotalHint = 5;

    private List<PromptRecord> promptHistory = new ArrayList<>();
    private List<ResponseRecord> responseHistory = new ArrayList<>();
    private List<String> usedPromptIds = new ArrayList<>();

    // Closure
    private FinalEmotion finalEmotion;
    private String finalCheckin;
    private String takeaway;
    private String summaryText;

    // Tracking
    private LocalDate sessionDate;
    private int refreshesUsed = 0;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant endedAt;

    public SessionDocument() {}

    public SessionDocument(String userId, String categorySlug, String categoryName) {
        this.userId = userId;
        this.categorySlug = categorySlug;
        this.categoryName = categoryName;
        this.status = SessionStatus.IN_PROGRESS;
        this.questionNumber = 1;
        this.sessionDate = LocalDate.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Nested types
    public static class PromptRecord {
        private int stepNumber;
        private String promptStage;
        private String promptId;
        private String promptText;
        private Instant shownAt;

        public PromptRecord() {}
        public PromptRecord(int stepNumber, String promptStage, String promptId, String promptText) {
            this.stepNumber = stepNumber;
            this.promptStage = promptStage;
            this.promptId = promptId;
            this.promptText = promptText;
            this.shownAt = Instant.now();
        }

        public int getStepNumber() { return stepNumber; }
        public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }
        public String getPromptStage() { return promptStage; }
        public void setPromptStage(String promptStage) { this.promptStage = promptStage; }
        public String getPromptId() { return promptId; }
        public void setPromptId(String promptId) { this.promptId = promptId; }
        public String getPromptText() { return promptText; }
        public void setPromptText(String promptText) { this.promptText = promptText; }
        public Instant getShownAt() { return shownAt; }
        public void setShownAt(Instant shownAt) { this.shownAt = shownAt; }
    }

    public static class ResponseRecord {
        private int stepNumber;
        private String responseType;
        private String responseText;
        private Instant respondedAt;

        public ResponseRecord() {}
        public ResponseRecord(int stepNumber, String responseType, String responseText) {
            this.stepNumber = stepNumber;
            this.responseType = responseType;
            this.responseText = responseText;
            this.respondedAt = Instant.now();
        }

        public int getStepNumber() { return stepNumber; }
        public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }
        public String getResponseType() { return responseType; }
        public void setResponseType(String responseType) { this.responseType = responseType; }
        public String getResponseText() { return responseText; }
        public void setResponseText(String responseText) { this.responseText = responseText; }
        public Instant getRespondedAt() { return respondedAt; }
        public void setRespondedAt(Instant respondedAt) { this.respondedAt = respondedAt; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCategorySlug() { return categorySlug; }
    public void setCategorySlug(String categorySlug) { this.categorySlug = categorySlug; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    public int getQuestionNumber() { return questionNumber; }
    public void setQuestionNumber(int questionNumber) { this.questionNumber = questionNumber; }
    public int getQuestionTotalHint() { return questionTotalHint; }
    public void setQuestionTotalHint(int questionTotalHint) { this.questionTotalHint = questionTotalHint; }
    public List<PromptRecord> getPromptHistory() { return promptHistory; }
    public void setPromptHistory(List<PromptRecord> promptHistory) { this.promptHistory = promptHistory; }
    public List<ResponseRecord> getResponseHistory() { return responseHistory; }
    public void setResponseHistory(List<ResponseRecord> responseHistory) { this.responseHistory = responseHistory; }
    public List<String> getUsedPromptIds() { return usedPromptIds; }
    public void setUsedPromptIds(List<String> usedPromptIds) { this.usedPromptIds = usedPromptIds; }
    public FinalEmotion getFinalEmotion() { return finalEmotion; }
    public void setFinalEmotion(FinalEmotion finalEmotion) { this.finalEmotion = finalEmotion; }
    public String getFinalCheckin() { return finalCheckin; }
    public void setFinalCheckin(String finalCheckin) { this.finalCheckin = finalCheckin; }
    public String getTakeaway() { return takeaway; }
    public void setTakeaway(String takeaway) { this.takeaway = takeaway; }
    public String getSummaryText() { return summaryText; }
    public void setSummaryText(String summaryText) { this.summaryText = summaryText; }
    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
    public int getRefreshesUsed() { return refreshesUsed; }
    public void setRefreshesUsed(int refreshesUsed) { this.refreshesUsed = refreshesUsed; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getEndedAt() { return endedAt; }
    public void setEndedAt(Instant endedAt) { this.endedAt = endedAt; }
}
