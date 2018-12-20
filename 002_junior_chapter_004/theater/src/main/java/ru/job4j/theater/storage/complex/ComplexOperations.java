package ru.job4j.theater.storage.complex;

import ru.job4j.util.database.DbExecutor;

import java.sql.SQLException;

/**
 * Describes complex operations with many objects which can be made in storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface ComplexOperations {
    /**
     * Operation with commit to database.
     * Adds buyer to storage if not present, sets seat occupied by buyer, inserts payment for seat.
     *
     * @param row    Seat row.
     * @param column Seat column.
     * @param name   Buyer name.
     * @param phone  Buyer phone.
     */
    boolean buySeat(int row, int column, String name, String phone) throws SQLException;

    /**
     * Operation without commit to database, using given executor.
     * Adds buyer to storage if not present, sets seat occupied by buyer, inserts payment for seat.
     *
     * @param row    Seat row.
     * @param column Seat column.
     * @param name   Buyer name.
     * @param phone  Buyer phone.
     */
    default boolean buySeat(int row, int column, String name, String phone, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
