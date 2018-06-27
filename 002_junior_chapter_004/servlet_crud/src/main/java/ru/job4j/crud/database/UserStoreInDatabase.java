package ru.job4j.crud.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.Store;
import ru.job4j.crud.User;

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
public class UserStoreInDatabase implements Store<User> {
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
    private static final Logger LOG = LogManager.getLogger(UserStoreInDatabase.class);
    /**
     * Map with sql queries.
     */
    private static final Map<String, String> QUERIES = new HashMap<String, String>() {
        {
            put("createTables", CreateQuery.createTables());
            put("dropTables", CreateQuery.dropAllTables());
            put("insertUser", CreateQuery.insertUser());
            put("updateUserById", CreateQuery.updateUserById());
            put("deleteUserById", CreateQuery.deleteUserById());
            put("findUserById", CreateQuery.findUserById());
            put("findAllUsers", CreateQuery.findAllUsers());
        }
    };
    /**
     * Class instance.
     */
    private static UserStoreInDatabase instance;

    static {
        try {
            instance = new UserStoreInDatabase();
        } catch (IOException | ClassNotFoundException e) {
            LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

    /**
     * Constructs new UserStoreInDatabase object.
     *
     * @throws IOException            Signals that an I/O exception of some sort has occurred.
     * @throws ClassNotFoundException Shows that no definition for the class with the specified name could be found.
     */
    private UserStoreInDatabase() throws IOException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties prop = this.loadProperties(PROPERTIES);
        this.configureConnectionPool(CONNECTION_POOL,
                prop.getProperty("db.type"), prop.getProperty("db.address"),
                prop.getProperty("db.name"), prop.getProperty("db.user"), prop.getProperty("db.password"));
        this.createTables();
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
     * Returns class instance.
     *
     * @return Class instance.
     */
    public static UserStoreInDatabase getInstance() {
        return instance;
    }

    /**
     * Creates needed tables in the database if needed.
     */
    private void createTables() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement create = connection.prepareStatement(QUERIES.get("createTables"))
        ) {
            create.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Drops all existing tables in the database.
     */
    @Override
    public void clear() {
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement drop = connection.prepareStatement(QUERIES.get("dropTables"));
             PreparedStatement create = connection.prepareStatement(QUERIES.get("createTables"))
        ) {
            drop.execute();
            create.execute();
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
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
             PreparedStatement insert = connection.prepareStatement(QUERIES.get("insertUser"))
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
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private int dbInsertUser(PreparedStatement statement, User user, int prevId) throws SQLException {
        int result = prevId;
        statement.setString(1, user.getName());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getEmail());
        statement.setTimestamp(4, Timestamp.from(Instant.ofEpochMilli(user.getCreated())));
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
             PreparedStatement update = connection.prepareStatement(QUERIES.get("updateUserById"))
        ) {
            result = this.doUpdateUserAndCheckRowsChanged(update, upd);
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
     * @param user      User object with id of user to update and new field values.
     * @return <tt>true</tt> if 1 row changed, <tt>false</tt> if none.
     * @throws SQLException     Provides information on a database access error or other errors.
     * @throws RuntimeException If more than 1 row is changed. That indicates a serious problem
     *                          because user id must be unique and lead to only one user.
     */
    private boolean doUpdateUserAndCheckRowsChanged(PreparedStatement statement, User user) throws SQLException {
        int changedRowsNeeded = 1;
        statement.setString(1, user.getName());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getEmail());
        statement.setInt(4, user.getId());
        int rowsChanged = statement.executeUpdate();
        if (rowsChanged > 1) {
            throw new RuntimeException("Update method changed more than 1 row");
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
             PreparedStatement delete = connection.prepareStatement(QUERIES.get("deleteUserById"))
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
             PreparedStatement find = connection.prepareStatement(QUERIES.get("findUserById"))
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
    public User[] findAll() {
        List<User> result = new LinkedList<>();
        try (Connection connection = CONNECTION_POOL.getConnection();
             PreparedStatement find = connection.prepareStatement(QUERIES.get("findAllUsers"))
        ) {
            result = this.dbSelectAllUsers(find);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result.toArray(new User[0]);
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
                res.getInt(1),                      // id
                res.getString(2),                   // name
                res.getString(3),                   // login
                res.getString(4),                   // email
                res.getTimestamp(5).getTime()       // created
        );
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * @throws SQLException If problems occured while closing the connection.
     */
    @Override
    public void close() throws SQLException {
        CONNECTION_POOL.close();
    }

    /**
     * Class to create sql queries strings.
     */
    private static class CreateQuery {

        /**
         * Forms sql query to create needed tables.
         *
         * @return Sql query to create needed tables.
         */
        private static String createTables() {
            return new StringJoiner(" ")
                    .add("CREATE TABLE IF NOT EXISTS users (")
                    .add("id SERIAL PRIMARY KEY,")
                    .add("name TEXT,")
                    .add("login TEXT,")
                    .add("email TEXT,")
                    .add("created TIMESTAMP WITH TIME ZONE")
                    .add(");")
                    .toString();
        }

        /**
         * Forms sql query to drop all existing tables in database.
         * That is needed for testing (to clear testing database used
         * by many applications).
         *
         * @return Sql query to to drop all existing tables in database.
         */
        private static String dropAllTables() {
            return new StringJoiner(" ")
                    .add("DO $$")
                    .add("DECLARE brow record; BEGIN")
                    .add("FOR brow IN (select 'drop table \"' || tablename || '\" cascade;' as table_name")
                    .add("FROM pg_tables WHERE schemaname = 'public')")
                    .add("LOOP EXECUTE brow.table_name; END LOOP;")
                    .add("END; $$")
                    .toString();
        }

        /**
         * Forms sql query to insert new user into database.
         *
         * @return Sql query to insert new user into database.
         */
        private static String insertUser() {
            return new StringJoiner(" ")
                    .add("INSERT INTO users")
                    .add("(name, login, email, created)")
                    .add("VALUES (?, ?, ?, ?)")
                    .add("RETURNING id")
                    .toString();
        }

        /**
         * Forms sql query to update user.
         *
         * @return Sql query to update user.
         */
        private static String updateUserById() {
            return new StringJoiner(" ")
                    .add("UPDATE users")
                    .add("SET name = ?, login = ?, email = ?")
                    .add("WHERE users.id = ?")
                    .toString();
        }

        /**
         * Forms sql query to delete user.
         *
         * @return Sql query to delete user.
         */
        private static String deleteUserById() {
            return new StringJoiner(" ")
                    .add("DELETE FROM users")
                    .add("WHERE users.id = ?")
                    .toString();
        }

        /**
         * Forms sql query to find user by id.
         *
         * @return Sql query to find user by id.
         */
        private static String findUserById() {
            return new StringJoiner(" ")
                    .add("SELECT id, name, login, email, created FROM users")
                    .add("WHERE id = ?")
                    .toString();
        }

        /**
         * Forms sql query to find all users in database.
         *
         * @return Sql query to find all users in database.
         */
        private static String findAllUsers() {
            return new StringJoiner(" ")
                    .add("SELECT id, name, login, email, created FROM users")
                    .add("ORDER BY id")
                    .toString();
        }
    }
}
