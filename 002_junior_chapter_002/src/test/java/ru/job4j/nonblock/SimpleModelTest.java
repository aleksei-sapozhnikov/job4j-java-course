package ru.job4j.nonblock;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for SimpleModel class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 21.04.2018
 */
public class SimpleModelTest {

    /**
     * Test getters: id(), version(), name().
     * And changeName() method.
     */
    @Test
    public void whenGetterThenFieldValue() {
        SimpleModel model = new SimpleModel(12, "aaa"); // version == 0
        SimpleModel modified = model.changeName("bbb"); // version == 1
        assertThat(model.id(), is(12));
        assertThat(model.name(), is("aaa"));
        assertThat(model.version(), is(0));
        assertThat(modified.id(), is(12));
        assertThat(modified.name(), is("bbb"));
        assertThat(modified.version(), is(1));
    }

    @Test
    public void whenIdEqualThenEqualsTrueAndHashCodesTheSameAndViceVersa() {
        SimpleModel model = new SimpleModel(11, "left");
        SimpleModel equal = new SimpleModel(11, "left");
        SimpleModel sameId = new SimpleModel(11, "right");
        SimpleModel otherId = new SimpleModel(22, "left");
        String otherClass = "Simple model";
        // trivial things
        assertThat(model.equals(model), is(true));
        assertThat(model.equals(otherClass), is(false));
        assertThat(model.equals(null), is(false));
        // only id matters
        assertThat(model.equals(equal), is(true));
        assertThat(model.equals(sameId), is(true));
        assertThat(model.equals(otherId), is(false));
        // hashcode
        assertThat(model.hashCode() == equal.hashCode(), is(true));
        assertThat(model.hashCode() == sameId.hashCode(), is(true));
    }
}