package ru.job4j.compare;

import java.util.HashMap;
import java.util.Map;

class CompareArrays<T> {
    private T[] left;
    private T[] right;

    CompareArrays(T[] left, T[] right) {
        this.left = left;
        this.right = right;
    }

    boolean byMap() {
        Map<T, Integer> mLeft = new HashMap<>();
        Map<T, Integer> mRight = new HashMap<>();
        for (T element : left) {
            Integer existing = mLeft.computeIfPresent(element, (k, v) -> (v + 1));
            if (existing == null) {
                mLeft.put(element, 1);
            }
        }
        return false;
    }

}
