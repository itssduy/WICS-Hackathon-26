package com.ireflect.dto;

public class AuthResponse {
    private String userId;
    private String username;
    private String plan;
    private String subscriptionStatus;
    private String token;

    public AuthResponse() {}

    public AuthResponse(String userId, String username, String plan, String subscriptionStatus, String token) {
        this.userId = userId;
        this.username = username;
        this.plan = plan;
        this.subscriptionStatus = subscriptionStatus;
        this.token = token;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
