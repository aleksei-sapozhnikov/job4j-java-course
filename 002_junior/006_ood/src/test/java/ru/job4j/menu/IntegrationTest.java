package ru.job4j.menu;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.menu.formatter.HierarchyFormatter;
import ru.job4j.menu.item.ActionAble;
import ru.job4j.menu.item.IMenuItem;
import ru.job4j.menu.item.MenuItemFactory;
import ru.job4j.menu.tree.IMenuTree;
import ru.job4j.menu.tree.MenuTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    @Test
    public void createMenuAndPrint() throws Exception {
        var elements = this.createMenuItems();
        var tree = this.createMenuTree(elements);
        var hierarchy = tree.getHierarchyList();
        var result = this.formatHierarchy(hierarchy);
        var expected = this.createExpected();
        assertEquals(result, expected);
    }

    private List<String> createExpected() {
        return List.of(
                "Task 1",
                "-- Task 1.1",
                "---- Task 1.1.1",
                "---- Task 1.1.2",
                "-- Task 1.2",
                "Task 2",
                "-- Task 2.1",
                "Task 3",
                "-- Task 3.1"
        );
    }

    private IMenuItem[] createMenuItems() {
        var creator = new MenuItemFactory();
        var action = Mockito.mock(ActionAble.class);
        return new IMenuItem[]{
                creator.newRootItem("r1", "Task 1", action),
                creator.newRootItem("r2", "Task 2", action),
                creator.newRootItem("r3", "Task 3", action),
                // a bit mixed adding order to assure hierarchy not depending on that
                creator.newInnerItem("r3s1", "Task 3.1", creator.newSearchItem("r3"), action),
                creator.newInnerItem("r2S1", "Task 2.1", creator.newSearchItem("r2"), action),
                creator.newInnerItem("r1S1", "Task 1.1", creator.newSearchItem("r1"), action),
                creator.newInnerItem("r1S2", "Task 1.2", creator.newSearchItem("r1"), action),
                creator.newInnerItem("r1S1S1", "Task 1.1.1", creator.newSearchItem("r1S1"), action),
                creator.newInnerItem("r1S1S1", "Task 1.1.2", creator.newSearchItem("r1S1"), action)
        };
    }

    private IMenuTree createMenuTree(IMenuItem[] elements) throws WrongArgumentException {
        var tree = new MenuTree();
        for (var item : elements) {
            tree.add(item);
        }
        return tree;
    }

    private ArrayList<String> formatHierarchy(List<IMenuTree.HierarchyEntry> hierarchy) throws WrongArgumentException {
        var formatter = new HierarchyFormatter("--", " ");
        var result = new ArrayList<String>();
        Consumer<String> consumer = result::add;
        for (var entry : hierarchy) {
            consumer.accept(formatter.formatEntry(
                    entry.getItem().getName(),
                    entry.getLevel())
            );
        }
        return result;
    }
}