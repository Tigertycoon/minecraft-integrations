package de.niklas.legendaryarcanesurvival.service;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SurvivalCooldownsTest {
    private final SurvivalCooldowns cooldowns = new SurvivalCooldowns();
    private final UUID player = UUID.randomUUID();

    @Test void duplicateEventsDoNotKeepExtendingCooldown() {
        assertTrue(cooldowns.allowCast(player, "fire", 100, 2));
        assertFalse(cooldowns.allowCast(player, "fire", 101, 2));
        assertTrue(cooldowns.allowCast(player, "fire", 102, 2));
    }

    @Test void interleavedSpellsCannotBypassDuplicateProtection() {
        assertTrue(cooldowns.allowCast(player, "fire", 100, 2));
        assertTrue(cooldowns.allowCast(player, "ice", 100, 2));
        assertFalse(cooldowns.allowCast(player, "fire", 100, 2));
        assertTrue(cooldowns.allowCast(UUID.randomUUID(), "fire", 100, 2));
    }

    @Test void targetEffectsShareCooldownAcrossSchools() {
        assertTrue(cooldowns.allowTarget(player, 100, 10));
        assertFalse(cooldowns.allowTarget(player, 109, 10));
        assertTrue(cooldowns.allowTarget(player, 110, 10));
    }

    @Test void newWorldWithEarlierClockDoesNotBlockEffects() {
        cooldowns.allowTarget(player, 1_000_000, 10);
        cooldowns.allowCast(player, "fire", 1_000_000, 2);
        assertTrue(cooldowns.allowTarget(player, 1, 10));
        assertTrue(cooldowns.allowCast(player, "fire", 1, 2));
    }

    @Test void logoutRemovesOnlyThatPlayersState() {
        UUID other = UUID.randomUUID();
        cooldowns.allowCast(player, "fire", 100, 2);
        cooldowns.allowTarget(player, 100, 10);
        cooldowns.allowTarget(other, 100, 10);
        cooldowns.forget(player);
        assertTrue(cooldowns.allowCast(player, "fire", 100, 2));
        assertTrue(cooldowns.allowTarget(player, 100, 10));
        assertFalse(cooldowns.allowTarget(other, 100, 10));
    }

    @Test void serverShutdownClearsBothCaches() {
        cooldowns.allowCast(player, "fire", 100, 2);
        cooldowns.allowTarget(player, 100, 10);
        cooldowns.clear();
        assertTrue(cooldowns.allowCast(player, "fire", 100, 2));
        assertTrue(cooldowns.allowTarget(player, 100, 10));
    }

    @Test void zeroIntervalDisablesSuppression() {
        for (int i = 0; i < 3; i++) {
            assertTrue(cooldowns.allowCast(player, "fire", 100, 0));
            assertTrue(cooldowns.allowTarget(player, 100, 0));
        }
    }
}
