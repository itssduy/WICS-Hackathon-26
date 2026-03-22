package com.ireflect.dto;

import java.util.List;

public class CategoryResponse {
    private String slug;
    private String name;
    private String description;
    private boolean isPremium;
    private List<String> samplePrompts;

    public CategoryResponse() {}

    public CategoryResponse(String slug, String name, String description, boolean isPremium, List<String> samplePrompts) {
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.isPremium = isPremium;
        this.samplePrompts = samplePrompts;
    }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean premium) { isPremium = premium; }
    public List<String> getSamplePrompts() { return samplePrompts; }
    public void setSamplePrompts(List<String> samplePrompts) { this.samplePrompts = samplePrompts; }
}
