package ru.job4j.streams.bank;

import ru.job4j.streams.bank.exceptions.AlreadyExistsException;
import ru.job4j.streams.bank.exceptions.NotFoundException;
import ru.job4j.streams.bank.exceptions.UnablePerformOperationException;

import java.math.BigDecimal;
import java.util.*;

/**
 * Bank with users and accounts.
 * Can add/delete users, add/delete ru.job4j.bank accounts for users, transfer money between accounts.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class Bank {

    /**
     * Users of the ru.job4j.bank.
     * Each user can have several accounts.
     */
    private Map<User, Set<Account>> users = new HashMap<>();

    /**
     * Add user to bank.
     *
     * @param user to add.
     */
    public void addUser(User user) throws AlreadyExistsException {
        Set<Account> result = (this.users.putIfAbsent(user, new HashSet<>()));
        if (result != null) {
            throw new AlreadyExistsException("Adding user to ru.job4j.bank: user already exists.");
        }
    }

    /**
     * Delete user from ru.job4j.bank.
     *
     * @param user user to delete.
     */
    public void deleteUser(User user) throws NotFoundException {
        Set<Account> result = this.users.remove(user);
        if (result == null) {
            throw new NotFoundException("Removing user from ru.job4j.bank: user not found.");
        }
    }

    /**
     * Show all users stored in ru.job4j.bank map.
     */
    public Set<User> showAllUsers() {
        return this.users.keySet();
    }

    /**
     * Find user by his passport.
     *
     * @param passport user passport (identifier).
     * @return first corresponding user found or <tt>null</tt>, if not found any.
     */
    private User getUserByPassport(String passport) {
        Optional<User> result = this.users.keySet().stream()
                .filter(user -> passport.equals(user.passport()))
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Add new ru.job4j.bank accounts for existing user.
     *
     * @param passport passport (identifier) of the user.
     * @param account  account to add.
     */
    public void addAccountToUser(String passport, Account account) throws AlreadyExistsException {
        Set<Account> accounts = this.getUserAccounts(passport);
        boolean result = accounts.add(account);
        if (!result) {
            throw new AlreadyExistsException("Adding account: account already exists for this user.");
        }
    }

    /**
     * Delete account for the ru.job4j.bank user.
     *
     * @param passport passport (identifier) of the user.
     * @param account  account to delete.
     */
    public void deleteAccountFromUser(String passport, Account account) throws NotFoundException {
        Set<Account> accounts = this.getUserAccounts(passport);
        if (accounts == null) {
            throw new NotFoundException("Deleting account: user not found.");
        }
        boolean result = accounts.remove(account);
        if (!result) {
            throw new NotFoundException("Deleting account: account not found.");
        }
    }

    /**
     * Get list of user accounts by his passport.
     *
     * @param passport passport (identifier) of the user.
     * @return the first found corresponding user, or null if such user not found.
     */
    public Set<Account> getUserAccounts(String passport) {
        User user = this.getUserByPassport(passport);
        return this.users.get(user);
    }

    /**
     * Get account from list by requisites.
     *
     * @param requisites account requisites.
     * @return the first found corresponding account, or <tt>null</tt> if such account not found.
     */
    public Account getAccountByRequisites(Set<Account> accounts, String requisites) {
        Optional<Account> result = accounts.stream()
                .filter(account -> requisites.equals(account.requisites()))
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Transfer money from one ru.job4j.bank account to another.
     * Accounts can belong to the same user or to different users.
     * Checks if accounts exist and if there is enough money to transfer.
     *
     * @param srcPassport   source user identifier.
     * @param srcRequisites source user ru.job4j.bank account identifier.
     * @param dstPassport   destination user identifier.
     * @param dstRequisites destination user ru.job4j.bank account identifier.
     * @param amount        amount of money to transfer.
     * @return true as transfer was successfully completed, false if couldn't perform transfer.
     */
    public boolean transferMoney(String srcPassport, String srcRequisites, String dstPassport, String dstRequisites, BigDecimal amount) {
        try {
            if (srcPassport.equals(dstPassport) && srcRequisites.equals(dstRequisites)) {
                throw new UnablePerformOperationException("Unable to transfer money: source and destination accounts are the same.");
            }
            Account srcAcc = this.getAccountByRequisites(this.getUserAccounts(srcPassport), srcRequisites);
            Account dstAcc = this.getAccountByRequisites(this.getUserAccounts(dstPassport), dstRequisites);
            srcAcc.transferToIfEnough(dstAcc, amount);
            return true;
        } catch (UnablePerformOperationException upoe) {
            return false;
        }
    }
}
