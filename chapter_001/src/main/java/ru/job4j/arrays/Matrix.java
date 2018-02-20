package ru.job4j.arrays;

/**
 * Class to study 2-dimensional arrays.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.01.2018
 */
public class Matrix {

    /**
     * Makes matrix (array size X size) with multiplication table.
     *
     * @param size Width and Height needed.
     * @return Matrix with multiplication table.
     */
    public int[][] multiple(int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = (i + 1) * (j + 1);
            }
        }
        return result;
    }

}
