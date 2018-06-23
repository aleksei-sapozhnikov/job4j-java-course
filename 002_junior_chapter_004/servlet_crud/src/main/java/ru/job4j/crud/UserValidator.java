package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logic layer for Users store. Validates each object before adding it to storage.
 * <p>
 * Singleton class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserValidator implements Validator<User> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserValidator.class);
    /**
     * Instance field.
     */
    private static UserValidator instance = new UserValidator();

    /**
     * Storage where to add/update/delete users.
     */
    private final Store<User> store = UserStore.getInstance();

    /**
     * Drops all existing tables in the store.
     */
    @Override
    public void clearExistingStructureAndCreateAgain() {
        this.store.clearExistingStructureAndCreateAgain();
    }

    /**
     * Returns this class instance.
     *
     * @return This class instance.
     */
    public static UserValidator getInstance() {
        return instance;
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
        User temp = old != null
                ? this.updateFields(old, upd)
                : null;
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
        String name = upd.getName() != null ? upd.getName() : old.getName();
        String login = upd.getLogin() != null ? upd.getLogin() : old.getLogin();
        String email = upd.getEmail() != null ? upd.getEmail() : old.getEmail();
        return new User(old.getId(), name, login, email, old.getCreated());
    }

    /**
     * Deletes user with given id.
     *
     * @param id Id of the user to delete.
     * @return Deleted object if deleted suucessfull, <tt>null</tt> if not.
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
    public User[] findAll() {
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
                && this.validateName(user.getName())
                && this.validateLogin(user.getLogin())
                && this.validateEmail(user.getEmail());
    }

    /**
     * Validates user name.
     *
     * @param name User name.
     * @return <tt>true</tt> if name is valid, <tt>false</tt> if not.
     */
    private boolean validateName(String name) {
        return name != null;
    }

    /**
     * Validates user login.
     *
     * @param login User login.
     * @return <tt>true</tt> if login is valid, <tt>false</tt> if not.
     */
    private boolean validateLogin(String login) {
        return login != null;
    }

    /**
     * Validates user email.
     *
     * @param email User email.
     * @return <tt>true</tt> if email is valid, <tt>false</tt> if not.
     */
    private boolean validateEmail(String email) {
        return email != null
                && email.contains("@");
    }

}
