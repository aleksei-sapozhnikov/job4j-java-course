package ru.job4j.storage.user;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * Storage of users.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.04.2018
 */
@ThreadSafe
class UserStorage {
    /**
     * Users stored.
     */
    @GuardedBy("this")
    private Set<User> users = new HashSet<>();

    /**
     * Adds new user to map.
     *
     * @param user user to add.
     * @return <tt>true</tt> if completed successfully, <tt>false</tt> if couldn't add user (already exists).
     */
    synchronized boolean add(User user) {
        return this.users.add(user);
    }

    /**
     * Replaces information about existing user.
     *
     * @param user new user object.
     * @return <tt>true</tt> if user replaced, <tt>false if not</tt>.
     */
    synchronized boolean update(User user) {
        boolean result = this.users.remove(user);
        if (result) {
            this.users.add(user);
        }
        return result;
    }

    /**
     * Deletes user from storage.
     *
     * @param user user to delete.
     * @return <tt>true</tt> if deleted successfully, <tt>false</tt> if not.
     */
    synchronized boolean delete(User user) {
        return this.users.remove(user);
    }

    /**
     * Finds user in storage by id.
     *
     * @param id user id.
     * @return found User of <tt>null</tt> if not found.
     */
    synchronized User findById(final int id) {
        User result = null;
        for (User temp : this.users) {
            if (id == temp.id()) {
                result = temp;
            }
        }
        return result;
    }

    /**
     * Transfers amount from one user to another.
     *
     * @param fromId id of user "from".
     * @param toId   id of user "to".
     * @param amount amount to transfer.
     * @return <tt>true</tt> if transferred, <tt>false</tt> if one of users
     * not found or if "from" user's amount becomes less then 0.
     */
    synchronized boolean transfer(final int fromId, final int toId, final int amount) {
        User from = this.findById(fromId);
        User to = this.findById(toId);
        boolean result = from != null
                && to != null
                && from.amount() >= amount;
        if (result) {
            User newFrom = new User(from.id(), from.amount() - amount);
            User newTo = new User(to.id(), to.amount() + amount);
            result = this.update(newFrom) && this.update(newTo);
        }
        return result;
    }

}
