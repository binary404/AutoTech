package binary404.autotech.common.core.util;

import java.util.Arrays;
import java.util.function.Predicate;

public class Predicates {

    /**
     * Create an inverse predicate from another predicate. Makes it less ugly to use method references.
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    /**
     * Reduce multiple OR predicates into a single predicate.
     */
    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        return Arrays.stream(predicates)
                .reduce(x -> false, Predicate::or);
    }

    /**
     * Reduce multiple AND predicates into a single predicate.
     */
    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return Arrays.stream(predicates)
                .reduce(x -> true, Predicate::and);
    }

}
