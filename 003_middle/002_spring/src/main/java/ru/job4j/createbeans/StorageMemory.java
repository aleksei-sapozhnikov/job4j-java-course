package ru.job4j.createbeans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.job4j.createbeans.util.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Memory user storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
@Component
public class StorageMemory implements Storage {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(StorageMemory.class);

    private final Map<Long, User> storage = new HashMap<>();

    /**
     * Adds user to database.
     *
     * @param user User to add.
     * @return Id given by storage.
     */
    @Override
    public long add(User user) {
        if (user.getId() != 0) {
            throw new RuntimeException("Adding user with certain id is prohibited");
        }
        var id = this.generateId();
        user.setId(id);
        this.storage.put(user.getId(), user);
        return id;
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

    /**
     * Generates id for added object.
     *
     * @return Generated id.
     */
    private long generateId() {
        return System.currentTimeMillis();
    }
}
