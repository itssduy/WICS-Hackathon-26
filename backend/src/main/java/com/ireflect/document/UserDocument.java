package com.ireflect.document;

import com.ireflect.domain.Plan;
import com.ireflect.domain.Role;
import com.ireflect.domain.SubscriptionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true, sparse = true)
    private String email;

    private String passwordHash;

    private Role role = Role.USER;

    @Indexed
    private Plan plan = Plan.FREE;

    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.FREE;

    private Instant createdAt;
    private Instant updatedAt;

    public UserDocument() {}

    public UserDocument(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = Role.USER;
        this.plan = Plan.FREE;
        this.subscriptionStatus = SubscriptionStatus.FREE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

    public SubscriptionStatus getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
