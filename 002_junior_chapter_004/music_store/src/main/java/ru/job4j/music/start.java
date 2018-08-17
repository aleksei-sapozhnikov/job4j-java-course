package ru.job4j.music;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.music.dao.DaoPool;
import ru.job4j.music.dao.specific.RoleDao;

import java.io.IOException;

/**
 * Just class to run something. Will be deleted after work.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class start {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(start.class);

    public static void main(String[] args) throws IOException {
        DaoPool factory = new DaoPool();
        RoleDao userDao = (RoleDao) factory.getDao(DaoPool.DaoType.USER);
        User res = userDao.get(4);
        System.out.println(res);
    }
}
