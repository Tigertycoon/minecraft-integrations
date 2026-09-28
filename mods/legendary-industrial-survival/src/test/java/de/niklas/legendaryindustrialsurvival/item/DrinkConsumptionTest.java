package de.niklas.legendaryindustrialsurvival.item;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DrinkConsumptionTest {
    private String hand = "";

    @Test void fallbackBottleSurvivesWhenSelectedSlotWasEmpty() {
        boolean drank = DrinkConsumption.consume("water", () -> hand, value -> hand = value,
                held -> new DrinkConsumption.Result<>(true, "bottle"), () -> {},
                remainder -> {
                    assertEquals("", hand, "Restore selected slot before fallback inventory insertion");
                    hand = remainder;
                });
        assertTrue(drank);
        assertEquals("bottle", hand);
    }

    @Test void heldEquipmentIsRestoredBeforeReturningContainer() {
        hand = "sword";
        List<String> storage = new ArrayList<>();
        assertTrue(DrinkConsumption.consume("water", () -> hand, value -> hand = value,
                held -> {
                    assertEquals("water", hand);
                    return new DrinkConsumption.Result<>(true, "bottle");
                }, () -> {}, remainder -> {
                    assertEquals("sword", hand);
                    storage.add(remainder);
                }));
        assertEquals("sword", hand);
        assertEquals(List.of("bottle"), storage);
    }

    @Test void rejectedUseReturnsUnconsumedDrinkExactlyOnce() {
        List<String> storage = new ArrayList<>();
        assertFalse(DrinkConsumption.consume("water", () -> hand, value -> hand = value,
                held -> new DrinkConsumption.Result<>(false, held), () -> {}, storage::add));
        assertEquals(List.of("water"), storage);
        assertEquals("", hand);
    }

    @Test void brokenItemCallbackCannotLeaveTemporaryItemInHand() {
        hand = "sword";
        assertThrows(IllegalStateException.class, () -> DrinkConsumption.consume("water",
                () -> hand, value -> hand = value,
                held -> { throw new IllegalStateException("item callback failed"); }, () -> {},
                remainder -> fail("Unknown consumption result must not be duplicated")));
        assertEquals("sword", hand);
    }
}
