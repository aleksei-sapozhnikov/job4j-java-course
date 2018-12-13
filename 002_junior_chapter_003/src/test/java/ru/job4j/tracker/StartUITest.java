package ru.job4j.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.util.common.Utils;
import ru.job4j.util.database.ConnectionRollback;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for StartUI class.
 */
public class StartUITest {
    private Path trackerTestConfig = Paths.get("src", "main", "resources", "ru", "job4j", "tracker", "tracker_test.properties").toAbsolutePath();

    private final Properties properties = Utils.loadProperties(this, "ru/job4j/tracker/test_liquibase.properties");

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.properties.getProperty("db.driver"));
        return ConnectionRollback.create(
                DriverManager.getConnection(
                        this.properties.getProperty("db.url"),
                        this.properties.getProperty("db.user"),
                        this.properties.getProperty("db.password")
                ));
    }

    /**
     * Stores "standard" console output.
     */
    private PrintStream stdout = System.out;

    /**
     * Output for testing.
     */
    private ByteArrayOutputStream out = new ByteArrayOutputStream();


    /**
     * Change "standard" output to testing output.
     */
    @Before
    public void changeOutput() {
        System.setOut(new PrintStream(this.out));
    }

    /**
     * Return output back to "standard".
     */
    @After
    public void backOutput() {
        System.setOut(this.stdout);
    }


    /**
     * Test add.
     */
    @Test
    public void whenUserAddItemThenTrackerHasItemWithThatName() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Input input = new StubInput(new String[]{"0", "name1", "description1", "6"});
            new StartUI(input, tracker).init();
            String result = tracker.findAll()[0].getDescription();
            String expected = "description1";
            assertThat(result, is(expected));
        }
    }

    @Test
    public void whenUserAddItemThenTrackerHasItemWithThatDescription() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Input input = new StubInput(new String[]{"0", "name1", "description1", "6"});
            new StartUI(input, tracker).init();
            String result = tracker.findAll()[0].getName();
            String expected = "name1";
            assertThat(result, is(expected));
        }
    }

    /**
     * Test update.
     */
    @Test
    public void whenUserUpdateItemThenTrackerHasUpdatedName() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
            Input input = new StubInput(new String[]{"2", item.getId(), "new name", "new desc", "y", "6"});
            new StartUI(input, tracker).init();
            String result = tracker.findById(item.getId()).getName();
            String expected = "new name";
            assertThat(result, is(expected));
        }
    }

    @Test
    public void whenUserUpdateItemThenTrackerHasUpdatedDescription() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
            Input input = new StubInput(new String[]{"2", item.getId(), "new name", "new desc", "y", "6"});
            new StartUI(input, tracker).init();
            String result = tracker.findById(item.getId()).getDescription();
            String expected = "new desc";
            assertThat(result, is(expected));
        }
    }

    /**
     * Test delete.
     */
    @Test
    public void whenUserDeleteLastItemThenTrackerIsEmpty() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
            Input input = new StubInput(new String[]{"3", item.getId(), "y", "6"});
            new StartUI(input, tracker).init();
            Item[] result = tracker.findAll();
            Item[] expected = new Item[0];
            assertThat(result, is(expected));
        }
    }

    @Test(expected = NoSuchIdException.class)
    public void whenUserDeleteItemThenFindByIdThisItemNoSuchIdException() throws Exception {
        try (Tracker tracker = new Tracker(this.getConnection())) {
            Item item1 = tracker.add(new Item("name1", "desc1", System.currentTimeMillis()));
            Item itemDel = tracker.add(new Item("name2", "desc3", System.currentTimeMillis()));
            Input input = new StubInput(new String[]{"3", itemDel.getId(), "y", "6"});
            new StartUI(input, tracker).init();
            tracker.findById(itemDel.getId());
        }
    }

    /**
     * Test show all.
     */
    @Test
    public void whenUserShowAllThenListOfItems() throws Exception {
        //add items
        try (Tracker tracker = new Tracker(this.getConnection())) {
            String id1 = tracker.add(new Item("name1", "desc1", 0L)).getId();
            String id2 = tracker.add(new Item("name2", "desc2", 0L)).getId();
            //run program
            Input input = new StubInput(new String[]{"1", "6"});
            new StartUI(input, tracker).init();
            String result = new String(this.out.toByteArray());
            String expected = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("============ Action menu ============")
                    .add("0 : Add new Item")
                    .add("1 : Show all items")
                    .add("2 : Edit item")
                    .add("3 : Delete item")
                    .add("4 : Find item by Id")
                    .add("5 : Find items by name")
                    .add("6 : Exit Program")
                    .add("")
                    .add("------------ Show all items contained ------------")
                    .add("")
                    .add(String.format("=== Item id : %s", id1))
                    .add("name : name1")
                    .add("description : desc1")
                    .add("")
                    .add(String.format("=== Item id : %s", id2))
                    .add("name : name2")
                    .add("description : desc2")
                    .add("")
                    .add("============ Action menu ============")
                    .add("0 : Add new Item")
                    .add("1 : Show all items")
                    .add("2 : Edit item")
                    .add("3 : Delete item")
                    .add("4 : Find item by Id")
                    .add("5 : Find items by name")
                    .add("6 : Exit Program")
                    .add("=== Exit program.")
                    .add("")
                    .toString();
            assertThat(result, is(expected));
        }
    }

    /**
     * Test show by id.
     */
    @Test
    public void whenUserShowItemByIdThenShowItem() throws Exception {
        //add items
        try (Tracker tracker = new Tracker(this.getConnection())) {
            String id1 = tracker.add(new Item("name1", "desc1", 0L)).getId();
            String id2 = tracker.add(new Item("name2", "desc2", 0L)).getId();
            //run program
            Input input = new StubInput(new String[]{"4", id1, "6"});
            new StartUI(input, tracker).init();
            String result = new String(this.out.toByteArray());
            String expected = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("============ Action menu ============")
                    .add("0 : Add new Item")
                    .add("1 : Show all items")
                    .add("2 : Edit item")
                    .add("3 : Delete item")
                    .add("4 : Find item by Id")
                    .add("5 : Find items by name")
                    .add("6 : Exit Program")
                    .add("")
                    .add("------------ Find item by id ------------")
                    .add("=== Item information : ")
                    .add(String.format("id : %s", id1))
                    .add("name : name1")
                    .add("description : desc1")
                    .add("")
                    .add("============ Action menu ============")
                    .add("0 : Add new Item")
                    .add("1 : Show all items")
                    .add("2 : Edit item")
                    .add("3 : Delete item")
                    .add("4 : Find item by Id")
                    .add("5 : Find items by name")
                    .add("6 : Exit Program")
                    .add("=== Exit program.")
                    .add("")
                    .toString();
            assertThat(result, is(expected));
        }
    }

    /**
     * Test show by id.
     */
    @Test
    public void whenUserShowItemByNameThenShowItemsWithThatName() throws Exception {
        //add items
        try (Tracker tracker = new Tracker(this.getConnection())) {
            String id1 = tracker.add(new Item("name1", "desc1", 0L)).getId();
            String id2 = tracker.add(new Item("name2", "desc2", 0L)).getId();
            String id3 = tracker.add(new Item("name1", "desc3", 0L)).getId();
            //run program
            Input input = new StubInput(new String[]{"5", "name1", "6"});
            new StartUI(input, tracker).init();
            String result = new String(this.out.toByteArray());
            String expected = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("============ Action menu ============")
                    .add("0 : Add new Item")
                    .add("1 : Show all items")
                    .add("2 : Edit item")
                    .add("3 : Delete item")
                    .add("4 : Find item by Id")
                    .add("5 : Find items by name")
                    .add("6 : Exit Program")
                    .add("")
                    .add("------------ Find items with given name ------------")
                    .add("")
                    .add(String.format("== Item id : %s", id1))
                    .add("name : name1")
                    .add("description : desc1")
                    .add("")
                    .add(String.format("== Item id : %s", id3))
                    .add("name : name1")
                    .add("description : desc3")
                    .add("")
                    .add("============ Action menu ============")
                    .add("0 : Add new Item")
                    .add("1 : Show all items")
                    .add("2 : Edit item")
                    .add("3 : Delete item")
                    .add("4 : Find item by Id")
                    .add("5 : Find items by name")
                    .add("6 : Exit Program")
                    .add("=== Exit program.")
                    .add("")
                    .toString();
            assertThat(result, is(expected));
        }
    }

}