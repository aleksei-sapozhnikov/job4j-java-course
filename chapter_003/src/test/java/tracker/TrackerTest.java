package tracker;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrackerTest {

    /**
     * Test add method.
     */
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("test1", "testDescription", 123L));
        assertThat(tracker.findAll().get(0), is(item));
    }

    /**
     * Test findById method.
     */
    @Test
    public void whenGivenIdOfItemThenTheItem() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name2", "desc2", 4323L));
        Item itemOther = tracker.findById(item.getId());
        assertThat(itemOther, is(item));
    }

    @Test(expected = NoSuchIdException.class)
    public void whenNoSuchIdThenNoSuchIdException() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("name2", "desc2", 4323L));
        String notId = item.getId() + "123";
        tracker.findById(notId);
    }

    /**
     * Test findByName method.
     */
    @Test
    public void whenGivenNameThenItemsWithThatName() {
        Tracker tracker = new Tracker();
        //create and add
        Item item1 = tracker.add(new Item("name1", "desc1", 324L));
        Item item2 = tracker.add(new Item("name2", "desc2", 756L));
        Item item3 = tracker.add(new Item("name1", "desc3", 743L));
        Item item4 = tracker.add(new Item("name4", "desc4", 12L));
        Item item5 = tracker.add(new Item("name1", "desc5", 323L));
        Item item6 = tracker.add(new Item("name1", "desc6", 577L));
        //matching
        List<Item> result = tracker.findByName("name1");
        List<Item> expected = Arrays.asList(item1, item3, item5, item6);
        assertThat(result, is(expected));
    }

    /**
     * Test findAll method.
     */
    @Test
    public void whenHasItemsReturnAllNotNullItems() {
        Tracker tracker = new Tracker();
        Item item1 = tracker.add(new Item("name1", "desc1", 324L));
        Item item2 = tracker.add(new Item("name2", "desc2", 756L));
        Item item3 = tracker.add(new Item("name1", "desc3", 743L));
        Item item4 = tracker.add(new Item("name3", "desc4", 12L));
        Item item5 = tracker.add(new Item("name1", "desc2", 323L));
        List<Item> result = tracker.findAll();
        List<Item> expected = Arrays.asList(item1, item2, item3, item4, item5);
        assertThat(result, is(expected));
    }

    /**
     * Test replace method.
     */
    @Test
    public void whenReplaceNameThenReturnNewName() {
        Tracker tracker = new Tracker();
        Item oldItem = tracker.add(new Item("name1", "desc1", 123L));
        Item newItem = new Item("name3", "desc3", 323L);
        newItem.setId(oldItem.getId());
        tracker.replace(oldItem.getId(), newItem);
        assertThat(tracker.findById(oldItem.getId()).getName(), is("name3"));
    }

    /**
     * Test delete method
     */
    @Test
    public void whenDeleteItemThenArrayWithoutThisItem() {
        Tracker tracker = new Tracker();
        Item item1 = tracker.add(new Item("name1", "desc1", 324L));
        Item item2 = tracker.add(new Item("name2", "desc2", 756L));
        Item item3 = tracker.add(new Item("name1", "desc3", 743L));
        Item item4 = tracker.add(new Item("name3", "desc4", 12L));
        Item item5 = tracker.add(new Item("name1", "desc2", 323L));
        tracker.delete(item4.getId());
        List<Item> result = tracker.findAll();
        List<Item> expected = Arrays.asList(item1, item2, item3, item5);
        assertThat(result, is(expected));
    }
}