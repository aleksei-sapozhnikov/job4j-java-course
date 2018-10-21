package ru.job4j.functional.tracker;

import java.util.*;
import java.util.stream.Collectors;

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
        Optional<Item> result = this.items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
        if (!result.isPresent()) {
            throw new NoSuchIdException("Item with such id not found");
        }
        return result.get();
    }

    /**
     * Return all items whose "name" field is equal to given name.
     *
     * @param name Items with this name will be returned.
     * @return Array of Items satisfying the condition.
     */
    public List<Item> findByName(String name) {
        return this.items.stream()
                .filter(item -> item.getName().equals(name))
                .collect(Collectors.toCollection(ArrayList::new));
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
