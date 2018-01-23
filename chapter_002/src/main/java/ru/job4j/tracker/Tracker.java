package ru.job4j.tracker;

import java.util.Arrays;
import java.util.Random;

/**
 * Storage of items : tasks, messages, etc.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.01.2018
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
     * Add new item to array.
     * Adds unique id value to Item's field.
     *
     * @param item Item to add.
     */
    public Item add(Item item) {
        item.setId(this.generateId());
        this.items[this.position++] = item;
        return item;
    }

    /**
     * Replace item with given item.
     *
     * @param id   Id of the item to replace.
     * @param item Item to store with the given id.
     */
    public void replace(String id, Item item) {
        int repIndex = Arrays.asList(this.items).indexOf(
                this.findById(id)
        );
        this.items[repIndex] = item;
    }

    /**
     * Delete item with the given id.
     *
     * @param id Id of the item to delete.
     */
    public void delete(String id) {
        int delIndex = Arrays.asList(this.items).indexOf(
                this.findById(id)
        );
        System.arraycopy(
                this.items,
                delIndex + 1,
                this.items,
                delIndex,
                this.position-- - delIndex - 1
        );
    }

    /**
     * Returns Item with the given Id.
     *
     * @param id Id given.
     * @return Item with this id.
     */
    public Item findById(String id) {
        Item result = null;
        for (int i = 0; i < this.position; i++) {
            if (id.equals(this.items[i].getId())) {
                result = this.items[i];
                break;
            }
        }
        return result;
    }

    /**
     * Return all items whose "name" field is equal to given key.
     *
     * @param key Items with this name will be returned.
     * @return Array of Items satisfying the condition.
     */
    public Item[] findByName(String key) {
        Item[] temp = new Item[this.position];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            if (key.equals(this.items[i].getName())) {
                temp[index++] = this.items[i];
            }
        }
        return Arrays.copyOf(temp, index);
    }

    /**
     * Return all Items stored now.
     *
     * @return Array of Items stored in tracker (without null elements).
     */
    public Item[] findAll() {
        return Arrays.copyOf(this.items, this.position);
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
