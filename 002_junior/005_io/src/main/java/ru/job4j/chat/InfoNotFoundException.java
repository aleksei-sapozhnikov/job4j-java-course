package ru.job4j.chat;

import java.io.IOException;

/**
 * The exception is thrown when information was not found in source.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class InfoNotFoundException extends IOException {

    /**
     * Constructs new instance.
     *
     * @param msg Message.
     */
    public InfoNotFoundException(String msg) {
        super(msg);
    }
}
