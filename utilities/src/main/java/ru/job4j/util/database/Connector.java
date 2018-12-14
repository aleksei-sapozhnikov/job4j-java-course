package ru.job4j.util.database;

import java.sql.Connection;

public interface Connector extends AutoCloseable {
    @Override
    void close() throws Exception;

    Connection getConnection();

    Connection getConnection(boolean commitAtClose);
}
