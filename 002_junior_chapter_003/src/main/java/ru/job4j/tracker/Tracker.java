package ru.job4j.tracker;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

/**
 * Storage of items : tasks, messages, etc.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.01.2018
 */
public class Tracker implements AutoCloseable {
    private static final String PROPERTIES_FILE = Paths.get("src", "main", "resources", "ru", "job4j", "tracker", "tracker.properties").toString();

    // Tracker должен работать с базой данных:
    // 0) подключаться к ней
    // 1) создавать начальную структуру, если надо
    // 2) добавлять в нее данные методом add, изменять методом replace и удалять методом delete
    // 3) а также возвращать их по id
    Connection connection = DriverManager.getConnection();

    private void InitDatabase() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");


        src / main / resources / ru / tracker.properties

        Properties properties = new Properties();
        properties.load
  ...
    }

}


    /**
     * Array containing items : tasks, messages etc.
     */
    private List<Item> items = new ArrayList<>();

    /**
     * Add new item to array.
     * Adds unique id value to Item's field.
     *
     * @param item Item to add.
     */
    public Item add(Item item) {
        item.setId(this.generateId());
        this.items.add(item);
        return item;
    }

    /**
     * Replace item with given item.
     *
     * @param id   Id of the item to replace.
     * @param item Item to store with the given id.
     */
    public void replace(String id, Item item) {
        this.items.set(
                this.items.indexOf(this.findById(id)), item
        );
    }

    /**
     * Delete item with the given id.
     *
     * @param id Id of the item to delete.
     */
    public void delete(String id) {
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
    public Item findById(String id) {
        boolean found = false;
        Item result = null;
        for (Item temp : this.items) {
            if (id.equals(temp.getId())) {
                result = temp;
                found = true;
                break;
            }
        }
        if (found) {
            return result;
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
