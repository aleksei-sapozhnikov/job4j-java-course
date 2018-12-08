package ru.job4j.theater.storage.repository.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Payment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple payment repository based on Set.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class PaymentRepositoryHashSet implements PaymentRepository {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(PaymentRepositoryHashSet.class);

    private Set<Payment> store = new HashSet<>();

    /**
     * Singleton instance.
     */
    private static final PaymentRepository INSTANCE = new PaymentRepositoryHashSet();

    /**
     * Constructor.
     */
    private PaymentRepositoryHashSet() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static PaymentRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Payment payment) {
        this.store.add(payment);
    }

    @Override
    public List<Payment> getAll() {
        return new ArrayList<>(this.store);
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() {
        this.store.clear();
    }
}
