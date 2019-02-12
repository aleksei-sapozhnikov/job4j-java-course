package ru.job4j.foodwarehouse.checker;

/**
 * Goods checker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface GoodsChecker<E> {
    /**
     * Performs check and returns its result
     * as an integer value.
     *
     * @param obj  Object to check.
     * @param time Time to treat as "current".
     * @return Check result.
     */
    int check(E obj, long time);
}
