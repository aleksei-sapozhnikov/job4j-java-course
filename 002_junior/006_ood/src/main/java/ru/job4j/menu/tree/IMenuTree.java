package ru.job4j.menu.tree;

import ru.job4j.menu.WrongArgumentException;
import ru.job4j.menu.item.IMenuItem;

import java.util.List;

/**
 * Menu tree structure.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IMenuTree {

    /**
     * Add menu item.
     *
     * @param item Item to add.
     * @throws WrongArgumentException If parent item of given item could not be found.
     */
    void add(IMenuItem item) throws WrongArgumentException;

    /**
     * Get menu item.
     *
     * @param item Item to get.
     * @return Found item or <tt>null</tt> if not found.
     * @throws WrongArgumentException If item is not applicable for search
     *                                (item == null or item id == null).
     */
    IMenuItem get(IMenuItem item) throws WrongArgumentException;

    /**
     * Returns list of this tree hierarchy with items.
     *
     * @return List hierarchy.
     */
    List<IMenuTree.HierarchyEntry> getHierarchyList();


    /**
     * Menu hierarchy object. Contains ШMenuItem щиоусе
     * and its level in hierarchy.
     */
    interface HierarchyEntry {
        /**
         * Returns item.
         *
         * @return Value of item field.
         */
        IMenuItem getItem();

        /**
         * Returns level.
         *
         * @return Value of level field.
         */
        int getLevel();
    }
}
