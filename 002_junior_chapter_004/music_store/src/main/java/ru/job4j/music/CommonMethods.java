package ru.job4j.music;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class with static commonly used methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class CommonMethods {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CommonMethods.class);

    public static String describeException(Exception e) {
        return String.format(
                "%s: %s",
                e.getClass().getName(),
                e.getMessage()
        );
    }
}
