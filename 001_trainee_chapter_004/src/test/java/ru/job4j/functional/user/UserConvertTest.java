package ru.job4j.functional.user;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for UserConvert class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UserConvertTest {

    private PrintStream stdSystemOut = System.out;
    private UserConvert convert = new UserConvert();

    /**
     * Replaces System.out with given PrintStream.
     *
     * @param p Printstream to use.
     */
    private void replaceSystemOut(PrintStream p) {
        System.setOut(p);
    }

    /**
     * Returns standard System.out back.
     */
    private void returnSystemOut() {
        System.setOut(this.stdSystemOut);
    }

    /**
     * Check convert()
     */
    @Test
    public void whenListOfStringsThenPrintListOfUsers() {
        List<String> names = Arrays.asList("Petr", "Nick", "Ban");
        List<UserConvert.User> converted = this.convert.convert(
                names, UserConvert.User::new
        );
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.replaceSystemOut(new PrintStream(stream));
        converted.forEach(System.out::println);
        this.returnSystemOut();
        String result = String.valueOf(stream);
        String expected = new StringJoiner(System.lineSeparator())
                .add(new UserConvert.User("Petr").toString())
                .add(new UserConvert.User("Nick").toString())
                .add(new UserConvert.User("Ban").toString())
                .add("")
                .toString();
        assertThat(result, is(expected));
    }

}