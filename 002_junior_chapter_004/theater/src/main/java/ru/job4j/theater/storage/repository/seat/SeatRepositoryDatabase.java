package ru.job4j.theater.storage.repository.seat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Seat;
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
    /**
     * Database API.
     */
    private final DatabaseApi database = Database.getInstance();
    /**
     * Account repository (owners).
     */
    private final AccountRepository accountRepository = AccountRepositoryDatabase.getInstance();

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
    public void add(Seat seat) {
        try (DbExecutor executor = this.database.getExecutor()) {
            this.add(seat, executor);
            executor.commit();
        }
    }

    /**
     * Add object to repository.
     * Using given Database executor, no autocommit.
     *
     * @param seat     Object to add.
     * @param executor Database executor.
     */
    @Override
    public void add(Seat seat, DbExecutor executor) {
        Account owner = seat.getOwner();
        if (owner != Account.getEmptyAccount()) {
            this.accountRepository.add(owner, executor);
        }
        executor.execute(
                this.database.getQuery("sql.query.seat.add"),
                List.of(seat.getRow(), seat.getColumn(), seat.getPrice(), owner.getName(), owner.getPhone()),
                PreparedStatement::execute);
    }

    /**
     * Get list of all seats in the repository.
     *
     * @return List of seats.
     */
    @Override
    public List<Seat> getAll() {
        try (DbExecutor executor = this.database.getExecutor()) {
            return this.getAll(executor);
        }
    }

    /**
     * Get list of all seats in the repository.
     * Using given Database executor, no autocommit.
     *
     * @param executor Database executor.
     * @return List of seats.
     */
    @Override
    public List<Seat> getAll(DbExecutor executor) {
        var values = executor.executeQuery(
                this.database.getQuery("sql.query.seat.get_all"),
                List.of(Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class)
        ).orElse(new ArrayList<>());
        return values.stream()
                .map(ObjectForm::formSeat)
                .collect(Collectors.toList());
    }

    /**
     * Updates seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @param newObj Seat with new values.
     */
    @Override
    public void updateByPlace(int row, int column, Seat newObj) {
        try (DbExecutor executor = this.database.getExecutor()) {
            this.updateByPlace(row, column, newObj, executor);
            executor.commit();
        }
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
    @Override
    public void updateByPlace(int row, int column, Seat newObj, DbExecutor executor) {
        Account owner = newObj.getOwner();
        if (owner != Account.getEmptyAccount()) {
            this.accountRepository.add(owner, executor);
        }
        executor.execute(
                this.database.getQuery("sql.query.seat.update_by_place"),
                List.of(newObj.getRow(), newObj.getColumn(), newObj.getPrice(),
                        owner.getName(), owner.getPhone(), row, column),
                PreparedStatement::execute);
    }

    /**
     * Returns seat which has given place and column.
     *
     * @param row    Row of needed seat.
     * @param column Column of needed seat.
     * @return Seat with needed row and column or empty Seat object.
     */
    @Override
    public Seat getByPlace(int row, int column) {
        try (DbExecutor executor = this.database.getExecutor()) {
            return this.getByPlace(row, column, executor);
        }
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
    @Override
    public Seat getByPlace(int row, int column, DbExecutor executor) {
        var values = executor.executeQuery(
                this.database.getQuery("sql.query.seat.get_by_place"),
                List.of(row, column),
                List.of(Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class)
        ).orElse(new ArrayList<>());
        return values.size() > 0
                ? ObjectForm.formSeat(values.get(0))
                : Seat.getEmptySeat();
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() throws SQLException {
        this.database.clearTables();
    }
}
