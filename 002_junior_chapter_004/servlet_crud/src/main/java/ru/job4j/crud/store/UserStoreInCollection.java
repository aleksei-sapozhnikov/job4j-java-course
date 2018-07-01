package ru.job4j.crud.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Storage for Users. Uses database.
 * <p>
 * Each User is identified by integer id given by
 * the storage as the result of the "add" operation.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UserStoreInCollection implements Store<User> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserStoreInCollection.class);
    /**
     * Instance field.
     */
    private static UserStoreInCollection instance = new UserStoreInCollection();
    /**
     * Inner storage for Users.
     */
    private final ConcurrentHashMap<Integer, User> storage = new ConcurrentHashMap<>();
    /**
     * Next id number to apply to the next user.
     */
    private final AtomicInteger idNumber = new AtomicInteger(1);

    /**
     * Constructs new UserStoreInDatabase object.
     */
    private UserStoreInCollection() {
    }

    /**
     * Returns class instance.
     *
     * @return Class instance.
     */
    public static UserStoreInCollection getInstance() {
        return instance;
    }

    /**
     * Adds new object to the storage.
     *
     * @param model Object to add.
     * @return Unique id given to the object by the
     * storage system or <tt>-1</tt> if couldn't add it.
     * @throws RuntimeException If id generated for a new user already is in the map.
     */
    @Override
    public int add(final User model) {
        final int id = this.idNumber.getAndIncrement();
        User adding = new User(id, model.getName(), model.getLogin(), model.getPassword(), model.getEmail(), model.getCreated());
        this.storage.put(id, adding);
        return id;
    }

    /**
     * Updates object fields.
     *
     * @param newModel object with the same unique id as of the object
     *                 being updated and with new fields values.
     * @return <tt>true</tt> if updated successfully, <tt>false</tt> if not
     * (e.g. object with this id was not found).
     */
    @Override
    public boolean update(final User newModel) {
        boolean result = false;
        final int id = newModel.getId();
        User old = this.storage.get(id);
        if (old != null) {
            User adding = this.formUser(old, newModel);
            this.storage.put(id, adding);
            result = true;
        }
        return result;
    }

    /**
     * Forms user object which is to update existing user.
     * Keeps old user's id and time of creation.
     *
     * @param old Existing user object.
     * @param upd New user object.
     * @return User object as it should be after update.
     */
    private User formUser(User old, User upd) {
        return new User(
                old.getId(),
                upd.getName(),
                upd.getLogin(),
                upd.getEmail(),
                old.getCreated());
    }

    /**
     * Deletes object with given id from the storage.
     *
     * @param id Id of the object to delete.
     * @return Deleted object if found and deleted, <tt>null</tt> if not.
     */
    @Override
    public User delete(final int id) {
        return this.storage.remove(id);
    }

    /**
     * Returns object with given id.
     *
     * @param id Id of the needed object.
     * @return Object with given id or <tt>null</tt> if object not found.
     */
    @Override
    public User findById(final int id) {
        return this.storage.get(id);
    }

    /**
     * Returns array of all objects stored in the storage.
     *
     * @return Array of stored objects.
     */
    @Override
    public List<User> findAll() {
        return new LinkedList<>(storage.values());
    }

    /**
     * Clears currently existing storage structure and creates it again.
     */
    @Override
    public synchronized void clear() {
        this.storage.clear();
        this.idNumber.set(1);
    }

    /**
     * Closes all resources opened by  this store
     */
    @Override
    public void close() {
    }
}
