package de.niklas.legendaryindustrialsurvival.item;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Keeps temporary hand state separate from returning bottles to storage or the player's inventory. */
final class DrinkConsumption {
    private DrinkConsumption() {}

    record Result<T>(boolean consumed, T remainder) {}

    static <T> boolean consume(T drink, Supplier<T> readHand, Consumer<T> writeHand,
                              Function<T, Result<T>> use, Runnable stopUsing,
                              Consumer<T> returnRemainder) {
        T original = readHand.get();
        Result<T> result;
        writeHand.accept(drink);
        try {
            result = use.apply(drink);
        } finally {
            try {
                stopUsing.run();
            } finally {
                writeHand.accept(original);
            }
        }
        // The fallback inventory insertion can use the selected slot. Restore it first,
        // otherwise restoring an empty original hand would erase the returned bottle.
        returnRemainder.accept(result.remainder());
        return result.consumed();
    }
}
