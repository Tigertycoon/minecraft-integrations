package de.niklas.legendaryarcanesurvival.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Server-thread cooldown state. Rejected events do not extend an accepted event's cooldown. */
final class SurvivalCooldowns {
    private final Map<UUID, Map<String, Long>> casts = new HashMap<>();
    private final Map<UUID, Long> targets = new HashMap<>();

    boolean allowCast(UUID player, String spell, long now, int interval) {
        Map<String, Long> spells = casts.computeIfAbsent(player, ignored -> new HashMap<>());
        if (isCoolingDown(spells.get(spell), now, interval)) {
            return false;
        }
        spells.put(spell, now);
        return true;
    }

    boolean allowTarget(UUID player, long now, int interval) {
        if (isCoolingDown(targets.get(player), now, interval)) {
            return false;
        }
        targets.put(player, now);
        return true;
    }

    void forget(UUID player) {
        casts.remove(player);
        targets.remove(player);
    }

    void clear() {
        casts.clear();
        targets.clear();
    }

    private static boolean isCoolingDown(Long previous, long now, int interval) {
        // A new world's clock may precede the previous world's clock.
        return previous != null && now >= previous && now - previous < interval;
    }
}
