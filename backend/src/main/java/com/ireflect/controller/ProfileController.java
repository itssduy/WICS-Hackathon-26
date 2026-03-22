package com.ireflect.controller;

import com.ireflect.document.UserProfileDocument;
import com.ireflect.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileRepository profileRepository;

    public ProfileController(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return profileRepository.findByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProfile(Authentication auth, @RequestBody Map<String, Object> body) {
        String userId = (String) auth.getPrincipal();

        // Check if profile already exists
        if (profileRepository.findByUserId(userId).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Profile already exists"));
        }

        UserProfileDocument profile = new UserProfileDocument();
        profile.setUserId(userId);
        profile.setDisplayName((String) body.getOrDefault("displayName", ""));
        profile.setAgeOrAgeRange((String) body.getOrDefault("ageOrAgeRange", ""));
        profile.setGender((String) body.getOrDefault("gender", ""));
        profile.setStatusInLife((String) body.getOrDefault("statusInLife", ""));
        profile.setPreferredTone((String) body.getOrDefault("preferredTone", "gentle"));
        profile.setCurrentMood((String) body.getOrDefault("currentMood", "Neutral"));
        profile.setPrimaryInterestCategory((String) body.getOrDefault("primaryInterestCategory", ""));
        
        if (body.containsKey("topicsHelpWith") && body.get("topicsHelpWith") instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            var topics = (java.util.List<String>) body.get("topicsHelpWith");
            profile.setTopicsHelpWith(topics);
        }
        if (body.containsKey("topicsToAvoid") && body.get("topicsToAvoid") instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            var topics = (java.util.List<String>) body.get("topicsToAvoid");
            profile.setTopicsToAvoid(topics);
        }

        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());
        profile = profileRepository.save(profile);

        return ResponseEntity.status(201).body(profile);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(Authentication auth, @RequestBody Map<String, Object> body) {
        String userId = (String) auth.getPrincipal();
        var optProfile = profileRepository.findByUserId(userId);
        if (optProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProfileDocument profile = optProfile.get();

        if (body.containsKey("displayName")) profile.setDisplayName((String) body.get("displayName"));
        if (body.containsKey("ageOrAgeRange")) profile.setAgeOrAgeRange((String) body.get("ageOrAgeRange"));
        if (body.containsKey("gender")) profile.setGender((String) body.get("gender"));
        if (body.containsKey("statusInLife")) profile.setStatusInLife((String) body.get("statusInLife"));
        if (body.containsKey("preferredTone")) profile.setPreferredTone((String) body.get("preferredTone"));
        if (body.containsKey("currentMood")) profile.setCurrentMood((String) body.get("currentMood"));
        if (body.containsKey("primaryInterestCategory")) profile.setPrimaryInterestCategory((String) body.get("primaryInterestCategory"));
        if (body.containsKey("topicsHelpWith") && body.get("topicsHelpWith") instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            var topics = (java.util.List<String>) body.get("topicsHelpWith");
            profile.setTopicsHelpWith(topics);
        }
        if (body.containsKey("topicsToAvoid") && body.get("topicsToAvoid") instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            var topics = (java.util.List<String>) body.get("topicsToAvoid");
            profile.setTopicsToAvoid(topics);
        }

        profile.setUpdatedAt(Instant.now());
        profile = profileRepository.save(profile);

        return ResponseEntity.ok(profile);
    }
}
