package com.example.demo.model;

import java.util.List;

public class LandingPageInfo {

    private String appName;
    private String headline;
    private String description;
    private List<String> features;
    private String signInText;
    private String signUpText;

    public LandingPageInfo() {}

    public LandingPageInfo(String appName, String headline, String description,
                           List<String> features, String signInText, String signUpText) {
        this.appName = appName;
        this.headline = headline;
        this.description = description;
        this.features = features;
        this.signInText = signInText;
        this.signUpText = signUpText;
    }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    public String getSignInText() { return signInText; }
    public void setSignInText(String signInText) { this.signInText = signInText; }

    public String getSignUpText() { return signUpText; }
    public void setSignUpText(String signUpText) { this.signUpText = signUpText; }
}