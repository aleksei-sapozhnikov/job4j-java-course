package ru.job4j.menu.tree;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.menu.WrongArgumentException;
import ru.job4j.menu.item.IMenuItem;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MenuTreeTest {

    @Test
    public void whenEmptyTreeThenHierarchyListEmpty() {
        var tree = new MenuTree();
        assertEquals(tree.getHierarchyList(), Collections.emptyList());
    }

    @Test
    public void whenAddRootItemThenGetThisItem() throws WrongArgumentException {
        var tree = new MenuTree();
        var root = this.mockMenuItem("root", null);
        tree.add(root);
        var found = tree.get(root);
        assertSame(found, root);
    }

    @Test
    public void whenAddSubItemThenGetThisItem() throws WrongArgumentException {
        var tree = new MenuTree();
        var root = mockMenuItem("root", null);
        var sub = mockMenuItem("sub", root);
        tree.add(root);
        Mockito.doAnswer(i -> Mockito.when(root.getChildren()).thenReturn(List.of(sub)))
                .when(root).addChild(sub);
        tree.add(sub);
        var found = tree.get(sub);
        assertSame(found, sub);
    }

    @Test
    public void whenParentNotFoundThenWrongInputException() {
        var tree = new MenuTree();
        var root = mockMenuItem("root", null);
        var sub = mockMenuItem("sub", root);
        var wasException = new boolean[]{false};
        try {
            tree.add(sub);
        } catch (WrongArgumentException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    @Test
    public void whenGetMethodReceivesItemNullThenWrongArgumentException() {
        var tree = new MenuTree();
        var wasException = new boolean[]{false};
        try {
            tree.get(null);
        } catch (WrongArgumentException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    @Test
    public void whenGetMethodReceivesItemNullIdThenWrongArgumentException() {
        var tree = new MenuTree();
        var item = this.mockMenuItem(null, null);
        Mockito.when(item.getId()).thenReturn(null);
        var wasException = new boolean[]{false};
        try {
            tree.get(item);
        } catch (WrongArgumentException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    @Test
    public void whenAddElementsThenTheyAreInHierarchyList() throws WrongArgumentException {
        var tree = new MenuTree();
        var root = mockMenuItem("root", null);
        var sub = mockMenuItem("sub", root);
        tree.add(root);
        Mockito.doAnswer(i -> Mockito.when(root.getChildren()).thenReturn(List.of(sub)))
                .when(root).addChild(sub);
        tree.add(sub);
        var hierarchy = tree.getHierarchyList();
        var rootEntry = hierarchy.get(0);
        var subEntry = hierarchy.get(1);
        assertEquals(rootEntry.getItem(), root);
        assertEquals(rootEntry.getLevel(), 0);
        assertEquals(subEntry.getItem(), sub);
        assertEquals(subEntry.getLevel(), 1);
    }

    private IMenuItem mockMenuItem(String id, IMenuItem parent) {
        var root = Mockito.mock(IMenuItem.class);
        Mockito.when(root.getId()).thenReturn(id);
        Mockito.when(root.getParent()).thenReturn(parent);
        return root;
    }
}