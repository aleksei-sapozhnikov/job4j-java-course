package ru.job4j.createbeans.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Holds JDBC Parameters.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class JdbcParams {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(JdbcParams.class);

    /**
     * Database url.
     */
    private final String dbUrl;
    /**
     * Database username.
     */
    private final String dbUser;
    /**
     * Database password.
     */
    private final String dbPassword;

    /**
     * Takes connection parameters to store.
     *
     * @param dbUrl      Database url.
     * @param dbUser     Database user.
     * @param dbPassword Database password.
     */
    public JdbcParams(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Returns dbUrl.
     *
     * @return Value of dbUrl field.
     */
    public String getDbUrl() {
        return this.dbUrl;
    }

    /**
     * Returns dbUser.
     *
     * @return Value of dbUser field.
     */
    public String getDbUser() {
        return this.dbUser;
    }

    /**
     * Returns dbPassword.
     *
     * @return Value of dbPassword field.
     */
    public String getDbPassword() {
        return this.dbPassword;
    }
}
