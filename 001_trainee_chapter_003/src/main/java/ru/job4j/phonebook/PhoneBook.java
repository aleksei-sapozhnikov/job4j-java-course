package ru.job4j.phonebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple phone book.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class PhoneBook {

    /**
     * List of people stored in the phone book.
     */
    private List<Person> persons = new ArrayList<>();

    /**
     * Add new entry.
     *
     * @param person new person to add.
     */
    public void add(Person person) {
        this.persons.add(person);
    }

    /**
     * Return all persons containing given key in any field.
     *
     * @param key key to search.
     * @return list of satisfying persons.
     */
    public List<Person> find(String key) {
        List<Person> result = new ArrayList<>();
        for (Person person : this.persons) {
            if (person.containsInAnyField(key)) {
                result.add(person);
            }
        }
        return result;
    }
}
