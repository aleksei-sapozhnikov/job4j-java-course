package tracker;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemTest {

    /**
     * Test getName method.
     */
    @Test
    public void whenGetNameThenName() {
        Item item = new Item("Item1", "Desc1", 123L);
        String result = item.getName();
        String expected = "Item1";
        assertThat(result, is(expected));
    }

    /**
     * Test getDescription method.
     */
    @Test
    public void whenGetDescriptionThenDescription() {
        Item item = new Item("Item1", "Desc1", 123L);
        String result = item.getDescription();
        String expected = "Desc1";
        assertThat(result, is(expected));
    }

    /**
     * Test getCreateTime method.
     */
    @Test
    public void whenGetCreateTimeThenCreateTime() {
        Item item = new Item("Item1", "Desc1", 123L);
        long result = item.getCreateTime();
        long expected = 123L;
        assertThat(result, is(expected));
    }

}