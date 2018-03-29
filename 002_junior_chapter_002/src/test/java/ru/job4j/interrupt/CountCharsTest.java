package ru.job4j.interrupt;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CountCharsTest {

    /**
     * Test countChars()
     */
    @Test
    public void whenInputStringThenCountCharsRight() {
        String input = "12345";
        CountChars count = new CountChars(input);
        assertThat(count.countChars(), is(5));
    }

    @Test
    public void notATest() throws InterruptedException {
        String input = "Работа";
        Thread a = new Thread(new CountChars(input));
        a.start();
        a.join();
    }
}