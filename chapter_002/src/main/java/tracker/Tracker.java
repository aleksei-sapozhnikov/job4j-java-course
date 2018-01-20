package tracker;

import java.util.Random;

/**
 * Storage of items : tasks, messages, etc.
 */
public class Tracker {

    /**
     * Array containing items : tasks, messages etc.
     */
    private Item[] items = new Item[100];

    /**
     * Index where to put new item into array.
     */
    private int position = 0;


    /**
     * Generate unique id for a new Item.Метод генерирует уникальный ключ для заявки.
     *
     * @return Unique id.
     */
    private String generateId() {
        return String.valueOf(System.currentTimeMillis() + (new Random()).nextInt());
    }

    /**
     * Add new item to array.
     * Adds unique id value to Item's field.
     *
     * @param item Item to add.
     */
    public Item add(Item item) {
        item.setId(this.generateId());
        this.items[position++] = item;
        return item;
    }

    /**
     * Returns Item with the given Id.
     *
     * @param id Id given.
     * @return Item with this id.
     */
    public Item findById(String id) {
        Item result = null;
        for (Item item : this.items) {
            if (item.getId().equals(id)) {
                result = item;
                break;
            }
        }
        return result;
    }

    /**
     * Replace item with another item.
     *
     * @param id   Id of the item to replace.
     * @param item Item to store with the given id.
     */
    public void replace(String id, Item item) {

    }

    /**
     * Delete item with the given id.
     *
     * @param id Id of the item to delete.
     */
    public void delete(String id) {

    }

    /**
     * Return all Items stored now.
     *
     * @return Array of Items stored in tracker (without null elements).
     */
    public Item[] findAll() {
        Item[] result = null;
        return result;
    }

    /**
     * Return all items whose "name" field equals to given parameter.
     *
     * @param key Items with this name will be returned.
     * @return Array of Items satisfying the condition.
     */
    public Item[] findByName(String key) {
        Item[] result = null;
        return result;
    }

}
