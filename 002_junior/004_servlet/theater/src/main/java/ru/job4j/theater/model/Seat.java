package ru.job4j.theater.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Seat model.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Seat implements Comparable<Seat> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Seat.class);

    private static final Seat EMPTY_SEAT = new Seat.Builder(-1, -1, -1).build();

    /**
     * Seat place: row.
     */
    private final int row;
    /**
     * Seat place: column.
     */
    private final int column;
    /**
     * Seat price.
     */
    private final int price;
    /**
     * Seat owner.
     */
    private final Account owner;

    /**
     * Initializes fields in the new objects.
     *
     * @param builder Builder object to gain values from.
     */
    private Seat(Builder builder) {
        this.row = builder.row;
        this.column = builder.column;
        this.price = builder.price;
        this.owner = builder.owner;
    }

    /**
     * Returns EMPTY_SEAT.
     *
     * @return Value of EMPTY_SEAT field.
     */
    public static Seat getEmptySeat() {
        return EMPTY_SEAT;
    }

    /**
     * Returns row.
     *
     * @return Value of row field.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns column.
     *
     * @return Value of column field.
     */
    public int getColumn() {
        return this.column;
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
     * Returns owner.
     *
     * @return Value of owner field.
     */
    public Account getOwner() {
        return this.owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "Seat[row='%s',column='%s',price='%s',owner='%s']",
                this.row, this.column, this.price, this.owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return row == seat.row
                && column == seat.column
                && price == seat.price
                && Objects.equals(owner, seat.owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column, price, owner);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Seat other) {
        int byRow = Integer.compare(this.getRow(), other.getRow());
        return byRow != 0
                ? byRow
                : Integer.compare(this.getColumn(), other.getColumn());
    }

    /**
     * Returns if the seat is free or not.
     *
     * @return true/false if the seat is free or not.
     */
    public boolean isFree() {
        return this.owner == Account.getEmptyAccount();
    }

    /**
     * Sets this seat occupied by given account.
     *
     * @param account Account occupying the seat.
     * @return Occupied seat object.
     */
    public Seat occupy(Account account) {
        return new Builder(this.row, this.column, this.price)
                .owner(account)
                .build();
    }

    /**
     * Builder class to construct Seat objects.
     */
    public static class Builder {
        private final int row;
        private final int column;
        private final int price;
        private Account owner = Account.getEmptyAccount();

        /**
         * Constructor. Takes must-have parameters.
         *
         * @param row    Seat row.
         * @param column Seat column.
         * @param price  Price for the seat.
         */
        public Builder(int row, int column, int price) {
            this.row = row;
            this.column = column;
            this.price = price;
        }

        /**
         * Sets value of the field to given one.
         *
         * @param val Value to set.
         * @return The Builder object.
         */
        public Builder owner(Account val) {
            this.owner = val;
            return this;
        }

        /**
         * Returns Seat object with parameters taken from this builder.
         *
         * @return New Seat object.
         */
        public Seat build() {
            return new Seat(this);
        }
    }
}