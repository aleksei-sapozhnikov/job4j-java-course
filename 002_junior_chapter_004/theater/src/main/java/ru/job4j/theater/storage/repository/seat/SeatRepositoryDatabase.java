package ru.job4j.theater.storage.repository.seat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Seat;
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
 * Seat repository based on database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SeatRepositoryDatabase implements SeatRepository {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SeatRepositoryDatabase.class);
    /**
     * Singleton instance.
     */
    private static final SeatRepository INSTANCE = new SeatRepositoryDatabase();
    private final DatabaseApi database = Database.getInstance();

    /**
     * Constructor.
     */
    private SeatRepositoryDatabase() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static SeatRepository getInstance() {
        return INSTANCE;
    }

    /**
     * Add object to repository.
     *
     * @param seat Object to add.
     */
    @Override
    public void add(Seat seat) throws SQLException {
        try (Connection connection = this.database.getConnection();
             PreparedStatement addAccount = connection.prepareStatement(this.database.getQuery("sql.query.account.add"));
             PreparedStatement addSeat = connection.prepareStatement(this.database.getQuery("sql.query.seat.add"))
        ) {
            connection.setAutoCommit(false);
            try {
                if (seat.getOwner() != Account.getEmptyAccount()) {
                    DatabaseObjectUtils.FillStatement.AccountStatements.fillAdd(addAccount, seat.getOwner());
                    addAccount.execute();
                }
                DatabaseObjectUtils.FillStatement.SeatStatements.fillAdd(addSeat, seat);
                addSeat.execute();
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
     * Get list of all seats in the repository.
     *
     * @return List of seats.
     */
    @Override
    public List<Seat> getAll() throws SQLException {
        List<Seat> list = new ArrayList<>();
        try (Connection connection = this.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.database.getQuery("sql.query.seat.get_all"))
        ) {
            try (ResultSet res = statement.executeQuery()) {
                while (res.next()) {
                    list.add(DatabaseObjectUtils.FormObject.formSeat(res));
                }
            }
        }
        return list;
    }

    /**
     * Updates seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @param newObj Seat with new values.
     */
    @Override
    public void updateByPlace(int row, int column, Seat newObj) throws SQLException {
        try (Connection connection = this.database.getConnection();
             PreparedStatement addAccount = connection.prepareStatement(this.database.getQuery("sql.query.account.add"));
             PreparedStatement updateSeat = connection.prepareStatement(this.database.getQuery("sql.query.seat.update_by_place"))
        ) {
            connection.setAutoCommit(false);
            try {
                if (newObj.getOwner() != Account.getEmptyAccount()) {
                    DatabaseObjectUtils.FillStatement.AccountStatements.fillAdd(addAccount, newObj.getOwner());
                    addAccount.execute();
                }
                DatabaseObjectUtils.FillStatement.SeatStatements.fillUpdateByPlace(updateSeat, row, column, newObj);
                updateSeat.execute();
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
     * Returns seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @return Seat with needed row and column or empty Seat object.
     */
    @Override
    public Seat getByPlace(int row, int column) throws SQLException {
        Seat result = Seat.getEmptySeat();
        try (Connection connection = this.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.database.getQuery("sql.query.seat.get_by_place"))
        ) {
            DatabaseObjectUtils.FillStatement.SeatStatements.fillGetByPlace(statement, row, column);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    result = DatabaseObjectUtils.FormObject.formSeat(res);
                }
            }
        }
        return result;
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() throws SQLException {
        this.database.clearTables();
    }
}
