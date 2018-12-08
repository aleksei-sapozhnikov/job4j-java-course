package ru.job4j.theater.storage.complex;

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
     * Adds buyer to storage if not present, sets seat occupied by buyer, inserts payment for seat.
     *
     * @param row    Seat row.
     * @param column Seat column.
     * @param name   Buyer name.
     * @param phone  Buyer phone.
     */
    boolean buySeat(int row, int column, String name, String phone) throws SQLException;
}
