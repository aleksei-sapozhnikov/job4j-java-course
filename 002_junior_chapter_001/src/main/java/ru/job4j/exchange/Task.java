package ru.job4j.exchange;

public class Task {
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

    public int volume() {
        return this.volume;
    }

    public int price() {
        return price;
    }

    void subtractVolume(int volume) {
        this.volume -= volume;
    }

    /**
     * Checks equality of this object to another object.
     *
     * @param other another object.
     * @return <tt>true</tt> if this and another objects are equal, <tt>false otherwise</tt>
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Task task = (Task) other;
        return this.id.equals(task.id);
    }

    /**
     * Returns integer hashcode of the object fields.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}