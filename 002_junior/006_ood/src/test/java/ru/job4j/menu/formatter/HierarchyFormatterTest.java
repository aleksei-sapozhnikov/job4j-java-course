package ru.job4j.menu.formatter;

import org.junit.Test;
import ru.job4j.menu.WrongArgumentException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HierarchyFormatterTest {

    @Test
    public void whenLevel0ThenJustEntry() throws Exception {
        var formatter = new HierarchyFormatter("--", " ");
        assertThat(formatter.formatEntry("test", 0), is("test"));
    }

    @Test
    public void whenLevel1ThenFormattedEntry() throws Exception {
        var formatter = new HierarchyFormatter("++", "|");
        assertThat(formatter.formatEntry("entry", 1), is("++|entry"));
    }

    @Test
    public void whenLevel2ThenFormattedEntry() throws Exception {
        var formatter = new HierarchyFormatter("**", "//");
        assertThat(formatter.formatEntry("something", 2), is("****//something"));
    }

    @Test
    public void whenLevelNegativeThenException() throws Exception {
        var formatter = new HierarchyFormatter("--", " ");
        var wasException = new boolean[]{false};
        try {
            formatter.formatEntry("test", -1);
        } catch (WrongArgumentException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }


}