package ru.job4j.json;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: description
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Storage {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Storage.class);
    private final ConcurrentHashMap<Human, Object> store = new ConcurrentHashMap<>();
    private final Object dummy = new Object();

    public void add(Human human) {
        this.store.put(human, this.dummy);
    }

}
