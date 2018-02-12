package convert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the ConvertList class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.02.2018
 */
public class ConvertListTest {

    /**
     * Test toArray() method.
     */
    @Test
    public void whenCreatedIntArrayItIsFilledWithZeros() {
        int[][] result = new int[2][3];
        int[][] expected = {
                {0, 0, 0},
                {0, 0, 0}
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertListWhereNumberOfElementsIsDivisibleByRowsThenFullyFilledWithElements() {
        List<Integer> list = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(8);
                add(4);
                add(5);
                add(-9);
            }
        };
        int rows = 2;
        int[][] result = new ConvertList().toArray(list, rows);
        int[][] expected = {
                {1, 2, 8},
                {4, 5, -9}
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertListWhereNumberOfElementsIsDivisibleByRowsThenFullyFilledWithElements2() {
        List<Integer> list = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };
        int rows = 1;
        int[][] result = new ConvertList().toArray(list, rows);
        int[][] expected = {
                {1, 2, 3}
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertListWhereNumberOfElementsIsNotDivisibleByRowsThenRestFilledWithZeros() {
        List<Integer> list = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
                add(6);
                add(7);
            }
        };
        int rows = 3;
        int[][] result = new ConvertList().toArray(list, rows);
        int[][] expected = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 0}
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertListWhereNumberOfElementsIsNotDivisibleByRowsThenRestFilledWithZeros2() {
        List<Integer> list = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
            }
        };
        int rows = 5;
        int[][] result = new ConvertList().toArray(list, rows);
        int[][] expected = {
                {1},
                {2},
                {0},
                {0},
                {0}
        };
        assertThat(result, is(expected));
    }

    /**
     * Test toList() method.
     */
    @Test
    public void whenConvertArrayToListThenResult() {
        int[][] array = {
                {-1, -2, 3},
                {4, -5, 6}
        };
        List<Integer> result = new ConvertList().toList(array);
        List<Integer> expected = new ArrayList<Integer>() {
            {
                add(-1);
                add(-2);
                add(3);
                add(4);
                add(-5);
                add(6);
            }
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertArrayToListThenResult2() {
        int[][] array = {
                {0, -7},
                {4, -6},
                {3, 23},
                {-7, 23},
                {0, 11},
        };
        List<Integer> result = new ConvertList().toList(array);
        List<Integer> expected = new ArrayList<Integer>() {
            {
                add(0);
                add(-7);
                add(4);
                add(-6);
                add(3);
                add(23);
                add(-7);
                add(23);
                add(0);
                add(11);
            }
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertEmptyArrayToListThenEmptyList() {
        int[][] array = {{}};
        List<Integer> result = new ConvertList().toList(array);
        List<Integer> expected = new ArrayList<>();
        assertThat(result, is(expected));
    }

    /**
     * Test convert() method.
     */
    @Test
    public void whenConvertListOfArraysThenResult() {
        List<int[]> input = new ArrayList<int[]>() {
            {
                add(new int[]{3, 2, 5});
                add(new int[]{2, 5, 4, 6, 4, 6});
                add(new int[]{3, 2});
                add(new int[]{});
            }
        };
        List<Integer> result = new ConvertList().convert(input);
        List<Integer> expected = new ArrayList<Integer>() {
            {
                add(3);
                add(2);
                add(5);
                add(2);
                add(5);
                add(4);
                add(6);
                add(4);
                add(6);
                add(3);
                add(2);
            }
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertListOfArraysThenResult2() {
        List<int[]> input = new ArrayList<>();
        input.add(new int[]{1, -2});
        input.add(new int[]{3, 5, -5, 6});
        List<Integer> result = new ConvertList().convert(input);
        List<Integer> expected = new ArrayList<Integer>() {
            {
                add(1);
                add(-2);
                add(3);
                add(5);
                add(-5);
                add(6);
            }
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenConvertListOfEmptyArraysThenEmptyArray() {
        List<int[]> input = new ArrayList<int[]>() {
            {
                add(new int[]{});
                add(new int[]{});
                add(new int[]{});
                add(new int[]{});
            }
        };
        List<Integer> result = new ConvertList().convert(input);
        List<Integer> expected = new ArrayList<>();
        assertThat(result, is(expected));
    }


}