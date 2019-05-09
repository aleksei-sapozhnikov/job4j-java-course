package ru.job4j.menu.item;


import java.util.List;

/**
 * Menu item with values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IMenuItem extends ActionAble {
    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    String getId();

    /**
     * Returns name.
     *
     * @return Value of name field.
     */
    String getName();

    /**
     * Returns list with this item's sub-items.
     *
     * @return Value of children field.
     */
    List<IMenuItem> getChildren();

    /**
     * Adds child item to this item.
     *
     * @param child Child item to add.
     */
    void addChild(IMenuItem child);

    /**
     * Returns this item's parent item.
     *
     * @return Parent item.
     */
    IMenuItem getParent();
}
