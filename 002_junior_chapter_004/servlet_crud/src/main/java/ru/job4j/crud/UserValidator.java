package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserValidator implements Validator<User> {
    private static UserValidator instance;
    private static final Logger LOG = LogManager.getLogger(UserValidator.class);
    private final Store<User> store = UserStore.getInstance();

    static {
        instance = new UserValidator();
    }

    public static UserValidator getInstance() {
        return instance;
    }

    @Override
    public int add(User add) {
        return this.validateUser(add)
                ? this.store.add(add)
                : -1;
    }

    @Override
    public boolean update(int id, User upd) {
        boolean result = false;
        User old = this.findById(id);
        User temp = old != null
                ? this.formUser(id, old, upd)
                : null;
        if (temp != null && this.validateUser(temp)) {
            result = this.store.update(temp);
        }
        return result;
    }

    @Override
    public User delete(int id) {
        return this.store.delete(id);
    }

    @Override
    public User[] findAll() {
        return this.store.findAll();
    }

    @Override
    public User findById(int id) {
        return this.store.findById(id);
    }

    private boolean validateUser(User user) {
        return this.validateName(user.getName())
                && this.validateLogin(user.getLogin())
                && this.validateEmail(user.getEmail());
    }

    private boolean validateName(String name) {
        return name != null;
    }

    private boolean validateLogin(String login) {
        return login != null;
    }

    private boolean validateEmail(String email) {
        return email != null
                && email.contains("@");
    }

    private User formUser(int id, User old, User upd) {
        String name = upd.getName() != null ? upd.getName() : old.getName();
        String login = upd.getLogin() != null ? upd.getLogin() : old.getLogin();
        String email = upd.getEmail() != null ? upd.getEmail() : old.getEmail();
        return new User(id, name, login, email, old.getCreated());
    }

}
