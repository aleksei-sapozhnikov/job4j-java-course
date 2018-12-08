package ru.job4j.theater.storage.repository.payment;

import ru.job4j.theater.model.Payment;

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
    void add(Payment payment) throws SQLException;

    /**
     * Get list of all payments in the repository.
     *
     * @return List of payments.
     */
    List<Payment> getAll() throws SQLException;

    /**
     * Clears the repository, removing all objects stored inside.
     */
    void clear() throws SQLException;
}
