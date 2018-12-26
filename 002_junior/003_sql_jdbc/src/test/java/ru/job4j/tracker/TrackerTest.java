package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.util.methods.CommonUtils;
import ru.job4j.util.methods.ConnectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrackerTest {
    private final Properties properties = CommonUtils.loadProperties(this, "ru/job4j/tracker/test_liquibase.properties");

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.properties.getProperty("db.driver"));
        return ConnectionUtils.rollbackOnClose(
                DriverManager.getConnection(
                        this.properties.getProperty("db.url"),
                        this.properties.getProperty("db.user"),
                        this.properties.getProperty("db.password")
                ));
    }

    /**
     * Test add() and findAll()
     */
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item = tracker.add(new Item("test1", "testDescription", 123L));
            assertThat(tracker.findAll()[0], is(item));
        }
    }

    /**
     * Test add() and findById()
     */
    @Test
    public void whenAddItemThenCanFindById() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item itemAdded = tracker.add(new Item("name2", "desc2", 4323L, new String[]{"comm1", "comm2"}));
            Item itemFound = tracker.findById(itemAdded.getId());
            assertThat(itemFound, is(itemAdded));
        }
    }

    @Test(expected = NoSuchIdException.class)
    public void whenNoItemWithSuchIdStoredThenNoSuchIdException() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item = tracker.add(new Item("name2", "desc2", 4323L));
            String notId = item.getId() + "123";
            tracker.findById(notId);
        }
    }

    /**
     * Test replace() and findById()
     */
    @Test
    public void whenReplaceNameThenReturnNewItem() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item oldItem = tracker.add(new Item("name1", "desc1", 123L));
            String id = oldItem.getId();
            Item newItem = new Item("name3", "desc3", 323L, new String[]{"comm1", "comm5"});
            tracker.replace(id, newItem);
            assertThat(tracker.findById(id), is(newItem));
        }
    }

    /**
     * Test findAll() and add()
     */
    @Test
    public void whenHasItemsReturnAllNotNullItems() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item1 = tracker.add(new Item("name1", "desc1", 324L));
            Item item2 = tracker.add(new Item("name2", "desc2", 756L));
            Item item3 = tracker.add(new Item("name1", "desc3", 743L));
            Item item4 = tracker.add(new Item("name3", "desc4", 12L));
            Item item5 = tracker.add(new Item("name1", "desc2", 323L));
            Item[] result = tracker.findAll();
            Item[] expected = {item1, item2, item3, item4, item5};
            assertThat(result, is(expected));
        }
    }

    /**
     * Test delete() and findAll()
     */
    @Test
    public void whenDeleteItemThenArrayWithoutThisItem() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item1 = tracker.add(new Item("name1", "desc1", 324L, new String[]{"n1c1", "n1c2"}));
            Item item2 = tracker.add(new Item("name2", "desc2", 756L, new String[]{"n2c1", "n2c2"}));
            Item item3 = tracker.add(new Item("name3", "desc3", 743L, new String[]{"n3c1", "n3c2"}));
            Item item4 = tracker.add(new Item("name4", "desc4", 125L, new String[]{"n4c1", "n4c2"}));
            Item item5 = tracker.add(new Item("name5", "desc5", 323L, new String[]{"n5c1", "n5c2"}));
            tracker.delete(item4.getId());
            Item[] result = tracker.findAll();
            Item[] expected = {item1, item2, item3, item5};
            assertThat(result, is(expected));
        }
    }

    /**
     * Test findByName()
     */
    @Test
    public void whenGivenNameThenItemsWithThatName() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item1 = tracker.add(new Item("name1", "desc1", 324L));
            Item item2 = tracker.add(new Item("name2", "desc2", 756L));
            Item item3 = tracker.add(new Item("name1", "desc3", 743L));
            Item item4 = tracker.add(new Item("name4", "desc4", 12L));
            Item item5 = tracker.add(new Item("name1", "desc5", 323L));
            Item item6 = tracker.add(new Item("name1", "desc6", 577L));
            // assert
            Item[] result = tracker.findByName("name1");
            Item[] expected = {item1, item3, item5, item6};
            assertThat(result, is(expected));
        }
    }
}