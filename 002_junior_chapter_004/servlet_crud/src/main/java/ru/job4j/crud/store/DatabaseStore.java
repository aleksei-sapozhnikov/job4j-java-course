package ru.job4j.crud.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Role;
import ru.job4j.crud.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.util.*;

/**
 * Storage for Users. Uses database.
 * <p>
 * Each User is identified by integer id given by
 * the storage as the result of the "add" operation.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class DatabaseStore implements Store<User> {
    /**
     * Properties file loaded as resource.
     */
    private static final String PROPERTIES = "ru/job4j/crud/database/database.properties";
    /**
     * Connection pool data source - creates connections to database.
     */
    private static final BasicDataSource CONNECTION_POOL = new BasicDataSource();
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DatabaseStore.class);
    /**
     * Class instance.
     */
    private static DatabaseStore instance;

    static {
        try {
            instance = new DatabaseStore();
        } catch (IOException | ClassNotFoundException e) {
            LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

    /**
     * Map with sql queries.
     */
    private final Map<String, String> queries;

    /**
     * Constructs new DatabaseStore object.
     *
     * @throws IOException            Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Shows that no definition for the class with the specified name could be found.
     */
    private DatabaseStore() throws IOException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties prop = this.loadProperties(PROPERTIES);
        this.queries = this.loadQueries(prop);
        this.configureConnectionPool(CONNECTION_POOL,
                prop.getProperty("db.type"), prop.getProperty("db.address"),
                prop.getProperty("db.name"), prop.getProperty("db.user"), prop.getProperty("db.password"));
        this.createTables();
        this.createFunctions();
    }

    /**
     * Returns class instance.
     *
     * @return Class instance.
     */
    public static DatabaseStore getInstance() {
        return instance;
    }

    /**
     * Loads properties file using ClassLoader.
     *
     * @param propFile path to the properties file
     *                 e.g: "ru/job4j/vacancies/main.properties"
     * @return Properties object with values read from file.
     * @throws IOException If problems happened with reading from/to InputStream.
     */
    private Properties loadProperties(String propFile) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = this.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(propFile)) {
            props.load(input);
        }
        return props;
    }

    /**
     * Returns map with sql queries.
     *
     * @param prop Properties where to load queries from.
     * @return Map with sql queries.
     */
    private Map<String, String> loadQueries(Properties prop) {
        Map<String, String> result = new HashMap<>();
        // structure
        result.put("structureCreateTables", prop.getProperty("sql.structure.createTables"));
        result.put("structureDropTables", prop.getProperty("sql.structure.dropTables"));
        result.put("structureDropFunctions", prop.getProperty("sql.structure.dropFunctions"));
        // functions
        result.put("functionInsertUser", prop.getProperty("sql.function.insertUser"));
        result.put("functionUpdateUser", prop.getProperty("sql.function.updateUser"));
        // queries
        result.put("insertUser", prop.getProperty("sql.query.insertUser"));
        result.put("updateUserById", prop.getProperty("sql.query.updateUserById"));
        result.put("deleteUserById", prop.getProperty("sql.query.deleteUserById"));
        result.put("findUserById", prop.getProperty("sql.query.findUserById"));
        result.put("findAllUsers", prop.getProperty("sql.query.findAllUsers"));
        return result;
    }

    /**
     * Configures connection-to-database pool.
     *
     * @param pool     Pool to configure.
     * @param type     Database type.
     * @param address  Database address.
     * @param name     Database name.
     * @param user     Database user.
     * @param password Database password.
     */
    private void configureConnectionPool(BasicDataSource pool, String type, String address,
                                         String name, String user, String password) {
        String url = String.format("jdbc:%s:%s%s", type, address, "".equals(name) ? "" : "/".concat(name));
        pool.setDriverClassName("org.postgresql.Driver");
        pool.setUrl(url);
        pool.setUsername(user);
        pool.setPassword(password);
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    /**
     * Creates needed tables in the database if needed.
     */
    private void createTables() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement create = connection.prepareStatement(queries.get("structureCreateTables"))
        ) {
            create.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Creates database pl/sql functions.
     */
    private void createFunctions() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement create = connection.prepareStatement(queries.get("functionInsertUser"));
             PreparedStatement update = connection.prepareStatement(queries.get("functionUpdateUser"))
        ) {
            this.queryAsOneTransaction(connection, create, update);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Drops all existing functions in database.
     */
    private void dropFunctions() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement drop = connection.prepareStatement(queries.get("structureDropFunctions"))
        ) {
            drop.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Performs sql queries as one transaction, by settin autocommit to "off".
     *
     * @param connection Connection to database.
     * @param statements Statements to perform.
     * @throws SQLException Provides information on a database access error or other errors
     */
    private void queryAsOneTransaction(Connection connection, PreparedStatement... statements) throws SQLException {
        connection.setAutoCommit(false);
        try {
            for (PreparedStatement statement : statements) {
                statement.execute();
            }
            connection.commit();
        } catch (Exception e) {
            LOG.error(String.format("Exception: %s", e.getMessage()));
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Drops all existing tables in database.
     */
    private void dropTables() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement drop = connection.prepareStatement(queries.get("structureDropTables"))
        ) {
            drop.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Drops all existing tables in the database and creates them anew.
     */
    @Override
    public void clear() {
        this.dropTables();
        this.dropFunctions();
        this.createTables();
        this.createFunctions();
    }

    /**
     * Adds new User to the storage.
     *
     * @param add User to add.
     * @return Unique id given to the object by the
     * storage system or <tt>-1</tt> if couldn't add it.
     */
    @Override
    public int add(final User add) {
        int id = -1;
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement insert = connection.prepareStatement(queries.get("insertUser"))
        ) {
            id = this.dbInsertUser(insert, add, id);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return id;
    }

    /**
     * Performs database insert operation with particular user and returns id given by database.
     *
     * @param statement Prepared statement to put user field values in.
     * @param user      User object to insert into database.
     * @return New integer id given to this user in database or previous id value if somehow new
     * id was not given by database.
     * @throws SQLException Provides information on a database access error or other errors.
     */
    private int dbInsertUser(PreparedStatement statement, User user, int prevId) throws SQLException {
        int result = prevId;
        statement.setString(1, user.getName());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getEmail());
        statement.setTimestamp(5, Timestamp.from(Instant.ofEpochMilli(user.getCreated())));
        statement.setString(6, user.getRole().toString());
        statement.setString(7, user.getCountry());
        statement.setString(8, user.getCity());
        try (ResultSet res = statement.executeQuery()) {
            if (res.next()) {
                result = res.getInt(1);
            }
        }
        return result;
    }

    /**
     * Updates User fields.
     *
     * @param upd object with the same unique id as of the object
     *            being updated and with new fields values.
     * @return <tt>true</tt> if updated successfully, <tt>false</tt> if not
     * (e.g. object with this id was not found).
     */
    @Override
    public boolean update(final User upd) {
        boolean result = false;
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement update = connection.prepareStatement(queries.get("updateUserById"))
        ) {
            result = this.updateUserAndCheckRowsChanged(update, upd);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result;
    }

    /**
     * Performs update query and check it happened as needed.
     * <p>
     * If more than one row is changed - there is a serious error and
     * RuntimeException is thrown. User id must be unique and only one
     * row can change.
     *
     * @param statement Prepared statement to put user field values in.
     * @param upd       User object with id of user to update and new field values.
     * @return <tt>true</tt> if 1 row changed, <tt>false</tt> if none.
     * @throws SQLException     Provides information on a database access error or other errors.
     * @throws RuntimeException If more than 1 row is changed. That indicates a serious problem
     *                          because user id must be unique and lead to only one user.
     */
    private boolean updateUserAndCheckRowsChanged(PreparedStatement statement, User upd) throws SQLException {
        int changedRowsNeeded = 1;
        statement.setInt(1, upd.getId());
        statement.setString(2, upd.getName());
        statement.setString(3, upd.getLogin());
        statement.setString(4, upd.getPassword());
        statement.setString(5, upd.getEmail());
        statement.setString(6, upd.getRole().toString());
        statement.setString(7, upd.getCountry());
        statement.setString(8, upd.getCity());
        int rowsChanged;
        try (ResultSet res = statement.executeQuery()) {
            if (res.next()) {
                rowsChanged = res.getInt(1);
            } else {
                LOG.error("Update method did not return number of rows changed");
                rowsChanged = 0;
            }
        }
        if (rowsChanged > 1) {
            LOG.error("Update method changed more than 1 row");
        }
        return rowsChanged == changedRowsNeeded;
    }

    /**
     * Deletes user with given id from the storage.
     *
     * @param id Id of the object to delete.
     * @return Deleted object if found and deleted, <tt>null</tt> if not.
     */
    @Override
    public User delete(final int id) {
        User result = null;
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement delete = connection.prepareStatement(queries.get("deleteUserById"))
        ) {
            result = this.findById(id);
            this.dbDeleteUser(delete, id);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result;
    }

    /**
     * Performs "delete" query in database.
     *
     * @param statement Prepared statement to put user field values in.
     * @param id        Id of user to delete.
     * @throws SQLException Provides information on a database access error or other errors.
     */
    private void dbDeleteUser(PreparedStatement statement, int id) throws SQLException {
        statement.setInt(1, id);
        statement.execute();
    }

    /**
     * Returns user with given id.
     *
     * @param id Id of the needed object.
     * @return Object with given id or <tt>null</tt> if object not found.
     */
    @Override
    public User findById(final int id) {
        User result = null;
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement find = connection.prepareStatement(queries.get("findUserById"))
        ) {
            result = this.dbSelectUserById(find, id);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result;
    }

    /**
     * Selects user with given id in database.
     *
     * @param statement Prepared statement to put user field values in.
     * @param id        Id of user to find.
     * @throws SQLException Provides information on a database access error or other errors.
     */
    private User dbSelectUserById(PreparedStatement statement, int id) throws SQLException {
        User result = null;
        statement.setInt(1, id);
        try (ResultSet res = statement.executeQuery()) {
            if (res.next()) {
                result = this.formUser(res);
            }
        }
        return result;
    }

    /**
     * Returns array of all users stored in the storage.
     *
     * @return Array of stored objects.
     */
    @Override
    public List<User> findAll() {
        List<User> result = new LinkedList<>();
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement find = connection.prepareStatement(queries.get("findAllUsers"))
        ) {
            result = this.dbSelectAllUsers(find);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result;
    }

    /**
     * Selects all users from database.
     *
     * @param statement Prepared statement to put user field values in.
     * @throws SQLException Provides information on a database access error or other errors.
     */
    private List<User> dbSelectAllUsers(PreparedStatement statement) throws SQLException {
        List<User> result = new LinkedList<>();
        try (ResultSet res = statement.executeQuery()) {
            while (res.next()) {
                result.add(
                        this.formUser(res)
                );
            }
        }
        return result;
    }

    /**
     * Forms user object from the ResultSet returned by sql query.
     *
     * @param res ResultSet returned by query.
     * @return User object.
     * @throws SQLException Provides information on a database access error or other errors.
     */
    private User formUser(ResultSet res) throws SQLException {
        return new User(
                res.getInt(1),                              // id
                res.getString(2),                           // name
                res.getString(3),                           // login
                res.getString(4),                           // password
                res.getString(5),                           // email
                res.getTimestamp(6).getTime(),              // created
                Role.valueOf(res.getString(7)),             // role
                res.getString(8),                           // country
                res.getString(9)                            // city
        );
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * @throws SQLException If problems occurred while closing the connection.
     */
    @Override
    public void close() throws SQLException {
        CONNECTION_POOL.close();
    }
}
