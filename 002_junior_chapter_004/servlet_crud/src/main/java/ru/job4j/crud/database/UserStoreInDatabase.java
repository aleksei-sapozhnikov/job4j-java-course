package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.CommonMethods;
import ru.job4j.crud.Store;
import ru.job4j.crud.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
     * Container of common useful methods.
     */
    private static final CommonMethods METHODS = CommonMethods.getInstance();
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
    private static UserStoreInDatabase instance = null;

    static {
        try {
            instance = new UserStoreInDatabase();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

    /**
     * Database connection.
     */
    private final Connection connection;

    /**
     * Constructs new UserStoreInDatabase object.
     *
     * @throws IOException            Signals that an I/O exception of some sort has occurred.
     * @throws SQLException           Provides information on a database access error or other errors.
     * @throws ClassNotFoundException Shows that no definition for the class with the specified name could be found.
     */
    private UserStoreInDatabase() throws IOException, SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties prop = METHODS.loadProperties(this, PROPERTIES);
        this.connection = METHODS.getConnectionToDatabase(
                prop.getProperty("db.type"), prop.getProperty("db.address"), prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password")
        );
        this.createTables();
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
        try {
            METHODS.dbPerformUpdate(this.connection, QUERIES.get("createTables"));
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    /**
     * Drops all existing tables in the database.
     */
    @Override
    public void clear() {
        try {
            METHODS.dbPerformUpdate(this.connection, QUERIES.get("dropTables"));
            METHODS.dbPerformUpdate(this.connection, QUERIES.get("createTables"));
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
        String query = String.format(
                QUERIES.get("insertUser"),
                add.getName(), add.getLogin(), add.getEmail(),
                Timestamp.from(Instant.ofEpochMilli(add.getCreated()))
        );
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            if (res.next()) {
                id = res.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return id;
    }

    /**
     * Updates User fields.
     *
     * @param update object with the same unique id as of the object
     *               being updated and with new fields values.
     * @return <tt>true</tt> if updated successfully, <tt>false</tt> if not
     * (e.g. object with this id was not found).
     */
    @Override
    public boolean update(final User update) {
        boolean result = false;
        String query = String.format(QUERIES.get("updateUserById"),
                update.getName(), update.getLogin(), update.getEmail(),
                update.getId());
        try {
            result = this.doUpdateQueryAndCheck(query);
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
     * @param query Update query to perform.
     * @return <tt>true</tt> if 1 row changed, <tt>false</tt> if none.
     * @throws SQLException     Provides information on a database access error or other errors.
     * @throws RuntimeException If more than 1 row is changed. That indicates a serious problem
     *                          because user id must be unique and lead to only one user.
     */
    private boolean doUpdateQueryAndCheck(final String query) throws SQLException {
        int changedRowsNeeded = 1;
        int rowsChanged = METHODS.dbPerformUpdate(this.connection, query);
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
        try {
            result = this.findById(id);
            String query = String.format(QUERIES.get("deleteUserById"), id);
            METHODS.dbPerformUpdate(this.connection, query);
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result;
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
        String queryOld = String.format(QUERIES.get("findUserById"), id);
        try (ResultSet res = this.connection.createStatement().executeQuery(queryOld)) {
            if (res.next()) {
                result = this.formUser(res);
            }
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
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
        String query = QUERIES.get("findAllUsers");
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            while (res.next()) {
                result.add(
                        this.formUser(res)
                );
            }
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
            result.clear();
        }
        return result.toArray(new User[0]);
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
        if (this.connection != null) {
            this.connection.close();
        }
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
                    .add("VALUES (\'%s\', \'%s\', \'%s\', \'%s\')")
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
                    .add("SET name = \'%s\', login = \'%s\', email = \'%s\'")
                    .add("WHERE users.id = \'%s\'")
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
                    .add("WHERE users.id = \'%s\'")
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
                    .add("WHERE id = \'%s\'")
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
