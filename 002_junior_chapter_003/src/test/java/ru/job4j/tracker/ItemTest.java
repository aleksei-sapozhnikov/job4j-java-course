package ru.job4j.tracker;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotEquals;
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

    /**
     * Test setId()
     */
    @Test
    public void whenSetIdThenNewIdReturned() {
        Item item = new Item("12", "Item1", "Desc1", 123L, new String[0]);
        assertThat(item.getId(), is("12"));
        item.setId("34");
        assertThat(item.getId(), is("34"));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenRightFormatString() {
        Item item = new Item("32", "Item1", "Desc1", 123L, new String[]{"com1", "com2", "com3"});
        String result = item.toString();
        // we have to format time because of current system timezone
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expectedTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(123L), ZoneId.systemDefault()).format(formatter);
        String expected = String.format(
                "[id=%s, name=%s, description=%s, createTime=\'%s\', comments=%s]",
                "32", "Item1", "Desc1", expectedTime, "[com1, com2, com3]"
        );
        assertThat(result, is(expected));
    }

    /**
     * Test getComments()
     */
    @Test
    public void whenGetCommentsThenCopyOfCommentsArray() {
        Item item = new Item("Item1", "Desc1", 123L, new String[]{"com1", "com2", "com3"});
        String[] expected = new String[]{"com1", "com2", "com3"};
        String[] beforeChange = item.getComments();
        assertThat(beforeChange, is(expected));
        beforeChange[0] = "change";             // change result array
        assertNotEquals(beforeChange, is(expected));
        String[] afterChange = item.getComments();  // we return copy, so original must be unchanged
        assertThat(afterChange, is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsVariantsAndHashcode() {
        Item main = new Item("0", "main", "descMain", 435L, new String[]{"mainC1", "mainC2"});
        // items to compare
        Item itself = main;
        Item same = new Item("0", "main", "descMain", 435L, new String[]{"mainC1", "mainC2"});
        Item otherId = new Item("13", "main", "descMain", 435L, new String[]{"mainC1", "mainC2"});
        Item otherName = new Item("0", "other", "descMain", 435L, new String[]{"mainC1", "mainC2"});
        Item otherDesc = new Item("0", "main", "descOther", 435L, new String[]{"mainC1", "mainC2"});
        Item otherTime = new Item("0", "main", "descMain", 4334L, new String[]{"mainC1", "mainC2"});
        Item otherComments = new Item("0", "main", "descMain", 435L, new String[]{"mainC1", "otherC2"});
        String otherClass = "I'm the item!";
        Item nullItem = null;
        // assert
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        assertThat(main.equals(otherId), is(true));
        assertThat(main.equals(otherName), is(false));
        assertThat(main.equals(otherDesc), is(false));
        assertThat(main.equals(otherTime), is(false));
        assertThat(main.equals(otherComments), is(false));
        assertThat(main.equals(otherClass), is(false));
        assertThat(main.equals(nullItem), is(false));
        // hashcode
        assertThat(main.hashCode() == same.hashCode(), is(true));
    }


}