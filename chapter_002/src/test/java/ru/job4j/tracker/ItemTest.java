package ru.job4j.tracker;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemTest {

    /**
     * Test getters : name, description, createTime.
     */
    @Test
    public void WhenGettersThenFieldValues() {
        Item item = new Item("Item1", "Desc1", 123L);
        //results
        String resultName = item.getName();
        String resultDescription = item.getDescription();
        long resultCreateTime = item.getCreateTime();
        //expected
        String expectedName = "Item1";
        String expectedDescription = "Desc1";
        long expectedCreateTime = 123L;
        //matching
        assertThat(resultName, is(expectedName));
        assertThat(resultDescription, is(expectedDescription));
        assertThat(resultCreateTime, is(expectedCreateTime));
    }

}