package com.ireflect.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "userProfiles")
public class UserProfileDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String displayName;
    private String ageOrAgeRange;
    private String gender;
    private String statusInLife;
    private String preferredTone;
    private String currentMood;
    private String primaryInterestCategory;
    private List<String> topicsHelpWith = new ArrayList<>();
    private List<String> topicsToAvoid = new ArrayList<>();
    private String optionalContext;
    private Instant createdAt;
    private Instant updatedAt;

    public UserProfileDocument() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAgeOrAgeRange() { return ageOrAgeRange; }
    public void setAgeOrAgeRange(String ageOrAgeRange) { this.ageOrAgeRange = ageOrAgeRange; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getStatusInLife() { return statusInLife; }
    public void setStatusInLife(String statusInLife) { this.statusInLife = statusInLife; }

    public String getPreferredTone() { return preferredTone; }
    public void setPreferredTone(String preferredTone) { this.preferredTone = preferredTone; }

    public String getCurrentMood() { return currentMood; }
    public void setCurrentMood(String currentMood) { this.currentMood = currentMood; }

    public String getPrimaryInterestCategory() { return primaryInterestCategory; }
    public void setPrimaryInterestCategory(String primaryInterestCategory) { this.primaryInterestCategory = primaryInterestCategory; }

    public List<String> getTopicsHelpWith() { return topicsHelpWith; }
    public void setTopicsHelpWith(List<String> topicsHelpWith) { this.topicsHelpWith = topicsHelpWith; }

    public List<String> getTopicsToAvoid() { return topicsToAvoid; }
    public void setTopicsToAvoid(List<String> topicsToAvoid) { this.topicsToAvoid = topicsToAvoid; }

    public String getOptionalContext() { return optionalContext; }
    public void setOptionalContext(String optionalContext) { this.optionalContext = optionalContext; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
