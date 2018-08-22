package ru.job4j.music.dao.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.dao.general.DaoOperations;

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
    protected final BasicDataSource pool;
    /**
     * Queries map.
     */
    protected Map<DaoOperations, String> queries;

    /**
     * Constructs new object.
     *
     * @param pool    Connection pool to the storage used.
     * @param queries Map of queries to the storage.
     */
    public AbstractDao(BasicDataSource pool, Map<DaoOperations, String> queries) {
        this.pool = pool;
        this.queries = queries;
    }

    /**
     * Creates new entity in database.
     *
     * @param entity Entity to add.
     * @return Added entity from database.
     */
    @Override
    public abstract E add(E entity);

    /**
     * Returns entity from database by key.
     *
     * @param key Entity identifier.
     * @return Entity from database or empty entity if not found.
     */
    @Override
    public abstract E get(int key);

    /**
     * Updates entity in database.
     *
     * @param key       Key of the entity to update.
     * @param newEntity Entity with updated fields.
     * @return Updated entity from database.
     */
    @Override
    public abstract E update(int key, E newEntity);

    /**
     * Deletes entity from database.
     * <p>
     * DELETE FROM ... WHERE id = ?;
     *
     * @param key Identifier of the entity to delete.
     * @return Deleted entity as it was found in database.
     */
    @Override
    public abstract E delete(int key);

    /**
     * Returns list of all entities.
     *
     * @return List with all entity objects found from database.
     */
    @Override
    public abstract List<E> getAll();
}
