package ru.job4j.crud.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletCreate;

public class UserServletCreateCollection extends AbstractUserServletCreate {
    private static final Logger LOG = LogManager.getLogger(UserServletCreateCollection.class);

    public UserServletCreateCollection() {
        super(UserValidatorCollection.getInstance());
    }

}
