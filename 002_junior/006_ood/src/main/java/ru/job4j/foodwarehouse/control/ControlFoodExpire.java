package ru.job4j.foodwarehouse.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.foodwarehouse.checker.GoodsChecker;
import ru.job4j.foodwarehouse.food.AbstractFood;
import ru.job4j.foodwarehouse.handler.HandlerByAge;

/**
 * Controller of food quality.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ControlFoodExpire<E extends AbstractFood> extends AbstractGoodsControl<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ControlFoodExpire.class);

    /**
     * Handler for food of different age.
     */
    private final HandlerByAge<E> hander;

    /**
     * Constructs new instance.
     *
     * @param checker Food checker - to perform checks on food.
     * @param handler Handler for food of different age.
     */
    public ControlFoodExpire(GoodsChecker<E> checker, HandlerByAge<E> handler) {
        super(checker);
        this.hander = handler;
    }

    /**
     * Perform control actions on food,
     *
     * @param food Food to control.
     */
    @Override
    public void doControl(E food) {
        int expired = this.getChecker().check(food, System.currentTimeMillis());
        if (expired < 25) {
            this.hander.handleNew(food);
        } else if (expired < 75) {
            this.hander.handleMiddle(food);
        } else if (expired < 100) {
            this.hander.handleOld(food);
        } else {
            this.hander.handleExpired(food);
        }
    }
}
