package ru.job4j.theater.storage.repository.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple account repository based on Set.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class AccountRepositoryHashSet implements AccountRepository {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AccountRepositoryHashSet.class);

    private Set<Account> store = new HashSet<>();

    /**
     * Singleton instance.
     */
    private static final AccountRepository INSTANCE = new AccountRepositoryHashSet();

    /**
     * Constructor.
     */
    private AccountRepositoryHashSet() {
    }

    /**
     * Returns INSTANCE.
     *
     * @return Value of INSTANCE field.
     */
    public static AccountRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Account account) {
        this.store.add(account);
    }

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(this.store);
    }

    @Override
    public Account getByNamePhone(String name, String phone) {
        return this.store.stream()
                .filter(account -> account.getName().equals(name) && account.getPhone().equals(phone))
                .findFirst()
                .orElse(Account.getEmptyAccount());
    }

    /**
     * Clears the repository, removing all objects stored inside.
     */
    @Override
    public void clear() {
        this.store.clear();
    }
}
