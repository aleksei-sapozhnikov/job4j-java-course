package ru.job4j.vacancies;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CommonMethods {

    public static String inputStreamToString(InputStream in, String charset) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        return out.toString(charset);
    }

    public static Connection getConnectionToDatabase(String type, String address, String name,
                                                     String user, String password)
            throws SQLException {
        String url = String.format("jdbc:%s:%s%s", type, address, "".equals(name) ? "" : "/".concat(name));
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Performs UPDATE operation updating values in database.
     *
     * @param query query script to perform.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public static void dbPerformUpdate(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    public static String loadSqlScript(Object obj, String pkg, String fileName, String charset) throws IOException {
        ClassLoader loader = obj.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(
                String.format("%s/%s", pkg, fileName)
        )) {
            return CommonMethods.inputStreamToString(input, charset);
        }
    }

    public static Properties loadProperties(Object obj, String propFile) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = obj.getClass().getClassLoader();
        try (InputStream input = loader.getResourceAsStream(propFile)) {
            props.load(input);
        }
        return props;
    }

}