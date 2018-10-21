package ru.job4j.music.entities;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressTest {
    /**
     * Objects to test.
     */
    private final Address withId = new Address(434, "Address with id");
    private final Address withoutId = new Address("Address without id");

    /**
     * Test getters.
     */
    @Test
    public void whenGetterThenValue() {
        // with id
        assertThat(this.withId.getId(), is(434));
        assertThat(this.withId.getName(), is("Address with id"));
        // without id
        assertThat(this.withoutId.getId(), is(Address.DEFAULT_ID));
        assertThat(this.withoutId.getName(), is("Address without id"));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenStringAsNeeded() {
        String expected = String.format(
                "[address id=%s, name=%s]",
                this.withId.getId(),
                this.withId.getName()
        );
        assertThat(this.withId.toString(), is(expected));
    }

    /**
     * Test equals() and hashCode()
     */
    @Test
    public void testEqualsAndHashcode() {
        Address main = this.withId;
        // objects to compare
        Address itself = this.withId;
        Address same = new Address(434, "Address with id");
        Address idOther = new Address(7314, "Address with id");
        Address nameOther = new Address(434, "Address with other name");
        Address nullObject = null;
        String classOther = "I'm Address!";
        // equal
        assertThat(main.equals(itself), is(true));
        assertThat(main.equals(same), is(true));
        assertThat(main.equals(idOther), is(true));
        // hashcode of equal objects
        assertThat(main.hashCode() == itself.hashCode(), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.hashCode() == idOther.hashCode(), is(true));
        // not equal
        assertThat(main.equals(nameOther), is(false));
        assertThat(main.equals(nullObject), is(false));
        assertThat(main.equals(classOther), is(false));
    }

}