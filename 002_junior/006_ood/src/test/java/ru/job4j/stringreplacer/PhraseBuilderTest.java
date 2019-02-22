package ru.job4j.stringreplacer;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhraseBuilderTest {

    @Test
    public void whenTailAfterKeysThenTailAppended() throws NotFoundException {
        var template = "Hello, ${name}. I want to talk to you.";
        var map = new HashMap<String, String>();
        map.put("name", "John");
        var builder = new StringReplacer();
        var result = builder.replaceAll(template, map);
        var expected = "Hello, John. I want to talk to you.";
        assertEquals(result, expected);
    }

    @Test
    public void whenMultiplyTimesUsedKeyThenAllReplaced() throws NotFoundException {
        var template = "Help, ${sos}, ${sos}, ${sos}!";
        var builder = new StringReplacer();
        var map = new HashMap<String, String>();
        map.put("sos", "AAA");
        var result = builder.replaceAll(template, map);
        var expected = "Help, AAA, AAA, AAA!";
        assertEquals(result, expected);
    }

    @Test
    public void whenIllegalSymbolsInKeysThenTemplateUnchanged() throws NotFoundException {
        var template = "These are not keys: ${name hi}, ${my.dog}, ${%beauty}";
        var builder = new StringReplacer();
        var map = new HashMap<String, String>();
        var result = builder.replaceAll(template, map);
        var expected = "These are not keys: ${name hi}, ${my.dog}, ${%beauty}";
        assertEquals(result, expected);
    }

    @Test
    public void whenKeyNotFoundInMapThenNotFoundException() {
        var template = "Hello, ${name}!";
        var builder = new StringReplacer();
        var map = new HashMap<String, String>();
        map.put("sos", "AAA");
        var wasException = new boolean[]{false};
        try {
            builder.replaceAll(template, map);
        } catch (NotFoundException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }
}