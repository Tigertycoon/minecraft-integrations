package de.niklas.legendaryindustrialsurvival.item;

/** Chooses the largest drink that fits, otherwise the drink with the least overflow. */
final class DrinkSelection {
    private DrinkSelection() {}

    static boolean isBetter(int candidate, int selected, int missing) {
        if (candidate <= 0) return false;
        if (selected <= 0) return true;
        boolean candidateFits = candidate <= missing;
        boolean selectedFits = selected <= missing;
        if (candidateFits != selectedFits) return candidateFits;
        return candidateFits ? candidate > selected : candidate < selected;
    }
}
