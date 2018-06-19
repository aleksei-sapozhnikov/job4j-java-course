package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.common.CommonMethods;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class UserStore implements Store<User> {
    private static final String DEFAULT_PROPERTIES = "ru/job4j/crud/default.properties";
    private static final CommonMethods METHODS = CommonMethods.getInstance();
    private final Logger log = LogManager.getLogger(UserStore.class);
    private final Connection connection;

    public UserStore() throws IOException, SQLException {
        this(DEFAULT_PROPERTIES);
    }

    public UserStore(String propFile) throws IOException, SQLException {
        Properties prop = METHODS.loadProperties(this, propFile);
        this.connection = METHODS.getConnectionToDatabase(
                prop.getProperty("db.type"), prop.getProperty("db.address"), prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password")
        );
    }

    @Override
    public int add(User model) {
        return 0;
    }

    @Override
    public User update(int id, User newModel) {
        return null;
    }

    @Override
    public User delete(int id) {
        return null;
    }

    @Override
    public User[] findAll() {
        return new User[0];
    }

    @Override
    public User findById(int id) {
        return null;
    }
}
