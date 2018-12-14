package ru.job4j.util.methods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class, contains common-used methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class CommonUtils {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CommonUtils.class);

    /**
     * Loads properties file using ClassLoader.
     *
     * @param obj  Object where you try to load properties (needed to get its class).
     * @param file path to the properties file
     *             e.g: "ru/job4j/vacancies/main.properties"
     * @return Properties object with values read from file.
     */
    public static Properties loadProperties(Object obj, String file) {
        Properties props = new Properties();
        ClassLoader loader = obj.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(file)) {
            props.load(input);
        } catch (IOException e) {
            LOG.error(CommonUtils.describeThrowable(e));
        }
        return props;
    }

    public static String describeThrowable(Throwable e) {
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement el : e.getStackTrace()) {
            trace.append(String.format(
                    "    %s.%s\n", el.getClassName(), el.getMethodName()
            ));
        }
        return String.format("%s: '%s'. Stacktrace:\n%s", e.getClass().getName(), e.getMessage(), trace.toString());
    }

    /**
     * Loads sql script from file to String using ClassLoader.
     *
     * @param obj     Object to create ClassLoader for.
     * @param charset charset to use when converting InputStream to String, e.g: "UTF-8"
     * @param path    path to file, e.g: "ru/job4j/vacancies/create_tables.sql"
     * @return String containing query.
     */
    public static String loadFileAsString(Object obj, String charset, String path) {
        String result = "";
        ClassLoader loader = obj.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(path)) {
            if (input != null) {
                result = CommonUtils.inputStreamToString(input, charset);
            }
        } catch (IOException e) {
            LOG.error(CommonUtils.describeThrowable(e));
        }
        return result;
    }

    /**
     * Converts InputStream object to a String object.
     *
     * @param in      InputStream to convert.
     * @param charset Charset used in the stream, e.g: "UTF-8"
     * @return Converted string.
     * @throws IOException If bytes could not be read from the stream
     *                     for some reason.
     */
    public static String inputStreamToString(InputStream in, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        return out.toString(charset);
    }
}
