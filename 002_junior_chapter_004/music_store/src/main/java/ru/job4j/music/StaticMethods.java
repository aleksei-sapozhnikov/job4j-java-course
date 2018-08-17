package ru.job4j.music;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class with static commonly used methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class StaticMethods {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(StaticMethods.class);

    /**
     * Returns string describing exception (for logger).
     *
     * @param e Exception.
     * @return String for logger.
     */
    public static String describeException(Exception e) {
        return String.format(
                "%s: %s",
                e.getClass().getName(),
                e.getMessage()
        );
    }

    /**
     * Loads properties file using ClassLoader.
     *
     * @param propFile path to the properties file
     *                 e.g: "ru/job4j/vacancies/main.properties"
     * @return Properties object with values read from file.
     */
    public static Properties loadProperties(String propFile, Class objClass) {
        Properties props = new Properties();
        ClassLoader loader = objClass.getClassLoader();
        try (InputStream input = loader.getResourceAsStream(propFile)) {
            props.load(input);
        } catch (IOException e) {
            LOG.error(StaticMethods.describeException(e));
        }
        return props;
    }
}
