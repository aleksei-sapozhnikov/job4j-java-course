package ru.job4j.streams.convert.list;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return list.stream()
                .flatMapToInt(Arrays::stream)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Converts 2-dimensional array into ArrayList.
     *
     * @param array 2-dimensional array of int to ru.job4j.convert.
     * @return ArrayList containing all elements of the array.
     */
    public List<Integer> toList(int[][] array) {
        return Arrays.stream(array)
                .flatMapToInt(Arrays::stream)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Converts given List into 2-dimensional array.
     *
     * @param list List to ru.job4j.convert.
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
