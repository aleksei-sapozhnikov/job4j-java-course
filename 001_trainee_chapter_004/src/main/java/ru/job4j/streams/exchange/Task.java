package ru.job4j.streams.exchange;

/**
 * Task for exchange: add/delete, buy/sell, how many, what price, etc.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.03.2018
 */
class Task {
    /**
     * Unique id of the item.
     */
    private String id;
    /**
     * Add or delete this item.
     */
    private ActionEnum action;
    /**
     * Type of the operation: buy shares or sell shares.
     */
    private OperationEnum operation;
    /**
     * Share issuer.
     */
    private String issuer;
    /**
     * Price to buy/sell for.
     */
    private int price;
    /**
     * How many shares to buy/sell.
     */
    private int volume;

    /**
     * Constructs a new item with all fields initialized.
     *
     * @param id        unique item id.
     * @param action    add or delete this item.
     * @param operation buy (ask) or sell (bid) shares.
     * @param issuer    shares issuer (company).
     * @param price     price to buy/sell for.
     * @param volume    amount of shares to buy/sell.
     */
    Task(String id, ActionEnum action, OperationEnum operation, String issuer, int price, int volume) {
        this.id = id;
        this.action = action;
        this.operation = operation;
        this.issuer = issuer;
        this.price = price;
        this.volume = volume;
    }

    /**
     * Get id.
     *
     * @return id field value.
     */
    public String id() {
        return this.id;
    }

    /**
     * Get action: add or delete task.
     *
     * @return action field value.
     */
    ActionEnum action() {
        return action;
    }

    /**
     * Get operation: buy (ask) or sell (bid) shares.
     *
     * @return operation field value.
     */
    OperationEnum operation() {
        return this.operation;
    }

    /**
     * Get shares issuer.
     *
     * @return issuer field value.
     */
    String issuer() {
        return this.issuer;
    }

    /**
     * Get volume.
     *
     * @return volume field value.
     */
    public int volume() {
        return this.volume;
    }

    /**
     * Get price.
     *
     * @return price field value.
     */
    public int price() {
        return price;
    }

    /**
     * Subtracts given volume from the task's volume.
     *
     * @param volume volume to subtract.
     */
    void subtractVolume(int volume) {
        this.volume -= volume;
    }
}