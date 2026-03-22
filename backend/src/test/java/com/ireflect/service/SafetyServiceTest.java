package com.ireflect.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SafetyServiceTest {

    private final SafetyService safetyService = new SafetyService();

    @Test
    void containsDangerLanguage_detectsKnownPhrases() {
        assertTrue(safetyService.containsDangerLanguage("Sometimes I feel like I want to die."));
        assertTrue(safetyService.containsDangerLanguage("I might hurt myself tonight"));
        assertTrue(safetyService.containsDangerLanguage("I can't go on like this"));
    }

    @Test
    void containsDangerLanguage_ignoresNeutralText() {
        assertFalse(safetyService.containsDangerLanguage(null));
        assertFalse(safetyService.containsDangerLanguage(""));
        assertFalse(safetyService.containsDangerLanguage("I feel overwhelmed but want to keep trying."));
    }

    @Test
    void crisisResources_returnsExpectedEntries() {
        var resources = safetyService.getCrisisResources();
        assertEquals(3, resources.size());
        assertEquals("988", resources.get(0).contact());
        assertEquals("Emergency Services", resources.get(2).name());
    }
}
