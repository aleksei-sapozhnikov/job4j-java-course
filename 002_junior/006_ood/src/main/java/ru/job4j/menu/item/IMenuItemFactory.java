package ru.job4j.menu.item;

/**
 * Creator for objects of IMenuItem interface.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IMenuItemFactory {
    /**
     * Creates new item for search (with id only).
     *
     * @param id Item id.
     * @return New search item.
     */
    IMenuItem newSearchItem(String id);

    /**
     * Creates new root item (parent == null).
     *
     * @param id     Item id.
     * @param name   Item's name.
     * @param action Item's action.
     * @return New root item.
     */
    IMenuItem newRootItem(String id, String name, ActionAble action);

    /**
     * Creates new inner item.
     *
     * @param id     Item id.
     * @param name   Item name.
     * @param parent Item parent.
     * @param action Item's action.
     * @return New inner item.
     */
    IMenuItem newInnerItem(String id, String name, IMenuItem parent, ActionAble action);
}
