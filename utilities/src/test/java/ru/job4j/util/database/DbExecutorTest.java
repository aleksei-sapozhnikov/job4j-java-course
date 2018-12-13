package ru.job4j.util.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import ru.job4j.util.database.DbExecutor.ObjValue;

import java.sql.PreparedStatement;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DbExecutorTest {

    private final Properties properties = this.createProperties();

    private final Properties createProperties() {
        Properties properties = new Properties();
        properties.put("db.driver", "org.postgresql.Driver");
        properties.put("db.type", "postgresql");
        properties.put("db.address", "//localhost:5432");
        properties.put("db.name", "liquid_test");
        properties.put("db.user", "liquid_tester");
        properties.put("db.password", "password");
        return properties;
    }

    private Connector createDbConnectorRollback(Properties properties) {
        return new DbConnectorRollback(new DbConnector(new BasicDataSource(), properties));
    }

    @Test
    public void addThenGetAllUsers() throws Exception {
        List<User> input = Arrays.asList(new User("masha", 15), new User("sasha", 40));
        List<User> result = new ArrayList<>();
        try (Connector connector = this.createDbConnectorRollback(this.properties);
             DbExecutor executor = new DbExecutor(connector.getConnection())) {
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