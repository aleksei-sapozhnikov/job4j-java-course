package ru.job4j.tracker;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for StartUI class.
 */
public class StartUITest {

    @Test
    public void whenUserAddItemThenTrackerHasItemWithThatName() {
        Tracker tracker = new Tracker();
        Input input = new StubInput(new String[]{"0", "name1", "description1", "6"});
        new StartUI(input, tracker).init();
        String result = tracker.findAll()[0].getDescription();
        String expected = "description1";
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserAddItemThenTrackerHasItemWithThatDescription() {
        Tracker tracker = new Tracker();
        Input input = new StubInput(new String[]{"0", "name1", "description1", "6"});
        new StartUI(input, tracker).init();
        String result = tracker.findAll()[0].getName();
        String expected = "name1";
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserUpdateItemThenTrackerHasUpdatedName() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
        Input input = new StubInput(new String[]{"2", item.getId(), "new name", "new desc", "y", "6"});
        new StartUI(input, tracker).init();
        String result = tracker.findById(item.getId()).getName();
        String expected = "new name";
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserUpdateItemThenTrackerHasUpdatedDescription() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
        Input input = new StubInput(new String[]{"2", item.getId(), "new name", "new desc", "y", "6"});
        new StartUI(input, tracker).init();
        String result = tracker.findById(item.getId()).getDescription();
        String expected = "new desc";
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserDeleteLastItemThenTrackerIsEmpty() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
        Input input = new StubInput(new String[]{"3", item.getId(), "y", "6"});
        new StartUI(input, tracker).init();
        Item[] result = tracker.findAll();
        Item[] expected = {};
        assertThat(result, is(expected));
    }

    @Test
    public void whenUserDeleteItemTheFindByIdThisItemIsNull() {
        Tracker tracker = new Tracker();
        Item item1 = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
        Item itemDel = tracker.add(new Item("name2", "desc3", System.currentTimeMillis()));
        Input input = new StubInput(new String[]{"3", itemDel.getId(), "y", "6"});
        new StartUI(input, tracker).init();
        Item result = tracker.findById(itemDel.getId());
        Item expected = null;
        assertThat(result, is(expected));
    }
}