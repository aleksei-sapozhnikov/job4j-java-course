package ru.job4j.foodwarehouse.control;

import ru.job4j.foodwarehouse.storage.Storage;

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

    /**
     * Take all elements from from given
     * storage and do control with them.
     *
     * @param storage
     */
    void resort(Storage<E> storage);
}
