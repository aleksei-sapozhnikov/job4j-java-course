package ru.job4j.menu.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creator for objects of IMenuItem interface.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class MenuItemFactory implements IMenuItemFactory {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(MenuItemFactory.class);

    /**
     * Creates new item for search.
     *
     * @param id Item id.
     * @return New search item.
     */
    @Override
    public IMenuItem newSearchItem(String id) {
        return new MenuItem(id, "", null, null);
    }

    /**
     * Creates new root item (parent == null).
     *
     * @param id     Item id.
     * @param name   Item's name.
     * @param action Item's action.
     * @return New root item.
     */
    @Override
    public IMenuItem newRootItem(String id, String name, ActionAble action) {
        return new MenuItem(id, name, null, action);
    }

    /**
     * Creates new inner item.
     *
     * @param id     Item id.
     * @param name   Item name.
     * @param parent Item parent.
     * @param action Item's action.
     * @return New inner item.
     */
    @Override
    public IMenuItem newInnerItem(String id, String name, IMenuItem parent, ActionAble action) {
        return new MenuItem(id, name, parent, action);
    }
}
