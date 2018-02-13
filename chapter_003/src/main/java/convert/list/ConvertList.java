package convert.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts 2-dimensional array into ArrayList and vice versa.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.02.2018
 */
public class ConvertList {

    /**
     * Converts List of int[] arrays into one List.
     *
     * @param list List of int[].
     * @return List containing all elements of int[] arrays in given list of this arrays.
     */
    public List<Integer> convert(List<int[]> list) {
        List<Integer> result = new ArrayList<>();
        for (int[] array : list) {
            for (int x : array) {
                result.add(x);
            }
        }
        return result;
    }

    /**
     * Converts 2-dimensional array into ArrayList.
     *
     * @param array 2-dimensional array of int to convert.
     * @return ArrayList containing all elements of the array.
     */
    public List<Integer> toList(int[][] array) {
        List<Integer> result = new ArrayList<>();
        for (int[] inside : array) {
            for (int element : inside) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Converts given List into 2-dimensional array.
     *
     * @param list List to convert.
     * @param rows Number of rows in the result Array. If there are not enough elements to fill all the rows, the rest is filled with "0".
     * @return 2-dimensional array containing all elements of given List.
     */
    public int[][] toArray(List<Integer> list, int rows) {
        int cols = list.size() % rows == 0
                ? list.size() / rows
                : list.size() / rows + 1;
        int[][] result = new int[rows][cols]; //filled with "0" by default
        int[] iRows = {0};
        int[] iCols = {0};
        list.forEach(element -> {
                    result[iRows[0]][iCols[0]++] = element;
                    if (iCols[0] >= cols) {
                        iRows[0]++;
                        iCols[0] = 0;
                    }
                }
        );
        return result;
    }
}
