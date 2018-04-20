package ru.job4j.compare;

import java.util.*;

/**
 * Methods to comparing two arrays: check if elements are the same
 * and number of every element is the same.
 *
 * @param <T>
 */
class CompareArrays<T extends Comparable<T>> {
    private T[] left;
    private T[] right;

    CompareArrays(T[] left, T[] right) {
        this.left = left;
        this.right = right;
    }

    /**
     * By HashMap. Complexity: O(n)
     */
    boolean byHashMap() {
        Map<T, Integer> mLeft = this.arrayToMap(this.left, new HashMap<>());
        Map<T, Integer> mRight = this.arrayToMap(this.right, new HashMap<>());
        return mLeft.equals(mRight);
    }

    /**
     * By TreeMap. Complexity: O(n*log(n)).
     */
    boolean byTreeMap() {
        Map<T, Integer> mLeft = this.arrayToMap(this.left, new TreeMap<>());
        Map<T, Integer> mRight = this.arrayToMap(this.right, new TreeMap<>());
        return mLeft.equals(mRight);
    }

    /**
     * Using list and sorting. Complexity: O(n^2) - quick sort worst case.
     */
    boolean byList() {
        List<T> lLeft = new ArrayList<>(Arrays.asList(this.left));
        List<T> lRight = new ArrayList<>(Arrays.asList(this.right));
        Collections.sort(lLeft);
        Collections.sort(lRight);
        return lLeft.equals(lRight);
    }

    private Map<T, Integer> arrayToMap(T[] array, Map<T, Integer> map) {
        for (T element : array) {
            Integer existing = map.computeIfPresent(element, (k, v) -> (v + 1));
            if (existing == null) {
                map.put(element, 1);
            }
        }
        return map;
    }
}
