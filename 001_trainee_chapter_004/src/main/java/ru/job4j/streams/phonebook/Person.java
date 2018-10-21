package ru.job4j.streams.phonebook;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Person for a phone book.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class Person {

    /**
     * Name.
     */
    private final String name;

    /**
     * Surname.
     */
    private final String surname;

    /**
     * Phone number.
     */
    private final String phone;

    /**
     * Address, place of living.
     */
    private final String address;

    /**
     * Constructor.
     *
     * @param name    name.
     * @param surname surname.
     * @param phone   phone number.
     * @param address address, place of living.
     */
    Person(String name, String surname, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Checks if given key is found in any of the person's fields.
     *
     * @param key string to look for in the person's fields.
     * @return true or false, as key was found in any of fields or not.
     */
    boolean containsInAnyField(String key) {
        return this.name.contains(key)
                || this.surname.contains(key)
                || this.phone.contains(key)
                || this.address.contains(key);
    }

    /**
     * Делает то же, что и метод containsInAnyField(), но используя Reflection API.
     * Захотелось немножко поизвращаться. В книге прочитал, а попробовать негде было.
     * <p>
     * Метод запрашивает все поля класса, затем у всех String-ов проверяет, содержат ли они key.
     * Тесты на него те же, что и на containsInAnyField().
     *
     * @param key ключ, который ищем.
     * @return true или false, если key найден в каком-то из String-полей key или нет.
     * Также возвращает false, если случился IllegalAccessException - запросили доступ к полю или методу,
     * к которому, по замыслу автора класса, не должно быть доступа отсюда.
     */
    boolean containsInAnyFieldReflectAPI(String key) {
        boolean result;
        Optional<String> found = Arrays.stream(this.getClass().getDeclaredFields())
                .map(this::getFieldOfThis)
                .filter(obj -> obj instanceof String)
                .map(str -> (String) str)
                .filter(str -> str.contains(key))
                .findAny();
        result = found.isPresent();
        return result;
    }

    private Object getFieldOfThis(Field field) {
        Object result = null;
        try {
            result = field.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Checks if this person is equal to another object.
     *
     * @param other object to compare to.
     * @return true of false, as objects are equal or not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Person person = (Person) other;
        return Objects.equals(this.name, person.name)
                && Objects.equals(this.surname, person.surname)
                && Objects.equals(this.phone, person.phone)
                && Objects.equals(this.address, person.address);
    }

    /**
     * Calculate integer hashcode for this person.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.surname, this.phone, this.address);
    }
}