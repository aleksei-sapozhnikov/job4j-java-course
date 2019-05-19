package ru.job4j.createbeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.job4j.createbeans.util.User;

/**
 * User meta-storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
@Component
public class MetaStorage implements Storage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(MetaStorage.class);
    /**
     * Inner storage.
     */
    private final Storage storage;

    /**
     * Creates new user storage.
     *
     * @param storage Inner storage implementation.
     */
    @Autowired
    public MetaStorage(Storage storage) {
        this.storage = storage;
    }


    /**
     * Adds user to database.
     *
     * @param user User to add.
     * @return Id given by storage.
     */
    @Override
    public long add(User user) {
        return this.storage.add(user);
    }

    /**
     * Returns user by id.
     *
     * @param id Id in storage.
     * @return User ogject.
     */
    @Override
    public User get(long id) {
        return this.storage.get(id);
    }

    /**
     * Removes user from storage.
     *
     * @param id Id in storage.
     */
    @Override
    public void remove(long id) {
        this.storage.remove(id);
    }
}
