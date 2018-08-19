package ru.job4j.music.dao.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.StaticMethods;
import ru.job4j.music.dao.general.DaoOperations;
import ru.job4j.music.entities.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * DAO performer working with User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class AddressDao extends AbstractDao<Address> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AddressDao.class);

    public AddressDao(BasicDataSource pool, Map<DaoOperations, String> queries) {
        super(pool, queries);
    }

    /**
     * Creates new entity in database.
     * <p>
     * INSERT INTO addresses (name) VALUES (?) RETURNING id;
     *
     * @param entity Entity to add.
     * @return Added entity from database.
     */
    @Override
    public Address add(Address entity) {
        int id = -1;
        String qAdd = this.queries.get(DaoOperations.ADD);
        try (Connection connection = this.pool.getConnection();
             PreparedStatement stAdd = connection.prepareStatement(qAdd)
        ) {
            stAdd.setString(1, entity.getName());
            try (ResultSet res = stAdd.executeQuery()) {
                if (res.next()) {
                    id = res.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
        return this.get(id);
    }

    /**
     * Returns entity from database by key.
     * <p>
     * SELECT id, name FROM addresses WHERE id = ?;
     *
     * @param key Entity identifier.
     * @return Entity from database or empty entity if not found.
     */
    @Override
    public Address get(int key) {
        Address result = Address.EMPTY_ADDRESS;
        String qGet = this.queries.get(DaoOperations.GET_BY_ID);
        try (Connection connection = this.pool.getConnection();
             PreparedStatement stGet = connection.prepareStatement(qGet)
        ) {
            stGet.setInt(1, key);
            try (ResultSet res = stGet.executeQuery()) {
                if (res.next()) {
                    result = new Address(
                            res.getInt(1),
                            res.getString(2)
                    );
                }
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
        return result;
    }

    /**
     * Updates entity in database.
     * <p>
     * UPDATE addresses SET name = ? WHERE id = ?;
     *
     * @param key       Key of the entity to update.
     * @param newEntity Entity with updated fields.
     * @return Updated entity from database.
     */
    @Override
    public Address update(int key, Address newEntity) {
        Address result = Address.EMPTY_ADDRESS;
        String qUpdate = this.queries.get(DaoOperations.GET_BY_ID);
        try (Connection connection = this.pool.getConnection();
             PreparedStatement stUpdate = connection.prepareStatement(qUpdate)
        ) {
            stUpdate.setString(1, newEntity.getName());
            stUpdate.setInt(2, key);
            int changed = stUpdate.executeUpdate();
            if (changed == 1) {
                result = this.get(key);
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
        return result;
    }

    /**
     * Deletes entity from database.
     *
     * @param key Identifier of the entity to delete.
     * @return Deleted entity as it was found in database.
     */
    @Override
    public Address delete(int key) {
        Address temp = this.get(key);
        boolean deleted = false;
        String qDelete = this.queries.get(DaoOperations.GET_BY_ID);
        try (Connection connection = this.pool.getConnection();
             PreparedStatement stDelete = connection.prepareStatement(qDelete)
        ) {
            stDelete.setInt(1, key);
            int changed = stDelete.executeUpdate();
            if (changed == 1) {
                deleted = true;
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
        return deleted ? temp : Address.EMPTY_ADDRESS;
    }

    /**
     * Returns list of all entities.
     * <p>
     * SELECT id, name FROM addresses;
     *
     * @return List with all entity objects found from database.
     */
    @Override
    public List<Address> getAll() {
        List<Address> result = new ArrayList<>();
        String qGetAll = this.queries.get(DaoOperations.GET_ALL);
        try (Connection connection = this.pool.getConnection();
             PreparedStatement stGetAll = connection.prepareStatement(qGetAll);
             ResultSet res = stGetAll.executeQuery()
        ) {
            while (res.next()) {
                result.add(
                        new Address(
                                res.getInt(1),
                                res.getString(2))
                );
            }
        } catch (SQLException e) {
            LOG.error(StaticMethods.describeException(e));
        }
        return result;
    }
}
