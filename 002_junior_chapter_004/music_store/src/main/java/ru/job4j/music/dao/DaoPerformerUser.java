package ru.job4j.music.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.job4j.music.CommonMethods.describeException;
import static ru.job4j.music.User.UserFields.*;
import static ru.job4j.music.dao.DaoOperations.GET_BY_ID;

/**
 * DAO performer working with User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DaoPerformerUser implements DaoPerformer<User> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DaoPerformerUser.class);
    /**
     * Database connection pool.
     */
    private final BasicDataSource connectionPool;
    /**
     * Queries map.
     */
    private Map<DaoOperations, String> queries;

    /**
     * Constructs new object.
     *
     * @param connectionPool Connection pool to the storage used.
     * @param queries        Map of queries to the storage.
     */
    public DaoPerformerUser(BasicDataSource connectionPool, Map<DaoOperations, String> queries) {
        this.connectionPool = connectionPool;
        this.queries = queries;
    }

    /**
     * Gets entity from storage as object.
     *
     * @param key Key to find entity.
     * @return Entity object if found or entity empty object if not found.
     */
    @Override
    public User get(int key) {
        return this.getUserFromDatabase(key);
    }

    @Override
    public User add(User entity) {
        return null;
    }

    @Override
    public User update(User newEntity) {
        return null;
    }

    @Override
    public User delete(int key) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    /**
     * Performs query to a database and returns result as entity object.
     * <p>
     * Query begins as:
     * WITH get_id AS (SELECT ?) ...
     *
     * @param id Id of the entity.
     * @return Entity object found or empty entity object if couldn't find.
     */
    private User getUserFromDatabase(int id) {
        User result = User.EMPTY_USER;
        try (Connection connection = this.connectionPool.getConnection();
             PreparedStatement get = connection.prepareStatement(this.queries.get(GET_BY_ID));
        ) {
            get.setInt(1, id);
            try (ResultSet res = get.executeQuery()) {
                result = this.formUserFromGetByIdQuery(res);
            }
        } catch (SQLException e) {
            LOG.error(describeException(e));
        }
        return result;
    }

    /**
     * Transforms query result set to entity object and returns the object.
     * <p>
     * Query begins as:
     * SELECT
     * users.id AS id,
     * users.login AS login,
     * users.password AS password,
     * roles.name AS role,
     * music.genre AS genre,
     * addresses.name AS address
     *
     * @param res Query result set.
     * @return Entity object found or empty object if couldn't.
     */
    private User formUserFromGetByIdQuery(ResultSet res) {
        User result = User.EMPTY_USER;
        Map<User.UserFields, String> values = new HashMap<>();
        try {
            if (res.next()) {
                int id = res.getInt(1);
                values.put(LOGIN, res.getString(2));
                values.put(PASSWORD, res.getString(3));
                values.put(ROLE, res.getString(4));
                values.put(MUSIC_GENRE, res.getString(5));
                values.put(ADDRESS, res.getString(6));
                result = new User(id, values);
            }
        } catch (SQLException e) {
            LOG.error(describeException(e));
        }
        return result;
    }
}
