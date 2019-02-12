package ru.job4j.foodwarehouse.control;

/**
 * Goods control.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface GoodsControl<E> {
    /**
     * Performs control actions.
     *
     * @param obj Object to control.
     */
    void doControl(E obj);
}
