package ru.job4j.foodwarehouse.food;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract food stored.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public abstract class AbstractFood {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractFood.class);

    /**
     * Food name.
     */
    private final String name;
    /**
     * Creation date in milliseconds.
     */
    private final long createDate;
    /**
     * Expire date in milliseconds.
     */
    private final long expireDate;
    /**
     * Price.
     */
    private int price;
    /**
     * Discount.
     */
    private int discount;

    /**
     * Constructor. Assures we have all needed field values.
     *
     * @param name       Food name.
     * @param createDate Food creation date.
     * @param expireDate Expiration date.
     * @param price      Price.
     * @param discount   Discount size.
     */
    public AbstractFood(String name, long createDate, long expireDate, int price, int discount) {
        this.name = name;
        this.expireDate = expireDate;
        this.createDate = createDate;
        this.price = price;
        this.discount = discount;
    }

    /**
     * Returns name.
     *
     * @return Value of name field.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns expireDate.
     *
     * @return Value of expireDate field.
     */
    public long getExpireDate() {
        return this.expireDate;
    }

    /**
     * Returns createDate.
     *
     * @return Value of createDate field.
     */
    public long getCreateDate() {
        return this.createDate;
    }

    /**
     * Returns price.
     *
     * @return Value of price field.
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Sets food price.
     *
     * @param price Value to set.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returns discount.
     *
     * @return Value of discount field.
     */
    public int getDiscount() {
        return this.discount;
    }

    /**
     * Sets food discount.
     *
     * @param discount Value to set.
     */
    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
