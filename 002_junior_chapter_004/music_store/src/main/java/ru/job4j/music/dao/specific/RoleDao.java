package ru.job4j.music.dao.specific;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.dao.AbstractDao;
import ru.job4j.music.dao.DaoOperations;

import java.util.Map;

/**
 * DAO performer working with User class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class RoleDao extends AbstractDao<String> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(RoleDao.class);

    public RoleDao(BasicDataSource connectionPool, Map<DaoOperations, String> queries) {
        super(connectionPool, queries);
    }
}
