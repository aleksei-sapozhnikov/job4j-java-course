package utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for StaticUtils class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class StaticUtilsTest {

    private double delta = 0.000001d;

    /**
     * Test haveEqualValues()
     */
    @Test
    public void testInnerMethodAreEqualArrays() {
        List<Double> main = Arrays.asList(0D, 1D, 2D, 3D);
        List<Double> equal = Arrays.asList(0D, 1D, 2D, 3D);
        List<Double> notEqualFirst = Arrays.asList(1D, 2D, 2D, 3D);
        List<Double> notEqualMiddle = Arrays.asList(0D, 2D, 2D, 3D);
        List<Double> notEqualLast = Arrays.asList(0D, 2D, 2D, 4D);
        List<Double> otherSize = Arrays.asList(0D, 1D, 2D);
        assertThat(StaticUtils.haveEqualValues(main, main, this.delta), is(true));
        assertThat(StaticUtils.haveEqualValues(main, equal, this.delta), is(true));
        assertThat(StaticUtils.haveEqualValues(main, notEqualFirst, this.delta), is(false));
        assertThat(StaticUtils.haveEqualValues(main, notEqualMiddle, this.delta), is(false));
        assertThat(StaticUtils.haveEqualValues(main, notEqualLast, this.delta), is(false));
        assertThat(StaticUtils.haveEqualValues(main, otherSize, this.delta), is(false));
    }

}