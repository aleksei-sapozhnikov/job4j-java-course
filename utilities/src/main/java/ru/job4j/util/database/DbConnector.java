package ru.job4j.util.database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.util.Properties;

/**
 * Connects to database using Connection pool (BasicDataSource)
 * Supplies optional connection object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DbConnector implements Connector {
    /**
     * Connection pool.
     */
    private final BasicDataSource pool;

    /**
     * Constructor.
     *
     * @param pool       Connection pool.
     * @param properties Database properties.
     */
    public DbConnector(BasicDataSource pool, Properties properties) {
        this.pool = pool;
        this.setRequiredParameters(properties);
        this.setOptionalParameters(properties);
    }

    /**
     * Returns Optional Connection object.
     *
     * @return Connection object.
     */
    public Connection getConnection() {
        Connection result = null;
        try {
            result = this.pool.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Sets required parameters for database connection.
     *
     * @param properties Object holding parameters.
     * @throws RuntimeException If parameter not found.
     */
    private void setRequiredParameters(Properties properties) {
        CertainPropertiesHolder holder = new CertainPropertiesHolder(properties, "db.", "file:");
        this.pool.setDriverClassName(holder.get("db.driver"));
        this.pool.setUrl(holder.get("db.url"));
        this.pool.setUsername(holder.get("db.user"));
        this.pool.setPassword(holder.get("db.password"));

    }

    /**
     * Sets optional parameters for database connection.
     * Takes parameters from given holder if found, or default values otherwise.
     *
     * @param properties Object holding parameters.
     */
    private void setOptionalParameters(Properties properties) {
        CertainPropertiesHolder holder = new CertainPropertiesHolder(properties, "pool.", "file:");
        this.pool.setMinIdle(Integer.parseInt(holder
                .getOrDefault("pool.minIdle", "5")));
        this.pool.setMaxIdle(Integer.parseInt(holder
                .getOrDefault("pool.maxIdle", "10")));
        this.pool.setMaxOpenPreparedStatements(Integer.parseInt(holder
                .getOrDefault("pool.setMaxOpenPreparedStatements", "100")));
    }

    /**
     * Closes all active database-connection resources.
     *
     * @throws Exception Possible exception.
     */
    @Override
    public void close() throws Exception {
        this.pool.close();
    }
}