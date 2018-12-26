package ru.job4j.theater.storage.repository.payment;

import ru.job4j.theater.model.Payment;
import ru.job4j.util.database.DbExecutor;

import java.sql.SQLException;
import java.util.List;

/**
 * Repository for Payment objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface PaymentRepository {
    /**
     * Add object to repository.
     *
     * @param payment Object to add.
     */
    void add(Payment payment);

    /**
     * Get list of all payments in the repository.
     *
     * @return List of payments.
     */
    List<Payment> getAll();

    /**
     * Clears the repository, removing all objects stored inside.
     */
    void clear() throws SQLException;

    /**
     * Add object to repository.
     *
     * @param payment  Object to add.
     * @param executor Database executor.
     */
    default void add(Payment payment, DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Get list of all payments in the repository.
     *
     * @param executor Database executor.
     * @return List of payments.
     */
    default List<Payment> getAll(DbExecutor executor) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
