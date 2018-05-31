package ru.job4j.tracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * Storage of items : tasks, messages, etc.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.01.2018
 */
public class Tracker implements AutoCloseable {
    private static final Path WORK_DIR = Paths.get("002_junior_chapter_003").toAbsolutePath();
    private static final Path PACKAGE_DIR = Paths.get("ru", "job4j", "tracker");
    private static final Path RESOURCE_DIR = Paths.get(
            WORK_DIR.toString(), "src", "main", "resources", PACKAGE_DIR.toString()
    );
    private final Properties PROPERTIES;
    private final Connection connection;

    // Tracker должен работать с базой данных:
    // 0) подключаться к ней
    // 1) создавать начальную структуру, если надо
    // 2) добавлять в нее данные методом add, изменять методом replace и удалять методом delete
    // 3) а также возвращать их по id

    // ========================== Пустой конструктор просто для того, чтобы не мешались старые тесты =============
    public Tracker() {
        this.PROPERTIES = null;
        this.connection = null;
    }
    // ============================ Пссле завершения работы над трекером - его быть не должно=====================

    public Tracker(String propertiesFileName) throws SQLException, IOException {
        this.PROPERTIES = new Properties();
        this.PROPERTIES.load(new FileInputStream(Paths.get(RESOURCE_DIR.toString(), propertiesFileName).toString()));
        this.connection = this.getConnection();
        this.dbPerformUpdate(
                Paths.get(RESOURCE_DIR.toString(), this.PROPERTIES.getProperty("sql_create_structure"))
        );
    }

    public static void main(String[] args) throws IOException, SQLException {
        try (Tracker tracker = new Tracker("tracker.properties")) {
            tracker.add(new Item("test3", "test_desc", System.currentTimeMillis()));
        }
    }

    private void dbPerformUpdate(Path sqlFile) throws SQLException, IOException {
        try (Statement statement = this.connection.createStatement()) {
            String query = new String(Files.readAllBytes(sqlFile));
            statement.executeUpdate(query);
        }
    }

    private void dbPerformUpdate(String query) throws SQLException, IOException {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    private Connection getConnection() throws IOException, SQLException {
        String url = this.PROPERTIES.getProperty("db_url");
        String user = this.PROPERTIES.getProperty("db_user");
        String password = this.PROPERTIES.getProperty("db_password");
        return DriverManager.getConnection(url, user, password);
    }


    /**
     * Array containing items : tasks, messages etc.
     */
    private List<Item> items = new ArrayList<>();

    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    /**
     * Add new item to database.
     * Adds unique id given by database to the item's id field.
     *
     * @param item Item to add.
     */
    public Item add(Item item) throws SQLException {
        int id;
        String query = String.format(
                "INSERT INTO items (name, description, create_time) VALUES (\'%s\', \'%s\', \'%s\') RETURNING id",
                item.getName(), item.getDescription(), new java.sql.Timestamp(item.getCreateTime()).toString()
        );
        try (ResultSet result = this.connection.createStatement().executeQuery(query)) {
            result.next();
            id = result.getInt(1);
        }
        item.setId(Integer.toString(id));
        return item;
    }

    /**
     * Replace item with given item.
     *
     * @param id   Id of the item to replace.
     * @param item Item to store with the given id.
     */
    public void replace(String id, Item item) throws SQLException {
        this.items.set(
                this.items.indexOf(this.findById(id)), item
        );
    }

    /**
     * Delete item with the given id.
     *
     * @param id Id of the item to delete.
     */
    public void delete(String id) throws SQLException {
        this.items.remove(
                this.findById(id)
        );
    }

    /**
     * Returns Item with the given Id.
     *
     * @param id Id given.
     * @return Item with this id.
     */
    public Item findById(String id) throws SQLException {
        String queryItem = String.format(
                "SELECT id, name, description, create_date) FROM items WHERE id = %s",
                id
        );
        String queryComments = String.format(
                "SELECT text FROM comments WHERE item_id = %s",
                id
        );
        boolean found = false;
        Item item = null;
        try (ResultSet r = this.connection.createStatement().executeQuery(queryItem)) {
            if (r.next()) {
                found = true;
                item = new Item(
                        Integer.toString(r.getInt("id")), r.getString("name"),
                        r.getString("description"), r.getTimestamp("create_time").getTime()
                );
            }
        }
        if (found) {
            try (ResultSet r = this.connection.createStatement().executeQuery(queryComments)) {
                List<String> comm = new ArrayList<>();
                while (r.next()) {
                    comm.add(r.getString("text"));
                }
                item.addComments(comm);
            }
        }
        if (found) {
            return item;
        } else {
            throw new NoSuchIdException("Item with such id not found");
        }
    }

    /**
     * Return all items whose "name" field is equal to given name.
     *
     * @param name Items with this name will be returned.
     * @return Array of Items satisfying the condition.
     */
    public List<Item> findByName(String name) {
        List<Item> result = new ArrayList<>();
        for (Item temp : this.items) {
            if (name.equals(temp.getName())) {
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * Return all Items stored now.
     *
     * @return Array of Items stored in tracker (without null elements).
     */
    public List<Item> findAll() {
        return Collections.unmodifiableList(this.items);
    }

    /**
     * Generate unique id for a new Item.Метод генерирует уникальный ключ для заявки.
     *
     * @return Unique id.
     */
    private String generateId() {
        return String.valueOf(System.currentTimeMillis() + (new Random()).nextInt());
    }


}
