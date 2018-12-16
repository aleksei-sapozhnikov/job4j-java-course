package ru.job4j.theater.storage.repository.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.storage.ObjectForm;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;
import ru.job4j.theater.storage.repository.account.AccountRepository;
import ru.job4j.theater.storage.repository.account.AccountRepositoryDatabase;
import ru.job4j.util.database.DbExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Payment repository based on database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class PaymentRepositoryDatabase implements PaymentRepository {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(PaymentRepositoryDatabase.class);
    /**
     * Singleton instance.
     */
    private static final PaymentRepository INSTANCE = new PaymentRepositoryDatabase();
    /**
     * Database API instance.
     */
    private final DatabaseApi database = Database.getInstance();
    /**
     * Account repository
     */
    private final AccountRepository accountRepository = AccountRepositoryDatabase.getInstance();

    /**
     * Constructor.
     */
    private PaymentRepositoryDatabase() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static PaymentRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Add object to repository.
     *
     * @param payment Object to add.
     */
    @Override
    public void add(Payment payment) {
        try (DbExecutor executor = this.database.getExecutor()) {
            this.add(payment, executor);
            executor.commit();
        }
    }

    /**
     * Add object to repository.
     * Using given executor - no autocommit.
     *
     * @param payment Object to add.
     */
    @Override
    public void add(Payment payment, DbExecutor executor) {
        Account from = payment.getFrom();
        this.accountRepository.add(from, executor);
        executor.execute(
                this.database.getQuery("sql.query.payment.add"),
                List.of(payment.getAmount(), from.getName(), from.getPhone(), payment.getComment()),
                PreparedStatement::execute);
    }

    /**
     * Get list of all payments in the repository.
     *
     * @return List of payments.
     */
    @Override
    public List<Payment> getAll() {
        try (DbExecutor executor = this.database.getExecutor()) {
            return this.getAll(executor);
        }
    }

    /**
     * Get list of all payments in the repository.
     * * Using given executor - no autocommit.
     *
     * @return List of payments.
     */
    @Override
    public List<Payment> getAll(DbExecutor executor) {
        var values = executor.executeQuery(
                this.database.getQuery("sql.query.payment.get_all"),
                List.of(String.class, String.class, Integer.class, String.class)
        ).orElse(new ArrayList<>());
        return values.stream()
                .map(ObjectForm::formPayment)
                .collect(Collectors.toList());
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() throws SQLException {
        this.database.clearTables();
    }
}
