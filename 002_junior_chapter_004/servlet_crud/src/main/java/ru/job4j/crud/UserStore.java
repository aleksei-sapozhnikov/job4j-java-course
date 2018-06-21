package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.CommonMethods;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserStore implements Store<User> {

    private static final String PROPERTIES = "ru/job4j/crud/default.properties";
    private static UserStore instance = null;

    private static final CommonMethods METHODS = CommonMethods.getInstance();

    private static final Logger LOG = LogManager.getLogger(UserStore.class);

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

    private final Connection connection;

    static {
        try {
            instance = new UserStore();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

    private UserStore() throws IOException, SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Properties prop = METHODS.loadProperties(this, PROPERTIES);
        this.connection = METHODS.getConnectionToDatabase(
                prop.getProperty("db.type"), prop.getProperty("db.address"), prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password")
        );
        this.createTables();
    }

    public static UserStore getInstance() {
        return instance;
    }

    private void createTables() {
        try {
            METHODS.dbPerformUpdate(this.connection, QUERIES.get("createTables"));
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    public void dropTables() {
        try {
            METHODS.dbPerformUpdate(this.connection, QUERIES.get("dropTables"));
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
    }

    @Override
    public int add(User add) {
        int id = -1;
        String query = String.format(
                QUERIES.get("insertUser"),
                add.getName(), add.getLogin(), add.getEmail(), new java.sql.Timestamp(add.getCreated())
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

    @Override
    public boolean update(User update) {
        boolean result = false;
        String query = String.format(QUERIES.get("updateUserById"),
                update.getName(), update.getLogin(), update.getEmail(),
                update.getId());
        try {
            METHODS.dbPerformUpdate(this.connection, query);
            result = true;
        } catch (SQLException e) {
            LOG.error(String.format("SQL exception: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public User delete(int id) {
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

    @Override
    public User findById(int id) {
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

    private User formUser(ResultSet res) throws SQLException {
        return new User(
                res.getInt(1),                      // id
                res.getString(2),                   // name
                res.getString(3),                   // login
                res.getString(4),                   // email
                res.getTimestamp(5).getTime()       // created
        );
    }

    private User formUser(int id, User old, User upd) {
        String name = upd.getName() != null ? upd.getName() : old.getName();
        String login = upd.getLogin() != null ? upd.getLogin() : old.getLogin();
        String email = upd.getEmail() != null ? upd.getEmail() : old.getEmail();
        return new User(id, name, login, email, old.getCreated());
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
                    .add("created TIMESTAMP")
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
