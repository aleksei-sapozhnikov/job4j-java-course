package ru.job4j.helloworld;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for HelloWorld.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.01.2018
 */
public class HelloWorldTest {
    /**
     * Test echo.
     */
    @Test
    public void whenTakeNameThenTreeEchoPlusName() {
        String input = "Ivan Ivanovich Ivanov";
        String expect = "Echo, echo, echo : Ivan Ivanovich Ivanov";
        HelloWorld calc = new HelloWorld();
        String result = calc.echo(input);
        assertThat(result, is(expect));
    }

}
