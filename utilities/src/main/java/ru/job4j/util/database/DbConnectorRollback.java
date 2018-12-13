package ru.job4j.util.database;

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
        Connection result = null;
        try {
            result = ConnectionRollback.create(this.dbConnector.getConnection());
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