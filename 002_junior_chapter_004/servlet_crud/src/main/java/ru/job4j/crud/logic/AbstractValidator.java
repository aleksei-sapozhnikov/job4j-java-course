package ru.job4j.crud.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.Role;
import ru.job4j.crud.model.User;
import ru.job4j.crud.store.Store;

import java.util.List;

import static ru.job4j.crud.model.Info.Fields.EMAIL;
import static ru.job4j.crud.model.Info.Fields.NAME;
import static ru.job4j.crud.model.Role.ADMIN;

/**
 * General class for a logic layer.
 * <p>
 * Validates each object before adding it to storage.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public abstract class AbstractValidator implements Validator<User> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractValidator.class);
    /**
     * Store where users are held.
     */
    private final Store<User> store;

    /**
     * Constructor initiating needed fields.
     *
     * @param store Memory layer class object.
     */
    protected AbstractValidator(Store<User> store) {
        this.store = store;
        User root = new User(new Credentials("root", "root", ADMIN), new Info("root_name", "root@email.com", "root_country", "root_city"));
        this.store.add(root);
    }

    /**
     * Validates and adds new user to store.
     *
     * @return Unique id given to the object by the
     * storage system or <tt>-1</tt> if object is not
     * valid or if store couldn't add it.
     */
    @Override
    public int add(User add) {
        return this.validateUser(add)
                ? this.store.add(add)
                : -1;
    }

    /**
     * Tries to update user with given id using new fields
     * values from the given user. If the result is valid,
     * updates object in the storage, otherwise leaves it untouched.
     *
     * @param id  Id of the object to update.
     * @param upd User object with new fields values.
     * @return <tt>true</tt> if the object was updated, <tt>false</tt> if not.
     */
    @Override
    public boolean update(int id, User upd) {
        boolean result = false;
        User old = this.findById(id);
        User temp = old != null ? this.updateFields(old, upd) : null;
        if (this.validateUser(temp)) {
            result = this.store.update(temp);
        }
        return result;
    }

    /**
     * Forms user object combining existing user fields with "updating" user fields.
     * If updating field is not null, then the updating field is taken. Otherwise,
     * the existing field value is taken.
     *
     * @param old Existing user.
     * @param upd User with u[dated fields.
     * @return User object with updated fields.
     */
    private User updateFields(User old, User upd) {
        Credentials credentials = old.getCredentials().mergeWith(upd.getCredentials());
        Info info = old.getInfo().mergeWith(upd.getInfo());
        return new User(old.getId(), old.getCreated(), credentials, info);
    }

    /**
     * Deletes user with given id.
     *
     * @param id Id of the user to delete.
     * @return Deleted object if deleted successful, <tt>null</tt> if not.
     */
    @Override
    public User delete(int id) {
        return this.store.delete(id);
    }

    /**
     * Returns array of all users stored now.
     *
     * @return Array of users stored now.
     */
    @Override
    public List<User> findAll() {
        return this.store.findAll();
    }

    /**
     * Finds user in the storage by id.
     *
     * @param id Id of the needed user.
     * @return User with given id if found, <tt>null</tt> if not found.
     */
    @Override
    public User findById(int id) {
        return this.store.findById(id);
    }

    /**
     * Validates user object before adding it to the store.
     *
     * @param user User object.
     * @return <tt>true</tt> if object is valid, <tt>false</tt> if not.
     */
    private boolean validateUser(User user) {
        return user != null
                && this.validateLogin(user.getCredentials().getLogin())
                && this.validatePassword(user.getCredentials().getPassword())
                && this.validateRole(user.getCredentials().getRole())
                && this.validateName(user.getInfo().getField(NAME))
                && this.validateEmail(user.getInfo().getField(EMAIL));

    }

    /**
     * Validates user name.
     *
     * @param name User name.
     * @return <tt>true</tt> if name is valid, <tt>false</tt> if not.
     */
    private boolean validateName(String name) {
        return name != null
                && !name.equals("");
    }

    /**
     * Validates user login.
     *
     * @param login User login.
     * @return <tt>true</tt> if login is valid, <tt>false</tt> if not.
     */
    private boolean validateLogin(String login) {
        return login != null
                && !login.equals("");
    }

    /**
     * Validates user password.
     *
     * @param password User password.
     * @return <tt>true</tt> if email is valid, <tt>false</tt> if not.
     */
    private boolean validatePassword(String password) {
        return password != null
                && !password.equals("");
    }

    /**
     * Validates user email.
     *
     * @param email User email.
     * @return <tt>true</tt> if email is valid, <tt>false</tt> if not.
     */
    private boolean validateEmail(String email) {
        return email != null
                && !email.equals("")
                && email.contains("@");
    }

    /**
     * Validates user role.
     *
     * @param role User role/
     * @return <tt>true</tt> if role is valid, <tt>false</tt> if not.
     */
    private boolean validateRole(Role role) {
        return role != null;
    }

    /**
     * Drops all existing tables in the store and creates structure again.
     */
    @Override
    public void clear() {
        this.store.clear();
    }

    /**
     * Closes all resources opened by this store.
     *
     * @throws Exception If problems while closing occurred.
     */
    @Override
    public void close() throws Exception {
        this.store.close();
    }

    /**
     * Returns user by login and password.
     *
     * @param login    User login.
     * @param password User password.
     * @return User object with given login and password or <tt>null</tt> if user not found.
     */
    @Override
    public User findByCredentials(String login, String password) {
        User result = null;
        for (User user : this.findAll()) {
            if (login.equals(user.getCredentials().getLogin())
                    && password.equals(user.getCredentials().getPassword())
                    ) {
                result = user;
                break;
            }
        }
        return result;
    }
}
