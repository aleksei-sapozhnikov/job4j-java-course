package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserStore implements Store<User> {
    private static final Logger LOG = LogManager.getLogger(UserStore.class);

    @Override
    public int add(User model) {
        return 0;
    }

    @Override
    public User update(int id, User newModel) {
        return null;
    }

    @Override
    public User delete(int id) {
        return null;
    }

    @Override
    public User[] findAll() {
        return new User[0];
    }

    @Override
    public User findById(int id) {
        return null;
    }
}
