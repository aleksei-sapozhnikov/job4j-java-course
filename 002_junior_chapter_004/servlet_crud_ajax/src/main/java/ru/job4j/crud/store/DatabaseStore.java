package ru.job4j.crud.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.util.*;

import static ru.job4j.crud.store.DatabaseStore.SQLQueries.*;

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
    private static final String PROPERTIES = "ru/job4j/crud/store/database_no_functions.properties";
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
    private final Map<SQLQueries, String> queries;

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
        this.dropFunctions();
        this.dropTables();
        this.createTables();
        // this.createFunctions();
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
    private Map<SQLQueries, String> loadQueries(Properties prop) {
        Map<SQLQueries, String> result = new HashMap<>();
        result.put(STRUCTURE_CREATE_TABLES, prop.getProperty("sql.structure.createTables"));
        result.put(STRUCTURE_DROP_TABLES, prop.getProperty("sql.structure.dropTables"));
        result.put(STRUCTURE_DROP_FUNCTIONS, prop.getProperty("sql.structure.dropFunctions"));
        // functions
        result.put(FUNCTION_INSERT_USER, prop.getProperty("sql.createFunction.insertUser"));
        result.put(FUNCTION_UPDATE_USER, prop.getProperty("sql.createFunction.updateUser"));
        // queries
        result.put(QUERY_INSERT_USER, prop.getProperty("sql.query.insertUser"));
        result.put(QUERY_UPDATE_USER, prop.getProperty("sql.query.updateUser"));
        result.put(QUERY_DELETE_USER_BY_ID, prop.getProperty("sql.query.deleteUserById"));
        result.put(QUERY_FIND_USER_BY_ID, prop.getProperty("sql.query.findUserById"));
        result.put(QUERY_FIND_ALL_USERS, prop.getProperty("sql.query.findAllUsers"));
        result.put(QUERY_FIND_ALL_COUNTRIES, prop.getProperty("sql.query.findAlLCountries"));
        result.put(QUERY_FIND_ALL_CITIES, prop.getProperty("sql.query.findAlLCities"));
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
             PreparedStatement create = connection.prepareStatement(queries.get(STRUCTURE_CREATE_TABLES))
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
             PreparedStatement create = connection.prepareStatement(queries.get(FUNCTION_INSERT_USER));
             PreparedStatement update = connection.prepareStatement(queries.get(FUNCTION_UPDATE_USER))
        ) {
            create.execute();
            update.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Drops all existing functions in database.
     */
    private void dropFunctions() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement drop = connection.prepareStatement(queries.get(STRUCTURE_DROP_FUNCTIONS))
        ) {
            drop.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Drops all existing tables in database.
     */
    private void dropTables() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement drop = connection.prepareStatement(queries.get(STRUCTURE_DROP_TABLES))
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
        // this.createFunctions();
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
             PreparedStatement insert = connection.prepareStatement(queries.get(QUERY_INSERT_USER))
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
        int index = 0;
        statement.setTimestamp(++index, Timestamp.from(Instant.ofEpochMilli(user.getCreated())));
        statement.setString(++index, user.getCredentials().getLogin());
        statement.setString(++index, user.getCredentials().getPassword());
        statement.setString(++index, user.getCredentials().getRole().toString());
        statement.setString(++index, user.getInfo().getName());
        statement.setString(++index, user.getInfo().getEmail());
        statement.setString(++index, user.getInfo().getCountry());
        statement.setString(++index, user.getInfo().getCity());
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
             PreparedStatement update = connection.prepareStatement(queries.get(QUERY_UPDATE_USER))
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
        int index = 0;
        statement.setInt(++index, upd.getId());
        statement.setString(++index, upd.getCredentials().getLogin());
        statement.setString(++index, upd.getCredentials().getPassword());
        statement.setString(++index, upd.getCredentials().getRole().toString());
        statement.setString(++index, upd.getInfo().getName());
        statement.setString(++index, upd.getInfo().getEmail());
        statement.setString(++index, upd.getInfo().getCountry());
        statement.setString(++index, upd.getInfo().getCity());
        int rowsChanged = statement.executeUpdate();
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
             PreparedStatement delete = connection.prepareStatement(queries.get(QUERY_DELETE_USER_BY_ID))
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
             PreparedStatement find = connection.prepareStatement(queries.get(QUERY_FIND_USER_BY_ID))
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
        List<User> result = Collections.emptyList();
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement find = connection.prepareStatement(queries.get(QUERY_FIND_ALL_USERS))
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
        List<User> result = new ArrayList<>();
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
     * Returns list of all countries in the system.
     *
     * @return List of all countries in the system.
     */
    @Override
    public List<String> findAllCountries() {
        List<String> result = Collections.emptyList();
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.queries.get(QUERY_FIND_ALL_COUNTRIES))
        ) {
            result = this.dbSelectListOfStrings(statement);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns list of all cities in the system.
     *
     * @return List of all cities in the system.
     */
    @Override
    public List<String> findAllCities() {
        List<String> result = Collections.emptyList();
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.queries.get(QUERY_FIND_ALL_CITIES))
        ) {
            result = this.dbSelectListOfStrings(statement);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Gets list of all roles in the system from the database.
     *
     * @return List of roles.
     */
    private List<String> dbSelectListOfStrings(PreparedStatement statement) {
        List<String> result = new ArrayList<>();
        try (ResultSet res = statement.executeQuery()) {
            while (res.next()) {
                result.add(res.getString(1));
            }
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
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
        int index = 0;
        return new User(
                res.getInt(++index),                                        // id
                res.getTimestamp(++index).getTime(),                        // created
                new Credentials(
                        res.getString(++index),                             // login
                        res.getString(++index),                             // password
                        Credentials.Role.valueOf(res.getString(++index))    // role
                ),
                new Info(
                        res.getString(++index),                             // name
                        res.getString(++index),                             // email
                        res.getString(++index),                             // country
                        res.getString(++index)                              // city
                )
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

    /**
     * Enum with possible sql operations.
     */
    enum SQLQueries {
        // structure
        STRUCTURE_CREATE_TABLES,
        STRUCTURE_DROP_TABLES,
        STRUCTURE_DROP_FUNCTIONS,
        // functions
        FUNCTION_INSERT_USER,
        FUNCTION_UPDATE_USER,
        // queries
        QUERY_INSERT_USER,
        QUERY_UPDATE_USER,
        QUERY_DELETE_USER_BY_ID,
        QUERY_FIND_USER_BY_ID,
        QUERY_FIND_ALL_USERS,
        QUERY_FIND_ALL_COUNTRIES,
        QUERY_FIND_ALL_CITIES
    }

    public static void main(String[] args) {
        for (User user : DatabaseStore.getInstance().findAll()) {
            LOG.info(user);
        }
    }

}
