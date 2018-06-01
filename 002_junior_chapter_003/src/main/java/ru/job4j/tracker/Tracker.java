package ru.job4j.tracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

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
    private final Properties trackerProperties;
    private final Connection connection;

    // Tracker должен работать с базой данных:
    // 0) подключаться к ней
    // 1) создавать начальную структуру, если надо
    // 2) добавлять в нее данные методом add, изменять методом replace и удалять методом delete
    // 3) а также возвращать их по id

    // ========================== Пустой конструктор просто для того, чтобы не мешались старые тесты =============
    public Tracker() {
        this.trackerProperties = null;
        this.connection = null;
    }
    // ============================ Пссле завершения работы над трекером - его быть не должно=====================

    public Tracker(String propertiesFileName) throws SQLException, IOException {
        this(propertiesFileName, false);
    }

    public Tracker(String propertiesFileName, boolean fillInitialValues) throws SQLException, IOException {
        this.trackerProperties = new Properties();
        this.trackerProperties.load(new FileInputStream(Paths.get(RESOURCE_DIR.toString(), propertiesFileName).toString()));
        this.connection = this.getConnectionToDatabase();
        this.dbPerformUpdate(
                Paths.get(RESOURCE_DIR.toString(), this.trackerProperties.getProperty("sql_create_tables"))
        );
        if (fillInitialValues) {
            this.dbPerformUpdate(
                    Paths.get(RESOURCE_DIR.toString(), this.trackerProperties.getProperty("sql_initial_values"))
            );
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

    public static void main(String[] args) throws IOException, SQLException {
        try (Tracker tracker = new Tracker("tracker.properties")) {

            Item added = tracker.add(new Item("added_name", "added_desc", System.currentTimeMillis(),
                    new String[]{"comment_1", "comment_2", "comment_3"})
            );
            String addedId = added.getId();
            Item replacer = new Item("rep_name", "rep_desc", System.currentTimeMillis(), new String[]{"rep_comm_1", "rep_comm_8"});

            System.out.println("Adding item: " + added);
            System.out.println("Replacing by item: " + replacer);

            System.out.println();
            System.out.println("Item found after ADD: " + tracker.findById(addedId));

            System.out.println();
            tracker.replace(addedId, replacer);
            System.out.println("Item found after REPLACE: " + tracker.findById(addedId));

            System.out.println();
            tracker.delete(addedId);
            System.out.println("Item found after DELETE: " + tracker.findById(addedId));
        }
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

    private Connection getConnectionToDatabase() throws SQLException {
        String url = this.trackerProperties.getProperty("db_url");
        String user = this.trackerProperties.getProperty("db_user");
        String password = this.trackerProperties.getProperty("db_password");
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Add new item to database.
     * Adds unique id given by database to the item's id field.
     *
     * @param item Item to add.
     */
    public Item add(Item item) throws SQLException, IOException {
        int id;
        String queryItems = String.format(
                "INSERT INTO items (name, description, create_time) VALUES (\'%s\', \'%s\', \'%s\') RETURNING id",
                item.getName(), item.getDescription(), new java.sql.Timestamp(item.getCreateTime()).toString()
        );
        try (ResultSet result = this.connection.createStatement().executeQuery(queryItems)) {
            result.next();
            id = result.getInt("id");
        }
        for (String text : item.getComments()) {
            String queryCommentsAdd = String.format(
                    "INSERT INTO comments (item_id, text) VALUES (%s, \'%s\')",
                    id, text
            );
            this.dbPerformUpdate(queryCommentsAdd);
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
    public void replace(String id, Item item) throws SQLException, IOException {
        String queryItemsAdd = String.format(
                "UPDATE items SET name = \'%s\', description = \'%s\', create_time = \'%s\' WHERE id = %s",
                item.getName(), item.getDescription(), new java.sql.Timestamp(item.getCreateTime()).toString(),
                id
        );
        String queryCommentsClear = String.format(
                "DELETE FROM comments WHERE item_id = %s",
                id
        );
        this.dbPerformUpdate(queryCommentsClear);
        for (String text : item.getComments()) {
            String queryCommentsAdd = String.format(
                    "INSERT INTO comments (item_id, text) VALUES (%s, \'%s\')",
                    id, text
            );
            this.dbPerformUpdate(queryCommentsAdd);
        }
        this.dbPerformUpdate(queryItemsAdd);
    }

    /**
     * Delete item with the given id.
     *
     * @param id Id of the item to delete.
     */
    public void delete(String id) throws SQLException, IOException {
        String queryItems = String.format(
                "DELETE FROM items WHERE id = %s",
                id
        );
        String queryComments = String.format(
                "DELETE FROM comments WHERE item_id = %s",
                id
        );
        this.dbPerformUpdate(queryComments);
        this.dbPerformUpdate(queryItems);
    }

    /**
     * Returns Item with the given Id.
     *
     * @param id Id given.
     * @return Item with this id.
     */
    public Item findById(String id) throws SQLException {
        Item item = null;
        boolean found = false;
        String queryItem = String.format(
                "SELECT id, name, description, create_time FROM items WHERE id = %s",
                id
        );
        String queryComments = String.format(
                "SELECT text FROM comments WHERE item_id = %s",
                id
        );
        String idI;
        String nameI;
        String descriptionI;
        long createTimeI;
        String[] commentsI;
        try (ResultSet rOut = this.connection.createStatement().executeQuery(queryItem)) {
            if (rOut.next()) {
                found = true;
                idI = Integer.toString(rOut.getInt("id"));
                nameI = rOut.getString("name");
                descriptionI = rOut.getString("description");
                createTimeI = rOut.getTimestamp("create_time").getTime();
                try (ResultSet rIn = this.connection.createStatement().executeQuery(queryComments)) {
                    List<String> c = new ArrayList<>();
                    while (rIn.next()) {
                        c.add(rIn.getString("text"));
                    }
                    commentsI = c.toArray(new String[0]);
                }
                item = new Item(idI, nameI, descriptionI, createTimeI, commentsI);
            }
        }
/*        if (!found) {
            throw new NoSuchIdException("Item with such id not found");
        }*/
        return item;
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
}
