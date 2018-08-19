package ru.job4j.music.dao.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.dao.general.DaoOperations;

import java.util.List;
import java.util.Map;

/**
 * DAO performer working with User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class UserDao extends AbstractDao<String> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserDao.class);

    public UserDao(BasicDataSource connectionPool, Map<DaoOperations, String> queries) {
        super(connectionPool, queries);
    }

    /**
     * Creates new entity in database.
     *
     * @param entity Entity to add.
     * @return Added entity from database.
     */
    @Override
    public String add(String entity) {
        return null;
    }

    /**
     * Returns entity from database by key.
     *
     * @param key Entity identifier.
     * @return Entity from database or empty entity if not found.
     */
    @Override
    public String get(int key) {
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
    public String update(int key, String newEntity) {
        return null;
    }

    /**
     * Deletes entity from database.
     *
     * @param key Identifier of the entity to delete.
     * @return Deleted entity as it was found in database.
     */
    @Override
    public String delete(int key) {
        return null;
    }

    /**
     * Returns list of all entities.
     *
     * @return List with all entity objects found from database.
     */
    @Override
    public List<String> getAll() {
        return null;
    }
}
