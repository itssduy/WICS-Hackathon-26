package com.ireflect.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "categories")
public class CategoryDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    private String name;
    private String description;

    @Indexed
    private boolean isPremium;

    @Indexed
    private boolean isActive = true;

    private PromptStages promptStages = new PromptStages();

    public CategoryDocument() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean premium) { isPremium = premium; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public PromptStages getPromptStages() { return promptStages; }
    public void setPromptStages(PromptStages promptStages) { this.promptStages = promptStages; }

    public static class PromptStages {
        private List<PromptItem> opening = new ArrayList<>();
        private List<PromptItem> deepening = new ArrayList<>();
        private List<PromptItem> reframing = new ArrayList<>();
        private List<PromptItem> release = new ArrayList<>();
        private List<PromptItem> closure = new ArrayList<>();

        public List<PromptItem> getOpening() { return opening; }
        public void setOpening(List<PromptItem> opening) { this.opening = opening; }

        public List<PromptItem> getDeepening() { return deepening; }
        public void setDeepening(List<PromptItem> deepening) { this.deepening = deepening; }

        public List<PromptItem> getReframing() { return reframing; }
        public void setReframing(List<PromptItem> reframing) { this.reframing = reframing; }

        public List<PromptItem> getRelease() { return release; }
        public void setRelease(List<PromptItem> release) { this.release = release; }

        public List<PromptItem> getClosure() { return closure; }
        public void setClosure(List<PromptItem> closure) { this.closure = closure; }
    }

    public static class PromptItem {
        private String id;
        private String text;
        private String tone;
        private List<String> sensitivityTags = new ArrayList<>();
        private List<String> blockedTopics = new ArrayList<>();

        public PromptItem() {}

        public PromptItem(String id, String text, String tone) {
            this.id = id;
            this.text = text;
            this.tone = tone;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getTone() { return tone; }
        public void setTone(String tone) { this.tone = tone; }

        public List<String> getSensitivityTags() { return sensitivityTags; }
        public void setSensitivityTags(List<String> sensitivityTags) { this.sensitivityTags = sensitivityTags; }

        public List<String> getBlockedTopics() { return blockedTopics; }
        public void setBlockedTopics(List<String> blockedTopics) { this.blockedTopics = blockedTopics; }
    }
}
