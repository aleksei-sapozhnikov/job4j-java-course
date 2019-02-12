package ru.job4j.foodwarehouse.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.foodwarehouse.checker.GoodsChecker;

/**
 * Abstract goods control class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public abstract class AbstractGoodsControl<E> implements GoodsControl<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractGoodsControl.class);

    /**
     * Checker - checks goods.
     */
    private final GoodsChecker<E> checker;

    /**
     * Constructor. To assure we have checker.
     *
     * @param checker Goods checker.
     */
    public AbstractGoodsControl(GoodsChecker<E> checker) {
        this.checker = checker;
    }

    /**
     * Returns current checker. For sub-classes.
     *
     * @return Value of checker field.
     */
    protected GoodsChecker<E> getChecker() {
        return this.checker;
    }
}
