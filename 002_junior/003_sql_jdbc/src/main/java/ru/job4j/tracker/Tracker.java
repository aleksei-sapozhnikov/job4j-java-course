package ru.job4j.tracker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Storage of items : tasks, messages, etc.
 * Uses PostgreSQL Database. Stores connection properties in .properties file.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.01.2018
 */
public class Tracker implements AutoCloseable {
    /**
     * Connection to the database.
     */
    private final Connection connection;

    /**
     * Constructs new Tracker object and connects to the
     * database with stored items.
     *
     * @param propertiesFile File *.properties where the database
     *                       connection settings and sql scripts paths
     *                       are stored.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     */
    public Tracker(Path propertiesFile) throws SQLException, IOException {
        this(propertiesFile, false);
    }

    /**
     * Constructs new Tracker object using given database Connection.
     */
    public Tracker(Connection connection) {
        this.connection = connection;
    }

    /**
     * Intended for testing.
     * <p>
     * Constructs new Tracker object and clears all existing values in tables
     * if needed. Uses .sql file with deletion script.
     *
     * @param testPropertiesFile  File *.properties where the database
     *                            connection settings and sql scripts paths
     *                            are stored.
     * @param eraseExistingValues If <tt>true</tt> erases all stored values in database.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     */
    Tracker(Path testPropertiesFile, boolean eraseExistingValues) throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(testPropertiesFile.toString()));
        this.connection = this.dbGetConnection(properties);
        this.dbPerformUpdate(Paths.get(
                testPropertiesFile.getParent().toString(), properties.getProperty("sql_create_tables")
        ));
        if (eraseExistingValues) {
            this.dbPerformUpdate(Paths.get(
                    testPropertiesFile.getParent().toString(), properties.getProperty("sql_erase_values")
            ));
        }
    }

    /**
     * Returns connection to a needed database.
     *
     * @param properties Properties object with database connection settings.
     * @return Connection to needed database.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private Connection dbGetConnection(Properties properties) throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db_url"),
                properties.getProperty("db_user"),
                properties.getProperty("db_password")
        );
    }

    /**
     * Performs operation updating values in database.
     *
     * @param sqlFile .sql file with script to perform.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     */
    private void dbPerformUpdate(Path sqlFile) throws SQLException, IOException {
        try (Statement statement = this.connection.createStatement()) {
            String query = new String(Files.readAllBytes(sqlFile));
            statement.executeUpdate(query);
        }
    }

    /**
     * Performs operation updating values in database.
     *
     * @param query script to perform.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbPerformUpdate(String query) throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    /**
     * Closes Tracker, relinquishing connection.
     * This method is invoked automatically on objects managed by the
     * <tt>try-with-resources</tt> statement.
     *
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    /**
     * Adds new item to database. Changes object adding
     * unique id to the item. The id is given by database.
     *
     * @param item Item to add.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public Item add(Item item) throws SQLException {
        String id = this.dbAddItem(item);
        item.setId(id);
        this.dbAddComments(item);
        return item;
    }

    /**
     * Adds item object fields to 'items' table in database.
     *
     * @param item Item to add.
     * @return id for the item given by database.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private String dbAddItem(Item item) throws SQLException {
        String id;
        String query = String.format(
                "INSERT INTO items (name, description, create_time) VALUES (\'%s\', \'%s\', \'%s\') RETURNING id",
                item.getName(), item.getDescription(), new java.sql.Timestamp(item.getCreateTime()).toString()
        );
        try (ResultSet result = this.connection.createStatement().executeQuery(query)) {
            result.next();
            id = Integer.toString(result.getInt("id"));
        }
        return id;
    }

    /**
     * Adds comments of the given item to database
     * 'comments' table.
     *
     * @param item Given item.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbAddComments(Item item) throws SQLException {
        for (String text : item.getComments()) {
            String query = String.format(
                    "INSERT INTO comments (item_id, text) VALUES (%s, \'%s\')",
                    item.getId(), text
            );
            this.dbPerformUpdate(query);
        }
    }

    /**
     * Deletes all comments of the given item from the database
     * 'comments' table.
     *
     * @param itemId Comments corresponding to this item id will be deleted.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbDeleteComments(String itemId) throws SQLException {
        String query = String.format(
                "DELETE FROM comments WHERE item_id = %s",
                itemId
        );
        this.dbPerformUpdate(query);
    }

    /**
     * Replaces item with given item. The new item will have
     * the same id but new other fields values.
     *
     * @param id   Id of the item to replace.
     * @param item Replacing item.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public void replace(String id, Item item) throws SQLException {
        this.dbUpdateItem(id, item);
        item.setId(id);
        this.dbDeleteComments(id);
        this.dbAddComments(item);
    }

    /**
     * Performs UPDATE operation for item in database
     * changing fields to the fields of the given new item.
     *
     * @param id   Id of the item in database which is to be replaced.
     * @param item New item which replaces the old one.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbUpdateItem(String id, Item item) throws SQLException {
        String query = String.format(
                "UPDATE items SET name = \'%s\', description = \'%s\', create_time = \'%s\' WHERE id = %s",
                item.getName(), item.getDescription(), new java.sql.Timestamp(item.getCreateTime()).toString(),
                id
        );
        this.dbPerformUpdate(query);
    }

    /**
     * Deletes item with the given id from the database.
     * Deletes both the item and corresponding comments.
     *
     * @param id Id of the item to delete.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public void delete(String id) throws SQLException {
        this.dbDeleteComments(id);
        this.dbDeleteItem(id);
    }

    /**
     * Deletes item from the 'items' database table.
     *
     * @param id Id of the item to delete.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbDeleteItem(String id) throws SQLException {
        String query = String.format(
                "DELETE FROM items WHERE id = %s",
                id
        );
        this.dbPerformUpdate(query);
    }

    /**
     * Returns Item with the given Id.
     *
     * @param id Id given.
     * @return Item with this id.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public Item findById(String id) throws SQLException {
        Item item = this.dbGetItem(id);
        if (item == null) {
            throw new NoSuchIdException("Item with such id not found");
        }
        return item;
    }

    /**
     * Retrieves item data from database and returns an Item
     * object.
     *
     * @param id Id of the item to retrieve.
     * @return Item object with field values from database.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private Item dbGetItem(String id) throws SQLException {
        Item result = null;
        String query = String.format(
                "SELECT id, name, description, create_time FROM items WHERE id = %s",
                id
        );
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            if (res.next()) {
                result = new Item(
                        Integer.toString(res.getInt("id")),
                        res.getString("name"),
                        res.getString("description"),
                        res.getTimestamp("create_time").getTime(),
                        this.dbGetComments(id)
                );
            }
        }
        return result;
    }

    /**
     * Retrieves comments to the given item from the database and
     * return value for the item "comments" field.
     *
     * @param itemId Id of the item to find comments to.
     * @return Array of comments for the "comments" field.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private String[] dbGetComments(String itemId) throws SQLException {
        List<String> result = new ArrayList<>();
        String query = String.format(
                "SELECT text FROM comments WHERE item_id = %s",
                itemId
        );
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            while (res.next()) {
                result.add(res.getString("text"));
            }
        }
        return result.toArray(new String[0]);
    }

    /**
     * Return all items whose "name" field is equal to given name.
     *
     * @param name Items with this name will be returned.
     * @return Array of Items satisfying the condition.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public Item[] findByName(String name) throws SQLException {
        List<Item> result = new LinkedList<>();
        String[] ids = this.dbGetItemIdsByName(name);
        for (String id : ids) {
            result.add(this.findById(id));
        }
        return result.toArray(new Item[0]);
    }

    /**
     * Retrieves all id values from database
     * which have given "name" field value.
     *
     * @param name Given name field value.
     * @return Array of item ids having needed name.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private String[] dbGetItemIdsByName(String name) throws SQLException {
        List<String> result = new LinkedList<>();
        String query = String.format(
                "SELECT id FROM items WHERE name = \'%s\' ORDER BY id", name
        );
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            while (res.next()) {
                result.add(Integer.toString(
                        res.getInt("id")
                ));
            }
        }
        return result.toArray(new String[0]);
    }

    /**
     * Return all Items stored now.
     *
     * @return Array of Items stored in tracker (without null elements).
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public Item[] findAll() throws SQLException {
        String[] ids = this.dbGetItemIdsAll();
        List<Item> items = new ArrayList<>();
        for (String id : ids) {
            items.add(this.findById(id));
        }
        return items.toArray(new Item[0]);
    }

    /**
     * Retrieves ids of all itemes stored in database.
     *
     * @return Array of all items ids stored in database.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private String[] dbGetItemIdsAll() throws SQLException {
        List<String> result = new ArrayList<>();
        String query = "select id from items order by id";
        try (ResultSet r = this.connection.createStatement().executeQuery(query)) {
            while (r.next()) {
                result.add(Integer.toString(
                        r.getInt("id")
                ));
            }
        }
        return result.toArray(new String[0]);
    }
}
