package ru.job4j.switcher;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the StringHolder class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 17.05.2018
 */

public class StringHolderTest {

    /**
     * Test append() and toString()
     */
    @Test
    public void whenAppendThenStringGrows() {
        StringHolder holder = new StringHolder();
        assertThat(holder.toString(), is(""));
        holder.append(123);
        assertThat(holder.toString(), is("123"));
        holder.append(123);
        assertThat(holder.toString(), is("123123"));
        holder.append(0);
        assertThat(holder.toString(), is("1231230"));
        holder.append(-5555);
        assertThat(holder.toString(), is("1231230-5555"));
    }
}