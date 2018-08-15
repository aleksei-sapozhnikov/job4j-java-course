package ru.job4j.music.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enumerates operations which Data access object must do.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public enum DaoOperations {
    ADD,
    UPDATE,
    DELETE,
    GET_BY_ID
}
