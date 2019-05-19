package ru.job4j.createbeans.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Holds database connections and gives them to those who need.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Connector {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Connector.class);

    /**
     * Connection object to return.
     */
    private final Connection connection;

    /**
     * Connects to database and acquires connection.
     *
     * @param params Connection parameters object.
     * @throws SQLException In case of connection problems.
     */
    public Connector(JdbcParams params) throws SQLException {
        this.connection = DriverManager.getConnection(
                params.getDbUrl(),
                params.getDbUser(),
                params.getDbPassword()
        );
    }

    /**
     * Returns connection object.
     *
     * @return Value of connection field.
     */
    public Connection getConnection() {
        return this.connection;
    }
}
