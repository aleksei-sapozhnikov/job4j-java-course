package ru.job4j.theater.storage.repository.seat;

import ru.job4j.theater.model.Seat;

import java.sql.SQLException;
import java.util.List;

/**
 * Repository for Seat objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface SeatRepository {
    /**
     * Add object to repository.
     *
     * @param seat Object to add.
     */
    void add(Seat seat) throws SQLException;

    /**
     * Get list of all seats in the repository.
     *
     * @return List of seats.
     */
    List<Seat> getAll() throws SQLException;

    /**
     * Updates seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @param newObj Seat with new values.
     */
    void updateByPlace(int row, int column, Seat newObj) throws SQLException;

    /**
     * Returns seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @return Seat with needed row and column or empty Seat object.
     */
    Seat getByPlace(int row, int column) throws SQLException;

    /**
     * Clears the repository, removing all objects stored inside.
     */
    void clear() throws SQLException;
}
