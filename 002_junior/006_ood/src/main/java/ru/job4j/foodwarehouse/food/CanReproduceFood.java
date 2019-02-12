package ru.job4j.foodwarehouse.food;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Food that can be reproduced.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class CanReproduceFood extends AbstractFood {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CanReproduceFood.class);

    /**
     * Basic food object.
     */
    private final AbstractFood food;

    /**
     * Field showing that this food can be reproduced.
     */
    private boolean canReproduce;

    /**
     * Constructs new instance.
     *
     * @param food         Basic food.
     * @param canReproduce Flag showing if food can be reproduced or not.
     */
    public CanReproduceFood(AbstractFood food, boolean canReproduce) {
        super(food.getName(), food.getCreateDate(), food.getExpireDate(), food.getPrice(), food.getDiscount());
        this.food = food;
        this.canReproduce = canReproduce;
    }

    /**
     * Returns canReproduce.
     *
     * @return Value of canReproduce field.
     */
    public boolean isCanReproduce() {
        return this.canReproduce;
    }

    /**
     * Sets canReproduce value.
     *
     * @param value Value to set.
     */
    public void setCanReproduce(boolean value) {
        this.canReproduce = value;
    }
}
