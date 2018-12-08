package ru.job4j.theater.storage.repository.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.storage.DatabaseObjectUtils;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.theater.storage.database.DatabaseApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void add(Payment payment) throws SQLException {
        try (Connection connection = this.database.getConnection();
             PreparedStatement addAccount = connection.prepareStatement(this.database.getQuery("sql.query.account.add"));
             PreparedStatement addPayment = connection.prepareStatement(this.database.getQuery("sql.query.payment.add"))
        ) {
            DatabaseObjectUtils.FillStatement.AccountStatements.fillAdd(addAccount, payment.getFrom());
            DatabaseObjectUtils.FillStatement.PaymentStatements.fillAdd(addPayment, payment);
            connection.setAutoCommit(false);
            try {
                addAccount.execute();
                addPayment.execute();
                connection.commit();
            } catch (Throwable e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Get list of all payments in the repository.
     *
     * @return List of payments.
     */
    @Override
    public List<Payment> getAll() throws SQLException {
        List<Payment> list = new ArrayList<>();
        try (Connection connection = this.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.database.getQuery("sql.query.payment.get_all"));
             ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                list.add(DatabaseObjectUtils.FormObject.formPayment(res));
            }
        }
        return list;
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() throws SQLException {
        this.database.clearTables();
    }
}
