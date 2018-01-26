package ru.job4j.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StartUITestAsking {
    private PrintStream stdout = System.out;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void changeOutput() {
        System.setOut(new PrintStream(this.out));
    }

    @After
    public void backOutput() {
        System.setOut(this.stdout);
    }

    @Test
    public void whenUserShowAllThenListOfItems() {
        //add items
        Tracker tracker = new Tracker();
        Item item1 = tracker.add(new Item("name1", "desc1", 123L));
        item1.setId("111");
        Item item2 = tracker.add(new Item("name2", "desc2", 456L));
        item2.setId("222");
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
                .add("=== Item id : 111")
                .add("name : name1")
                .add("description : desc1")
                .add("created : 01.01.1970 03:00:00")
                .add("")
                .add("=== Item id : 222")
                .add("name : name2")
                .add("description : desc2")
                .add("created : 01.01.1970 03:00:00")
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