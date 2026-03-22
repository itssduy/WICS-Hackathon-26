package com.ireflect.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SafetyService {

    // Crisis/danger keywords — triggers safety interruption
    private static final List<String> DANGER_PHRASES = List.of(
        "kill myself", "want to die", "end my life", "suicide",
        "self harm", "self-harm", "cut myself", "hurt myself",
        "no reason to live", "better off dead", "don't want to be here",
        "can't go on", "going to end it"
    );

    private static final Pattern DANGER_PATTERN;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DANGER_PHRASES.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(Pattern.quote(DANGER_PHRASES.get(i)));
        }
        DANGER_PATTERN = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
    }

    /**
     * Scans text for danger-language indicators.
     * @return true if text contains crisis language
     */
    public boolean containsDangerLanguage(String text) {
        if (text == null || text.isBlank()) return false;
        return DANGER_PATTERN.matcher(text).find();
    }

    /**
     * Returns the supportive message shown during safety interruption.
     */
    public String getSupportiveMessage() {
        return "We noticed something in your words that we want to take seriously. " +
               "Your feelings are valid, and you don't have to carry this alone. " +
               "Please reach out to someone who can help.";
    }

    /**
     * Returns crisis resource info.
     */
    public List<CrisisResource> getCrisisResources() {
        return List.of(
            new CrisisResource("National Suicide Prevention Lifeline", "988", "call"),
            new CrisisResource("Crisis Text Line", "Text HOME to 741741", "text"),
            new CrisisResource("Emergency Services", "911", "call")
        );
    }

    public record CrisisResource(String name, String contact, String type) {}
}
