package ru.job4j.music.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * General class for all DAO performers, for extension.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public abstract class AbstractDao<E> implements Dao<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractDao.class);
    /**
     * Database connection pool.
     */
    protected final BasicDataSource connectionPool;
    /**
     * Queries map.
     */
    protected Map<DaoPossibleOperations, String> queries;

    /**
     * Constructs new object.
     *
     * @param connectionPool Connection pool to the storage used.
     * @param queries        Map of queries to the storage.
     */
    public AbstractDao(BasicDataSource connectionPool, Map<DaoPossibleOperations, String> queries) {
        this.connectionPool = connectionPool;
        this.queries = queries;
    }


    /**
     * Creates new entity in database.
     *
     * @param entity Entity to add.
     * @return Added entity from database.
     */
    @Override
    public E add(E entity) {
        return null;
    }

    /**
     * Returns entity from database by key.
     *
     * @param key Entity identifier.
     * @return Entity from database or empty entity if not found.
     */
    @Override
    public E get(int key) {
        return null;
    }

    /**
     * Updates entity in database.
     *
     * @param key       Key of the entity to update.
     * @param newEntity Entity with updated fields.
     * @return Updated entity from database.
     */
    @Override
    public E update(int key, E newEntity) {
        return null;
    }

    /**
     * Deletes entity from database.
     *
     * @param key Identifier of the entity to delete.
     * @return Deleted entity as it was found in database.
     */
    @Override
    public E delete(int key) {
        return null;
    }

    /**
     * Returns list of all entities.
     *
     * @return List with all entity objects found from database.
     */
    @Override
    public List<E> getAll() {
        return null;
    }
}
