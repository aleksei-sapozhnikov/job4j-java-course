package ru.job4j.compare;

import java.util.*;

class CompareArrays<T extends Comparable<T>> {
    private T[] left;
    private T[] right;

    CompareArrays(T[] left, T[] right) {
        this.left = left;
        this.right = right;
    }

    boolean byHashMap() {
        Map<T, Integer> mLeft = this.arrayToMap(this.left, new HashMap<>());
        Map<T, Integer> mRight = this.arrayToMap(this.right, new HashMap<>());
        return mLeft.equals(mRight);
    }

    boolean byTreeMap() {
        Map<T, Integer> mLeft = this.arrayToMap(this.left, new TreeMap<>());
        Map<T, Integer> mRight = this.arrayToMap(this.right, new TreeMap<>());
        return mLeft.equals(mRight);
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

    boolean byList() {
        List<T> lLeft = new ArrayList<>(Arrays.asList(this.left));
        List<T> lRight = new ArrayList<>(Arrays.asList(this.right));
        Collections.sort(lLeft);
        Collections.sort(lRight);
        return lLeft.equals(lRight);
    }

    boolean byListOneByOne() {
        boolean result = true;
        List<T> lLeft = new ArrayList<>(Arrays.asList(this.left));
        List<T> lRight = new ArrayList<>(Arrays.asList(this.right));
        for (T left : lLeft) {
            boolean has = false;
            for (T right : lRight) {
                if (left.compareTo(right) == 0) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                result = false;
                break;
            }
        }
        return result;
    }

}
