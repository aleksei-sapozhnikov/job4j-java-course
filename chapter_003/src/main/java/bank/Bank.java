package bank;

import bank.exceptions.AlreadyExistsException;
import bank.exceptions.NotEnoughMoneyException;
import bank.exceptions.NotFoundException;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.*;

/**
 * Bank with users and accounts.
 * Can add/delete users, add/delete bank accounts for users, transfer money between accounts.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class Bank {

    /**
     * Users of the bank.
     * Each user can have several accounts.
     */
    private Map<User, List<Account>> users = new HashMap<>();

    /**
     * Add user to bank.
     *
     * @param user to add.
     */
    public void addUser(User user) throws AlreadyExistsException {
        List<Account> result = (this.users.putIfAbsent(user, new ArrayList<>()));
        if (result != null) {
            throw new AlreadyExistsException("Adding user to bank: user already exists.");
        }
    }

    /**
     * Delete user from bank.
     *
     * @param user user to delete.
     */
    public void deleteUser(User user) throws NotFoundException {
        List<Account> result = this.users.remove(user);
        if (result == null) {
            throw new NotFoundException("Removing user from bank: user not found.");
        }
    }

    /**
     * Show all users stored in bank map.
     */
    public Set<User> showAllUsers() {
        return new HashSet<>(users.keySet());
    }

    /**
     * Add new bank accounts for existing user.
     *
     * @param passport passport (identifier) of the user.
     * @param account  account to add.
     */
    public void addAccountToUser(String passport, Account account) {
        Pair<User, List<Account>> pair = this.getUserAndAccounts(passport);
        User user = pair.getKey();
        List<Account> accounts = pair.getValue();
        accounts.add(account);
        this.users.put(user, accounts);
    }

    /**
     * Delete account for the bank user.
     *
     * @param passport passport (identifier) of the user.
     * @param account  account to delete.
     */
    public void deleteAccountFromUser(String passport, Account account) {
        Pair<User, List<Account>> pair = this.getUserAndAccounts(passport);
        User user = pair.getKey();
        List<Account> accounts = pair.getValue();
        accounts.remove(account);
        this.users.put(user, accounts);
    }

    /**
     * Get user and his accounts by passport.
     *
     * @param passport passport (identifier) of the user.
     * @return Pair, where key is the first found corresponding user,
     * value is his accounts list; or null if couldn't find any corresponding user.
     */
    public Pair<User, List<Account>> getUserAndAccounts(String passport) {
        Pair<User, List<Account>> result = null;
        for (Map.Entry<User, List<Account>> entry : this.users.entrySet()) {
            if (passport.equals(entry.getKey().passport())) {
                result = new Pair<>(entry.getKey(), entry.getValue());
                break;
            }
        }
        return result;
    }

    /**
     * Get an account from account list by his requisites.
     *
     * @param accounts   list of accounts.
     * @param requisites requisites of the needed account.
     * @return first found corresponding account, or null if not found any.
     */
    public Account getAccountByRequisites(List<Account> accounts, String requisites) {
        Account result = null;
        for (Account acc : accounts) {
            if (requisites.equals(acc.requisites())) {
                result = acc;
                break;
            }
        }
        return result;
    }

    /**
     * Get an account from account list by his requisites.
     *
     * @param accounts list of accounts.
     * @param oldAcc   account to replace.
     * @param newAcc   replacement account.
     */
    public void replace(List<Account> accounts, Account oldAcc, Account newAcc) {
        accounts.remove(oldAcc);
        accounts.add(newAcc);
    }

    /**
     * Transfer money from one bank account to another.
     * Accounts can belong to the same user or to different users.
     * Checks if accounts exist and if there is enough money to transfer.
     *
     * @param srcPassport   source user identifier.
     * @param srcRequisites source user bank account identifier.
     * @param dstPassport   destination user identifier.
     * @param dstRequisites destination user bank account identifier.
     * @param amount        amount of money to transfer.
     * @return true as transfer was successfully completed.
     */
    public boolean transferMoney(String srcPassport, String srcRequisites, String dstPassport, String dstRequisites, BigDecimal amount)
            throws NotEnoughMoneyException {

        Pair<User, List<Account>> srcPair = this.getUserAndAccounts(srcPassport);
        Pair<User, List<Account>> dstPair = this.getUserAndAccounts(dstPassport);
        User srcUser = srcPair.getKey();
        User dstUser = dstPair.getKey();
        List<Account> srcAccounts = srcPair.getValue();
        List<Account> dstAccounts = dstPair.getValue();
        Account srcAcc = this.getAccountByRequisites(srcAccounts, srcRequisites);
        Account dstAcc = this.getAccountByRequisites(dstAccounts, dstRequisites);

        BigDecimal srcValue = srcAcc.value();
        if (srcValue.compareTo(amount) < 0) {
            throw new NotEnoughMoneyException("Money transfer: not enough money in source to transfer.");
        }
        BigDecimal dstValue = dstAcc.value();
        srcValue = srcValue.subtract(amount);
        dstValue = dstValue.add(amount);

        srcAccounts.remove(srcAcc);
        srcAccounts.add(new Account(srcRequisites, srcValue));
        dstAccounts.remove(dstAcc);
        dstAccounts.add(new Account(dstRequisites, dstValue));
        return true;
    }
}
