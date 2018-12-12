package ru.job4j.util.database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Connects to database using Connection pool (BasicDataSource)
 * Supplies optional connection object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DbConnector implements Supplier<Optional<Connection>> {
    /**
     * Connection pool.
     */
    private final BasicDataSource pool;
    /**
     * Handler of throwable objects (exceptions).
     */
    private final Consumer<Throwable> throwableHandler;

    /**
     * Constructs instance using given parameters.
     *
     * @param builder Builder object;
     */
    private DbConnector(Builder builder) {
        this.pool = builder.pool;
        this.throwableHandler = builder.throwableHandler;
        this.setRequiredParameters(builder.properties);
        this.setOptionalParameters(builder.properties);
    }

    /**
     * Returns Connection object to main Database class.
     *
     * @return Connection object.
     */
    @Override
    public Optional<Connection> get() {
        Optional<Connection> result = Optional.empty();
        try {
            result = Optional.of(this.pool.getConnection());
        } catch (SQLException e) {
            this.throwableHandler.accept(e);
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
        PropertiesHolder holder = new PropertiesHolder.Builder(properties, "db.").build();
        this.pool.setDriverClassName(this.valueOrException(holder, "db.driver"));
        this.pool.setUsername(this.valueOrException(holder, "db.user"));
        this.pool.setPassword(this.valueOrException(holder, "db.password"));
        this.pool.setUrl(this.formJdbcUrl(holder));
    }

    /**
     * Sets optional parameters for database connection.
     * Takes parameters from given holder if found, or default values otherwise.
     *
     * @param properties Object holding parameters.
     */
    private void setOptionalParameters(Properties properties) {
        PropertiesHolder holder = new PropertiesHolder.Builder(properties, "pool.").build();
        this.pool.setMinIdle(Integer.parseInt(
                holder.get("pool.minIdle").orElse("5")));
        this.pool.setMaxIdle(Integer.parseInt(
                holder.get("pool.maxIdle").orElse("10")));
        this.pool.setMaxOpenPreparedStatements(Integer.parseInt(
                holder.get("pool.setMaxOpenPreparedStatements").orElse("100")));
    }

    /**
     * Gets value from holder or throws RuntimeException.
     *
     * @param holder Holder with values.
     * @param key    Key to find.
     * @return Value associated with key.
     * @throws RuntimeException If parameter not found.
     */
    private String valueOrException(PropertiesHolder holder, String key) {
        String error = "Cannot build object: no needed property: %s";
        return holder
                .get(key)
                .orElseThrow(() -> new RuntimeException(String.format(error, key)));
    }

    /**
     * Forms JDBC URL to connect to database.
     *
     * @param holder Holder with parameters.
     * @return JDBC url.
     * @throws RuntimeException If parameter not found.
     */
    private String formJdbcUrl(PropertiesHolder holder) {
        String type = this.valueOrException(holder, "db.type");
        String address = this.valueOrException(holder, "db.address");
        String name = this.valueOrException(holder, "db.name");
        return String.format("jdbc:%s:%s%s", type, address,
                "".equals(name) ? "" : "/".concat(name)
        );
    }

    /**
     * Builder class for the DbConnector.
     */
    public static class Builder {
        /**
         * Required fields.
         */
        private final BasicDataSource pool;
        private final Properties properties;
        /**
         * Optional fields.
         */
        private Consumer<Throwable> throwableHandler = Throwable::printStackTrace;

        /**
         * Constructor to set required fields.
         *
         * @param pool       Connection pool to use.
         * @param properties Database properties.
         */
        public Builder(BasicDataSource pool, Properties properties) {
            this.pool = pool;
            this.properties = properties;
        }

        /**
         * Sets 'throwableHandler' field value and returns this builder.
         *
         * @param value Value to set.
         */
        public Builder setThrowableHandler(Consumer<Throwable> value) {
            this.throwableHandler = value;
            return this;
        }

        /**
         * Creates new DbConnector object using current builder.
         *
         * @return The Builder object.
         */
        public DbConnector build() {
            return new DbConnector(this);
        }


    }


}