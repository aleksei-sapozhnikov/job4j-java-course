package ru.job4j.createbeans;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.job4j.createbeans.util.Connector;
import ru.job4j.createbeans.util.JdbcParams;
import ru.job4j.createbeans.util.User;
import ru.job4j.util.methods.ConnectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;


public class MetaStorageTest {
    /**
     * Context based on XML, with explicit component definition.
     */
    private ApplicationContext xmlContext = new ClassPathXmlApplicationContext("spring-context-xml.xml");
    /**
     * Context based on Annotations, using automatic component scan.
     */
    private ApplicationContext annContext = new ClassPathXmlApplicationContext("spring-context-annotations.xml");

    ////////////////////////////
    // XML-based config
    ////////////////////////////

    @Test
    public void xmlWhenLoadContextThenGeObject() {
        this.whenLoadContextThenGeObject(this.xmlContext);
    }

    @Test
    public void xmlWhenAddUserThenGetUserById() {
        this.whenAddUserThenGetUserById(this.xmlContext);
    }

    @Test
    public void xmlWhenRemoveThenFoundNull() {
        this.whenRemoveThenFoundNull(this.xmlContext);
    }

    ////////////////////////////
    // ANNOTATION-based config
    ////////////////////////////

    @Test
    public void annWhenLoadContextThenGeObject() {
        this.whenLoadContextThenGeObject(this.annContext);
    }

    @Test
    public void annWhenAddUserThenGetUserById() throws SQLException {
        this.whenAddUserThenGetUserById(this.annContext);
    }

    @Test
    public void annWhenRemoveThenFoundNull() {
        this.whenRemoveThenFoundNull(this.annContext);
    }

    ////////////////////////////
    // test methods
    ////////////////////////////

    private void whenLoadContextThenGeObject(ApplicationContext context) {
        StorageMemory storageMemory = context.getBean(StorageMemory.class);
        assertNotNull(storageMemory);
        MetaStorage metaStorage = context.getBean(MetaStorage.class);
        assertNotNull(metaStorage);
        JdbcParams jdbcParams = context.getBean(JdbcParams.class);
        assertNotNull(jdbcParams);
    }

    private void whenAddUserThenGetUserById(ApplicationContext context) {
        // memory
        var mem = new StorageMemory();
        this.whenAddUserThenGetUserById(mem);
        // jdbc
        var connector = this.getRollbackConnector(context);
        var jdbc = new StorageJdbc(connector);
        this.whenAddUserThenGetUserById(jdbc);
    }

    private void whenAddUserThenGetUserById(Storage inner) {
        var storage = new MetaStorage(inner);
        var added = new User("user");
        long id = storage.add(added);
        var found = storage.get(id);
        assertEquals(added.getId(), found.getId());
        assertEquals(added.getName(), found.getName());
    }

    private void whenRemoveThenFoundNull(ApplicationContext context) {
        // memory
        var mem = new StorageMemory();
        this.whenRemoveThenFoundNull(mem);
        // jdbc
        var connector = this.getRollbackConnector(context);
        var jdbc = new StorageJdbc(connector);
        this.whenRemoveThenFoundNull(jdbc);
    }

    private void whenRemoveThenFoundNull(Storage inner) {
        var storage = new MetaStorage(inner);
        var added = new User("user");
        long id = storage.add(added);
        assertNotNull(storage.get(id));
        storage.remove(id);
        assertNull(storage.get(id));
    }

    ////////////////////////////
    // util methods
    ////////////////////////////

    /**
     * Returns proxy Connection object for tests.
     *
     * @param context Application context.
     * @return Connector making proxy connections with rollback.
     */
    private Connector getRollbackConnector(ApplicationContext context) {
        try {
            var jdbcParams = context.getBean(JdbcParams.class);
            return new Connector(jdbcParams) {
                @Override
                public Connection getConnection() {
                    try {
                        return ConnectionUtils.rollbackOnClose(
                                DriverManager.getConnection(
                                        jdbcParams.getDbUrl(),
                                        jdbcParams.getDbUser(),
                                        jdbcParams.getDbPassword()
                                ));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException("problems getting rollback connector");
                    }
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("problems getting connector");
        }
    }
}