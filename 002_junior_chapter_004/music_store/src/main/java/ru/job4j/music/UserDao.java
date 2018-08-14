package ru.job4j.music;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static ru.job4j.music.DaoFactory.DaoOperations.ADD;
import static ru.job4j.music.DaoFactory.DaoOperations.GET_BY_ID;

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
             PreparedStatement add = connection.prepareStatement(this.queries.get(ADD));
             PreparedStatement get = connection.prepareStatement(this.queries.get(GET_BY_ID));
        ) {
            add.executeQuery();
            get.executeQuery();
        } catch (SQLException e) {
            LOG.error(CommonMethods.describeException(e));
        }
        return null;
    }

    private int addUser(User user) {
        try (Connection connection = this.connectionPool.getConnection();
             PreparedStatement userAdd = connection.prepareStatement(this.queries.get(ADD));

        ) {
            // INSERT INTO users (login, password, role_id, address_id) VALUES (?, ?, ?, ?) RETURNING id;
            userAdd.setString(1, user.getLogin());
            userAdd.setString(2, user.getPassword());
            try (ResultSet res = userAdd.executeQuery()) {

            }
        } catch (SQLException e) {
            LOG.error(CommonMethods.describeException(e));
        }
        return 0;
    }

}
