package de.niklas.legendaryindustrialsurvival.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DrinkSelectionTest {
    private int select(int missing, int... drinks) {
        int selected = -1;
        for (int value : drinks) {
            if (DrinkSelection.isBetter(value, selected, missing)) selected = value;
        }
        return selected;
    }

    @Test void choosesLargestDrinkThatFits() { assertEquals(6, select(7, 2, 8, 4, 6, 10)); }
    @Test void exactFitWinsRegardlessOfOrder() {
        assertEquals(7, select(7, 9, 7, 2));
        assertEquals(7, select(7, 2, 7, 9));
    }
    @Test void whenAllOverflowChoosesLeastWaste() { assertEquals(8, select(7, 12, 8, 10)); }
    @Test void largeCustomDrinksRemainUsable() { assertEquals(120, select(7, 150, 120)); }
    @Test void skipsNonDrinksAndHarmfulValues() { assertEquals(-1, select(7, 0, -3)); }
}
