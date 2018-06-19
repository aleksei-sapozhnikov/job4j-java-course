package ru.job4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Common useful methods used in different classes.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */

public class CommonMethods {

    private static CommonMethods INSTANCE = new CommonMethods();

    private CommonMethods() {
    }

    public static CommonMethods getInstance() {
        return INSTANCE;
    }

    /**
     * Converts InputStream object to a String object.
     *
     * @param in      InputStream to convert.
     * @param charset Charset used in the stream.
     * @return Converted string.
     * @throws IOException If bytes could not be read from the stream
     *                     for some reason.
     */
    public String inputStreamToString(InputStream in, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        return out.toString(charset);
    }

    /**
     * Returns jdbc connection to a given database.
     *
     * @param type     database type ("postgresql", "sqlite", etc.)
     * @param address  address ("//localhost:5432", etc.)
     * @param name     database name ("vacancies", "test_db", etc.)
     * @param user     database user ("ivan", "john", etc.)
     * @param password database password ("pass", "slk2j3asdf", etc.)
     * @return Connection to the given database.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public Connection getConnectionToDatabase(String type, String address, String name,
                                              String user, String password)
            throws SQLException {
        String url = String.format("jdbc:%s:%s%s", type, address, "".equals(name) ? "" : "/".concat(name));
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Performs UPDATE operation updating values in database.
     * <p>
     * Useful when no result of the query is needed.
     *
     * @param connection Connection to the database.
     * @param query      Query script to perform.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public int dbPerformUpdate(Connection connection, String query) throws SQLException {
        int rowsUpdated;
        try (Statement statement = connection.createStatement()) {
            rowsUpdated = statement.executeUpdate(query);
        }
        return rowsUpdated;
    }

    /**
     * Loads properties file using ClassLoader.
     *
     * @param obj      Object to create ClassLoader for.
     * @param propFile path to the properties file
     *                 e.g: "ru/job4j/vacancies/main.properties"
     * @return Properties object with values read from file.
     * @throws IOException If problems happened with reading from/to InputStream.
     */
    public Properties loadProperties(Object obj, String propFile) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = obj.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(propFile)) {
            props.load(input);
        }
        return props;
    }

    /**
     * Loads sql script from file to String using ClassLoader.
     *
     * @param obj      Object to create ClassLoader for.
     * @param pkg      package where sql file is situated,
     *                 e.g: "ru/job4j/vacancies"
     * @param fileName name of the sql file,
     *                 e.g.: "create_tables.sql"
     * @param charset  charset to use when converting InputStream to String.
     * @return String containing query.
     * @throws IOException If problems happened with reading from/to InputStream.
     */
    public String loadFileAsString(Object obj, String pkg, String fileName, String charset) throws IOException {
        ClassLoader loader = obj.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(
                String.format("%s/%s", pkg, fileName)
        )) {
            return this.inputStreamToString(input, charset);
        }
    }

}