package ru.job4j.util.database;

import org.junit.Test;
import ru.job4j.util.database.DbExecutor.ObjValue;
import ru.job4j.util.methods.CommonUtils;
import ru.job4j.util.methods.ConnectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DbExecutorTest {

    private final Properties properties =
            CommonUtils.loadProperties(this, "ru/job4j/util/database/test_liquibase.properties");

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.properties.getProperty("db.driver"));
        return ConnectionUtils.rollbackAtClose(
                DriverManager.getConnection(
                        this.properties.getProperty("db.url"),
                        this.properties.getProperty("db.user"),
                        this.properties.getProperty("db.password")
                ));
    }

    @Test
    public void addThenGetAllUsers() throws Exception {
        List<User> input = Arrays.asList(new User("masha", 15), new User("sasha", 40));
        List<User> result = new ArrayList<>();
        try (DbExecutor executor = new DbExecutor(this.getConnection())) {
            for (User user : input) {
                executor.execute(
                        "insert into users (name, age) values (?, ?)",
                        Arrays.asList(user.getName(), user.getAge()),
                        PreparedStatement::execute);
            }
            List<Map<Integer, ObjValue>> resQuery = executor.executeQuery(
                    "select name, age from users",
                    Arrays.asList(String.class, Integer.class)
            ).orElse(new ArrayList<>());
            for (Map<Integer, ObjValue> row : resQuery) {
                result.add(this.formUser(row));
            }
        }
        assertThat(result, is(input));
    }

    /**
     * Simple class for tests.
     */
    private class User {
        private String name;
        private int age;

        private User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return this.name;
        }

        public int getAge() {
            return this.age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User user = (User) o;
            return age == user.age
                    && Objects.equals(name, user.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    private User formUser(Map<Integer, ObjValue> row) {
        int i = 0;
        return new User(
                row.get(++i).asString(),    // name
                row.get(++i).asInteger()    // age
        );
    }
}