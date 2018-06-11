package ru.job4j.vacancies;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class VacancyStorage implements AutoCloseable {
    private final Connection connection;

    public VacancyStorage(String config) throws IOException, SQLException {
        Properties p = this.loadProperties(config);
        this.connection = this.dbGetConnection(
                p.getProperty("db.type"), p.getProperty("db.address"), p.getProperty("db.name"),
                p.getProperty("db.user"), p.getProperty("db.password"));
    }

    private Properties loadProperties(String config) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = VacancyStorage.class.getClassLoader();
        try (InputStream input = loader.getResourceAsStream(config)) {
            props.load(input);
        }
        return props;
    }

    private Connection dbGetConnection(String type, String address, String name,
                                       String user, String password) throws SQLException {
        String url = String.format("jdbc:%s:%s/%s", type, address, name);
        return DriverManager.getConnection(url, user, password);
    }

    public int add(Vacancy vacancy) {
        return 0;
    }


    /**
     * Closes this resource connection. Is invoked automatically on
     * objects managed by the <tt>try-with-resources</tt> statement.
     *
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }

    }
}
