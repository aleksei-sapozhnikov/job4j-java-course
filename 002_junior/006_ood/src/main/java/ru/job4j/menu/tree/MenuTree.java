package ru.job4j.menu.tree;

import ru.job4j.menu.WrongArgumentException;
import ru.job4j.menu.item.IMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Menu tree structure.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class MenuTree implements IMenuTree {
    /**
     * List of root elements.
     */
    private List<IMenuItem> roots = new ArrayList<>();

    /**
     * Add menu item.
     *
     * @param item Item to add.
     * @throws WrongArgumentException If parent item of given item could not be found.
     */
    @Override
    public void add(IMenuItem item) throws WrongArgumentException {
        var itemParent = item.getParent();
        if (itemParent == null) {
            this.roots.add(item);
        } else {
            var parent = this.find(itemParent);
            if (parent == null) {
                throw new WrongArgumentException("Parent item not found");
            }
            parent.addChild(item);
        }
    }

    /**
     * Get menu item stored in tree.
     *
     * @param item Item to get.
     * @return Found item or <tt>null</tt> if not found.
     * @throws WrongArgumentException If item is not applicable for search
     *                                (item == null or item id == null).
     */
    @Override
    public IMenuItem get(IMenuItem item) throws WrongArgumentException {
        this.validateGetItem(item);
        return this.find(item);
    }

    /**
     * Validates given item for search.
     *
     * @param item Given item.
     * @throws WrongArgumentException If item is not applicable for search
     *                                (item == null or item id == null).
     */
    private void validateGetItem(IMenuItem item) throws WrongArgumentException {
        if (item == null) {
            throw new WrongArgumentException("Item cannot be null");
        }
        if (item.getId() == null) {
            throw new WrongArgumentException("Item id cannot be null");
        }
    }

    /**
     * Returns list of this tree hierarchy with items.
     *
     * @return List hierarchy.
     */
    @Override
    public List<IMenuTree.HierarchyEntry> getHierarchyList() {
        List<HierarchyEntry> result = new ArrayList<>();
        BiFunction<IMenuItem, Integer, IMenuItem> function = (item, level) -> {
            result.add(new ItemHierarchyEntry(item, level));
            return null;
        };
        this.walkAndApply(function);
        return result;
    }

    /**
     * Tries to find item equal to needed item
     * in the tree.
     *
     * @param needed Needed item.
     * @return Found equal item or <tt>null</tt> if not found.
     */
    private IMenuItem find(IMenuItem needed) {
        BiFunction<IMenuItem, Integer, IMenuItem> check = (item, level) -> {
            IMenuItem result = null;
            if (item.equals(needed)) {
                result = item;
            }
            return result;
        };
        return this.walkAndApply(check);
    }

    /**
     * Walks through the tree and applies function
     * on to every item.
     * <p>
     * If function always returns <tt>null</tt> then
     * all entries will be walked through and <tt>null</tt>
     * returned as a result.
     *
     * @param function Function to apply.
     * @return First non-null value obtained or <tt>null</tt> as default value.
     */
    private IMenuItem walkAndApply(BiFunction<IMenuItem, Integer, IMenuItem> function) {
        return this.applyToListOfItems(this.roots, 0, function);
    }

    /**
     * Applies given function to every item and its sub-items in given lise.
     *
     * @param items    List of items.
     * @param level    Hierarchy level of this list of items.
     * @param function Function to apply.
     * @return First non-null value obtained or <tt>null</tt> as default value.
     */
    private IMenuItem applyToListOfItems(List<IMenuItem> items, int level,
                                         BiFunction<IMenuItem, Integer, IMenuItem> function) {
        IMenuItem result = null;
        for (var elt : items) {
            if (result == null) {
                result = this.applyToItem(elt, level, function);
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Applies given function to item and it's sub-items.
     *
     * @param item     Item.
     * @param level    Item's hierarchy level.
     * @param function Function to apply.
     * @return First non-null value obtained or <tt>null</tt> as default value.
     */
    private IMenuItem applyToItem(IMenuItem item, int level, BiFunction<IMenuItem, Integer, IMenuItem> function) {
        var result = function.apply(item, level);
        if (result == null) {
            result = this.applyToListOfItems(item.getChildren(), level + 1, function);
        }
        return result;
    }

    /**
     * Menu hierarchy object. Contains ШMenuItem щиоусе
     * and its level in hierarchy.
     */
    public static class ItemHierarchyEntry implements HierarchyEntry {
        /**
         * Menu item object.
         */
        private final IMenuItem item;
        /**
         * Item's hierarchy level.
         */
        private final int level;

        /**
         * @param item  Item object.
         * @param level Item's level in hierarchy.
         */
        public ItemHierarchyEntry(IMenuItem item, int level) {
            this.item = item;
            this.level = level;
        }

        /**
         * Returns item.
         *
         * @return Value of item field.
         */
        @Override
        public IMenuItem getItem() {
            return this.item;
        }

        /**
         * Returns level.
         *
         * @return Value of level field.
         */
        @Override
        public int getLevel() {
            return this.level;
        }
    }
}
