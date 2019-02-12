package ru.job4j.foodwarehouse.checker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.foodwarehouse.food.AbstractFood;

/**
 * Checks food quality.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FoodExpireChecker<E extends AbstractFood> implements GoodsChecker<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(FoodExpireChecker.class);

    /**
     * Calculates food expiration percent for given time:
     * <p>
     * value < 0 is when food was not created yet,
     * 0 is when food is just created,
     * 10 is when 10% of time from creation to expire has passed,
     * 100 is when current time is right an expiration time
     * value > 100 is when expiration date is before current date.
     *
     * @param food Food to check.
     * @param time Current time.
     * @return Expiration percent.
     */
    @Override
    public int check(E food, long time) {
        var create = food.getCreateDate();
        var expire = food.getExpireDate();
        var passed = (double) (expire - time);
        var maxPassed = (double) (expire - create);
        var expireAmount = 1 - passed / maxPassed;
        return (int) (100 * expireAmount);
    }
}

