package ru.job4j.util.methods;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection, which rollback all commits.
 * It is used for integration test.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ConnectionUtils {

    /**
     * Create connection with autocommit=false mode and commit, when the connection is closed.
     *
     * @param connection connection.
     * @return Connection object.
     * @throws SQLException possible exception.
     */
    public static Connection rollbackOnClose(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        return (Connection) Proxy.newProxyInstance(
                ConnectionUtils.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    Object rsl = null;
                    String mName = method.getName();
                    if ("close".equals(mName)) {
                        connection.rollback();
                        connection.close();
                    } else {
                        rsl = method.invoke(connection, args);
                    }
                    return rsl;
                }
        );
    }
}
