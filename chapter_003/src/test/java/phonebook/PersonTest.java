package phonebook;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Person class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class PersonTest {

    /**
     * Test info() method.
     */
    @Test
    public void whenInfoThenStringInfo() {
        Person person = new Person("Ivan", "Demidov", "12345678", "Moscow");
        String result = person.info();
        String expected = "Ivan, Demidov, 12345678, Moscow";
        assertThat(result, is(expected));
    }
}