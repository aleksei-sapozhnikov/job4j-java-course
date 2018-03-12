package ru.job4j.orderbook;

import java.math.BigDecimal;
import java.util.Objects;

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
    private BigDecimal price;
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
    Task(String id, ActionEnum action, OperationEnum operation, String issuer, BigDecimal price, int volume) {
        this.id = id;
        this.action = action;
        this.operation = operation;
        this.issuer = issuer;
        this.price = price;
        this.volume = volume;
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
    public OperationEnum operation() {
        return this.operation;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Task task = (Task) other;
        return Objects.equals(this.id, task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Enum: add or delete this item.
     */
    enum ActionEnum {
        ADD, DELETE
    }

    /**
     * Enum: action of the operation: buy shares or sell shares.
     */
    enum OperationEnum {
        ASK, BID
    }

}