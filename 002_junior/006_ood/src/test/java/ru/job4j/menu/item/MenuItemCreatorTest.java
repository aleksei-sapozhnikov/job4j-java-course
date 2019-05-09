package ru.job4j.menu.item;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class MenuItemCreatorTest {

    @Test
    public void whenNewSearchItemThenResultAsNeeded() {
        var creator = new MenuItemFactory();
        var result = creator.newSearchItem("12");
        var expected = new MenuItem("12", "", null, null);
        assertEquals(result, expected);
        assertEquals(result.getId(), "12");
        assertEquals(result.getName(), "");
        assertNull(result.getParent());
    }

    @Test
    public void whenNewRootItemThenResultAsNeeded() {
        var action = Mockito.mock(ActionAble.class);
        var creator = new MenuItemFactory();
        var result = creator.newRootItem("12", "root", action);
        var expected = new MenuItem("12", "root", null, action);
        assertEquals(result, expected);
        assertEquals(result.getId(), "12");
        assertEquals(result.getName(), "root");
        assertNull(result.getParent());
    }

    @Test
    public void whenNewInnerItemwThenResultAsNeeded() {
        var action = Mockito.mock(ActionAble.class);
        var creator = new MenuItemFactory();
        var root = new MenuItem("root", "root", null, action);
        var result = creator.newInnerItem("12", "sub", root, action);
        var expected = new MenuItem("12", "root", null, action);
        assertEquals(result, expected);
        assertEquals(result.getId(), "12");
        assertEquals(result.getName(), "sub");
        assertSame(result.getParent(), root);
    }
}