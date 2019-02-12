package ru.job4j.foodwarehouse.handler;

/**
 * Handler for objects of different age.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface HandlerByAge<E> {
    /**
     * Handle new object.
     *
     * @param obj Object to handle.
     */
    void handleNew(E obj);

    /**
     * Handle middle-age object.
     *
     * @param obj Object to handle.
     */
    void handleMiddle(E obj);

    /**
     * Handle old object.
     *
     * @param obj Object to handle.
     */
    void handleOld(E obj);

    /**
     * Handle expired object.
     *
     * @param obj Object to handle.
     */
    void handleExpired(E obj);
}
