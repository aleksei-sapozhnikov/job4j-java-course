package ru.job4j.foodwarehouse.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.foodwarehouse.food.CanReproduceFood;
import ru.job4j.foodwarehouse.storage.Storage;

/**
 * Handler for reproducible food.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class CanReproduceFoodHandler<E extends CanReproduceFood> implements HandlerByAge<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CanReproduceFoodHandler.class);

    /**
     * Default basic handler.
     */
    private final HandlerByAge<E> handler;

    /**
     * Refrigerator for reproducible objects.
     */
    private final Storage<E> refrigerator;

    /**
     * Constructs new instance.
     *
     * @param handler      Default basic handler.
     * @param refrigerator Refrigerator for reproducible objects.
     */
    public CanReproduceFoodHandler(HandlerByAge<E> handler, Storage<E> refrigerator) {
        this.handler = handler;
        this.refrigerator = refrigerator;
    }

    /**
     * Handle new object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleNew(E obj) {
        this.handler.handleNew(obj);
    }

    /**
     * Handle middle-age object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleMiddle(E obj) {
        this.handler.handleMiddle(obj);
    }

    /**
     * Handle old object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleOld(E obj) {
        this.handler.handleOld(obj);
    }

    /**
     * Handle expired object.
     * If object is reproducible, moves it to refrigerator,
     * otherwise does default handler action.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleExpired(E obj) {
        if (obj.isCanReproduce()) {
            this.refrigerator.add(obj);
        } else {
            this.handler.handleExpired(obj);
        }
    }
}
