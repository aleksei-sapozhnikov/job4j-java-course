package ru.job4j.menu.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Menu item with values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class MenuItem implements IMenuItem {
    /**
     * Item id.
     */
    private final String id;
    /**
     * Item name.
     */
    private final String name;
    /**
     * Parent item (== null for root item).
     */
    private final IMenuItem parent;
    /**
     * Item's sub-items.
     */
    private final List<IMenuItem> children = new ArrayList<>();
    /**
     * Action performed by this item.
     */
    private final ActionAble action;

    /**
     * Creates new item.
     *
     * @param id     Item id.
     * @param name   Item name.
     * @param action Item's action.
     * @param parent Parent item (== null for root item).
     */
    public MenuItem(String id, String name, IMenuItem parent, ActionAble action) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.action = action;
    }

    /**
     * Returns id.
     *
     * @return Value of id field.
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Returns name.
     *
     * @return Value of name field.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns list with this item's sub-items.
     *
     * @return Value of children field.
     */
    @Override
    public List<IMenuItem> getChildren() {
        return new ArrayList<>(this.children);
    }

    /**
     * Adds child item to this item.
     *
     * @param child Child item to add.
     */
    @Override
    public void addChild(IMenuItem child) {
        this.children.add(child);
    }

    /**
     * Returns this item's parent item.
     *
     * @return Parent item.
     */
    @Override
    public IMenuItem getParent() {
        return this.parent;
    }

    /**
     * Performs action.
     */
    @Override
    public void doAction() {
        this.action.doAction();
    }

    /**
     * Equals method. Uses item's id only.
     *
     * @param o Another object.
     * @return <tt>true</tt> if equal, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(this.getId(), menuItem.getId());
    }

    /**
     * Hashcode() method. Uses id only.
     *
     * @return This object's hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
