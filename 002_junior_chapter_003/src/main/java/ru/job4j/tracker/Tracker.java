package ru.job4j.tracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
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
    private final Connection connection;

    public Tracker(Path propertiesFile) throws SQLException, IOException {
        this(propertiesFile, false);
    }

    public Tracker(Path propertiesFile, boolean clearTables) throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile.toString()));
        this.connection = this.getConnectionToDatabase(
                properties.getProperty("db_url"), properties.getProperty("db_user"), properties.getProperty("db_password")
        );
        this.dbPerformUpdate(Paths.get(
                propertiesFile.getParent().toString(), properties.getProperty("sql_create_tables")
        ));
        if (clearTables) {
            this.dbPerformUpdate(Paths.get(
                    propertiesFile.getParent().toString(), properties.getProperty("sql_erase_values")
            ));
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

    /**
     * Array containing items : tasks, messages etc.
     */
    private List<Item> items = new ArrayList<>();

    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    private Connection getConnectionToDatabase(String url, String user, String password) throws SQLException {
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
        if (!found) {
            throw new NoSuchIdException("Item with such id not found");
        }
        return item;
    }

    /**
     * Return all items whose "name" field is equal to given name.
     *
     * @param name Items with this name will be returned.
     * @return Array of Items satisfying the condition.
     */
    public List<Item> findByName(String name) throws SQLException {
        String query = String.format(
                "SELECT id FROM items WHERE name = \'%s\' ORDER BY id", name
        );
        List<Integer> ids = new ArrayList<>();
        try (ResultSet r = this.connection.createStatement().executeQuery(query)) {
            while (r.next()) {
                ids.add(r.getInt("id"));
            }
        }
        List<Item> items = new ArrayList<>();
        for (Integer i : ids) {
            items.add(this.findById(i.toString()));
        }
        return items;
    }

    /**
     * Return all Items stored now.
     *
     * @return Array of Items stored in tracker (without null elements).
     */
    public List<Item> findAll() throws SQLException {
        String query = "SELECT id FROM items ORDER BY id";
        List<Integer> ids = new ArrayList<>();
        try (ResultSet r = this.connection.createStatement().executeQuery(query)) {
            while (r.next()) {
                ids.add(r.getInt("id"));
            }
        }
        List<Item> items = new ArrayList<>();
        for (Integer i : ids) {
            items.add(this.findById(i.toString()));
        }
        return items;
    }
}
