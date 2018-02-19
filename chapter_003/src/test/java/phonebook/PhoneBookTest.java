package phonebook;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Person class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class PhoneBookTest {

    /**
     * Test find() method.
     */
    @Test
    public void whenPersonContainsThenPerson() {
        PhoneBook book = new PhoneBook();
        book.add(new Person("Petr", "Arsentev", "534872", "Bryansk"));
        List<Person> found = book.find("Petr");
        String result = found.iterator().next().info();
        String expected = "Petr, Arsentev, 534872, Bryansk";
        assertThat(result, is(expected));
    }

    @Test
    public void whenTwoPersonsContainThenTwoPersonsList() {
        PhoneBook book = new PhoneBook();
        book.add(new Person("Petr", "Arsentev", "534872", "Bryansk"));
        book.add(new Person("Ivan", "Demidov", "2143434", "Moscow"));
        book.add(new Person("Vasya", "Demidov", "872435", "Sochi"));
        book.add(new Person("Anna", "Marjina", "665543", "Petersburg"));
        List<Person> found = book.find("Demidov");
        int result = found.size();
        assertThat(result, is(2));
    }

    @Test
    public void whenNoAnyPersonContainingThenEmptyList() {
        PhoneBook book = new PhoneBook();
        book.add(new Person("Petr", "Arsentev", "534872", "Bryansk"));
        book.add(new Person("Ivan", "Demidov", "2143434", "Moscow"));
        List<Person> found = book.find("Petersburg");
        int result = found.size();
        assertThat(result, is(0));
    }

}