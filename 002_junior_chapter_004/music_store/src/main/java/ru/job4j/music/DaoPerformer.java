package ru.job4j.music;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.DaoFactory.DaoOperations;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DaoPerformer<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DaoPerformer.class);
    /**
     * Database connection pool.
     */
    private final BasicDataSource connectionPool;
    /**
     * private final
     */
    private final Map<DaoOperations, String> queries;

    public DaoPerformer(BasicDataSource connectionPool, Map<DaoOperations, String> queries) {
        this.connectionPool = connectionPool;
        this.queries = queries;
    }

    /**
     * Creates new entity in database.
     */
    public E add(E entity) throws SQLException {
        return null;
    }

    /**
     * Returns entity from database by key.
     */
    public E get(int key) {
        return null;
    }

    /**
     * Updates entity in database.
     */
    public E update(E newEntity) {
        return null;
    }

    /**
     * Deletes entity from database.
     */
    public E delete(int key) {
        return null;
    }

    /**
     * Returns list of all entities.
     */
    public List<E> getAll() {
        return null;
    }

}
