package ru.job4j.theater.storage.repository.seat;

import ru.job4j.theater.model.Seat;
import ru.job4j.util.database.DbExecutor;

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
    void add(Seat seat);

    /**
     * Get list of all seats in the repository.
     *
     * @return List of seats.
     */
    List<Seat> getAll();

    /**
     * Updates seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @param newObj Seat with new values.
     */
    void updateByPlace(int row, int column, Seat newObj);

    /**
     * Returns seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @return Seat with needed row and column or empty Seat object.
     */
    Seat getByPlace(int row, int column);

    /**
     * Clears the repository, removing all objects stored inside.
     */
    void clear() throws SQLException;

    /**
     * Add object to repository.
     * Using given Database executor, no autocommit.
     *
     * @param seat     Object to add.
     * @param executor Database executor.
     */
    default void add(Seat seat, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Get list of all seats in the repository.
     * Using given Database executor, no autocommit.
     *
     * @param executor Database executor.
     * @return List of seats.
     */
    default List<Seat> getAll(DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Updates seat which has given place and column.
     * Using given Database executor, no autocommit.
     *
     * @param row      Row of needed seat.
     * @param column   Column of needed seat.
     * @param newObj   Seat with new values.
     * @param executor Database executor.
     */
    default void updateByPlace(int row, int column, Seat newObj, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns seat which has given place and column.
     * Using given Database executor, no autocommit.
     *
     * @param row      Row of needed seat.
     * @param column   Column of needed seat.
     * @param executor Database executor.
     * @return Seat with needed row and column or empty Seat object.
     */
    default Seat getByPlace(int row, int column, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
