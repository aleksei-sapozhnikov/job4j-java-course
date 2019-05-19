package ru.job4j.createbeans;


import ru.job4j.createbeans.util.User;

/**
 * User storage interface.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface Storage {
    /**
     * Adds user to database.
     *
     * @param user User to add.
     * @return Id given by storage.
     */
    long add(User user);

    /**
     * Returns user by id.
     *
     * @param id Id in storage.
     * @return User ogject.
     */
    User get(long id);

    /**
     * Removes user from storage.
     *
     * @param id Id in storage.
     */
    void remove(long id);
}
