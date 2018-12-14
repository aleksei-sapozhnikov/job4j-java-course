package ru.job4j.util.database;


import ru.job4j.util.methods.ConnectionUtils;

import java.sql.Connection;

/**
 * Connection, which rollback all commits.
 * It is used for integration test.
 */
public class DbConnectorRollback implements Connector {
    private final Connector dbConnector;

    public DbConnectorRollback(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public Connection getConnection() {
        return this.getConnection(false);
    }

    @Override
    public Connection getConnection(boolean commitAtClose) {
        Connection result = null;
        try {
            // we don't need commit at close, we need rollback always
            result = ConnectionUtils.rollbackAtClose(this.dbConnector.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        this.dbConnector.close();
    }


}