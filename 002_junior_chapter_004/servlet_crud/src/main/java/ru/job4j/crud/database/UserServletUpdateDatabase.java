package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletUpdate;

public class UserServletUpdateDatabase extends AbstractUserServletUpdate {
    private static final Logger LOG = LogManager.getLogger(UserServletUpdateDatabase.class);

    public UserServletUpdateDatabase() {
        super(UserValidatorDatabase.getInstance());
    }

}
