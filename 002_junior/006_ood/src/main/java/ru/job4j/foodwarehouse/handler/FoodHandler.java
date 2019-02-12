package ru.job4j.foodwarehouse.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.foodwarehouse.food.AbstractFood;
import ru.job4j.foodwarehouse.storage.Storage;

/**
 * Handler for different-age food.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FoodHandler<E extends AbstractFood> implements HandlerByAge<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(FoodHandler.class);
    /**
     * Warehouse - for new objects.
     */
    private final Storage<E> warehouse;
    /**
     * Shop - for middle-age and old objects.
     */
    private final Storage<E> shop;
    /**
     * Trash - for expired objects.
     */
    private final Storage<E> trash;
    /**
     * Discount value to set on old objects.
     */
    private final int discount;

    /**
     * Creates new instance.
     *
     * @param warehouse Warehouse - for new objects.
     * @param shop      Shop - for middle-age and old objects.
     * @param trash     Trash - for expired objects.
     * @param discount  Discount value to set on old objects.
     */
    public FoodHandler(Storage<E> warehouse, Storage<E> shop, Storage<E> trash, int discount) {
        this.warehouse = warehouse;
        this.shop = shop;
        this.trash = trash;
        this.discount = discount;
    }

    /**
     * Handle new object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleNew(E obj) {
        this.warehouse.add(obj);
    }

    /**
     * Handle middle-age object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleMiddle(E obj) {
        this.shop.add(obj);
    }

    /**
     * Handle old object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleOld(E obj) {
        obj.setDiscount(this.discount);
        this.shop.add(obj);
    }

    /**
     * Handle expired object.
     *
     * @param obj Object to handle.
     */
    @Override
    public void handleExpired(E obj) {
        this.trash.add(obj);
    }
}
