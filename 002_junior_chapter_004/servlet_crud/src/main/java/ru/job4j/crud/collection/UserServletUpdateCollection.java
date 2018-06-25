package ru.job4j.crud.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletUpdate;

public class UserServletUpdateCollection extends AbstractUserServletUpdate {
    private static final Logger LOG = LogManager.getLogger(UserServletUpdateCollection.class);

    public UserServletUpdateCollection() {
        super(UserValidatorCollection.getInstance());
    }

}
