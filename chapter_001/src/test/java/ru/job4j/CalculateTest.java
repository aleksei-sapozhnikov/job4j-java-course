package ru.job4j;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
* Test for Calculate
*
* @author Aleksei Sapozhnikov (vermucht@gmail.com)
* @vesrion $Id$
* @since 04.01.2018
*/
public class CalculateTest {
	/**
	* Test echo.
	*/ 
	@Test
	public void whenTakeNameThenTreeEchoPlusName() {
		String input = "Ivan Ivanovich Ivanov";
		String expect = "Echo, echo, echo : Ivan Ivanovich Ivanov"; 
		Calculate calc = new Calculate();
		String result = calc.echo(input);
		assertThat(result, is(expect));
	}
 
}
