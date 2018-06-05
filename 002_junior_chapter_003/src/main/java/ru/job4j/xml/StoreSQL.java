package ru.job4j.xml;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Class to generate database with needed amount of values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.06.2018
 */
public class StoreSQL implements AutoCloseable {
    /**
     * Connection to database.
     */
    private final Connection connection;

    /**
     * Constructs new object and connects it to database.
     *
     * @param properties Path to .properties file with database connection parameters.
     * @param dbAddress  Database address (file).
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public StoreSQL(Path properties, Path dbAddress) throws IOException, SQLException {
        Properties p = new Properties();
        p.load(new FileReader(properties.toString()));
        this.connection = this.dbGetConnection(p, dbAddress);
    }

    /**
     * Returns database connection.
     *
     * @param properties Path to .properties file with database connection parameters.
     * @param dbAddress  Database address (file).
     * @return Database connection.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private Connection dbGetConnection(Properties properties, Path dbAddress) throws SQLException {
        String url = String.format(
                "jdbc:%s:%s",
                properties.getProperty("db_type"), dbAddress.toString()
        );
        return DriverManager.getConnection(
                url,
                properties.getProperty("db_user"),
                properties.getProperty("db_password")
        );
    }

    /**
     * Creates tables in database if needed and calls method to
     * generate given number of values in database.
     *
     * @param nValues Numver of values to generate.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public void generate(int nValues) throws SQLException {
        this.dbPerformUpdate("CREATE TABLE IF NOT EXISTS entry (field INTEGER PRIMARY KEY)");
        this.dbPerformUpdate("DELETE FROM entry");
        this.dbAddValuesAsGroup(nValues);
    }

    /**
     * Generates given number of values and adds them to database.
     *
     * @param nValues Number of values to generate.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbAddValuesAsGroup(int nValues) throws SQLException {
        this.connection.setAutoCommit(false);
        try {
            for (int i = 1; i <= nValues; i++) {
                this.dbPerformUpdate(String.format("INSERT INTO entry (field) VALUES (%s)", i));
            }
            this.connection.commit();
        } catch (Exception e) {
            this.connection.rollback();
        } finally {
            this.connection.setAutoCommit(true);
        }
    }

    /**
     * Executes database update operation given by query.
     *
     * @param query Query to execute.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbPerformUpdate(String query) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
        }
    }

    /**
     * Closes connection. Executed in <tt>try-with-resources</tt> statement.
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
