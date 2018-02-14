package compare.list;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the ListCompare class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 14.02.2018
 */
public class ListCompareTest {

    /**
     * Test compare() method.
     */
    @Test
    public void whenLeftAndRightEqualsThenZero() {
        int rst = new ListCompare().compare(
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 2, 3)
        );
        boolean result = rst == 0;
        assertThat(result, is(true));
    }

    @Test
    public void whenBothAreEmptyThenEqualZero() {
        int rst = new ListCompare().compare(
                Arrays.asList(),
                Arrays.asList()
        );
        boolean result = rst == 0;
        assertThat(result, is(true));
    }

    @Test
    public void whenLeftIsEmptyThenNegative() {
        int rst = new ListCompare().compare(
                Arrays.asList(),
                Arrays.asList(1, 2, 3)
        );
        boolean result = rst < 0;
        assertThat(result, is(true));
    }

    @Test
    public void whenRightIsEmptyThenPositive() {
        int rst = new ListCompare().compare(
                Arrays.asList(1, 2, 3),
                Arrays.asList()
        );
        boolean result = rst > 0;
        assertThat(result, is(true));
    }

    @Test
    public void whenLeftLessThanRightThenNegative() {
        int rst = new ListCompare().compare(
                Arrays.asList(1),
                Arrays.asList(1, 2, 3)
        );
        boolean result = rst < 0;
        assertThat(result, is(true));
    }

    @Test
    public void whenLeftGreaterThanRightThenPositive() {
        int rst = new ListCompare().compare(
                Arrays.asList(1, 2),
                Arrays.asList(1, 1)
        );
        boolean result = rst > 0;
        assertThat(result, is(true));
    }
}