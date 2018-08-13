package ru.job4j.music;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * TODO: class description
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class UserDao extends GenericDao<User> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserDao.class);

    public UserDao(BasicDataSource connectionPool, Map<DaoFactory.DaoOperations, String> queries) {
        super(connectionPool, queries);
    }

    @Override
    public User add(User entity) throws SQLException {
        try (Connection connection = this.connectionPool.getConnection();
             PreparedStatement add = connection.prepareStatement(this.queries.get(DaoFactory.DaoOperations.ADD));
             PreparedStatement get = connection.prepareStatement(this.queries.get(DaoFactory.DaoOperations.GET_BY_ID))
        ) {
        } catch (SQLException e) {
            LOG.error(CommonMethods.describeException(e));
        }
    }
}
