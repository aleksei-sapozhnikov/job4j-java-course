package ru.job4j.compare;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompareArraysTest {

    @Test
    public void whenEqualNumbersAndTheirQuantityThenTrue() {
        Integer[] left = {1, 5, 5, 2, 5, 3, 4};
        Integer[] right = {2, 5, 4, 3, 5, 1, 5};
        CompareArrays<Integer> compare = new CompareArrays<>(left, right);
        assertThat(compare.byHashMap(), is(true));
        assertThat(compare.byTreeMap(), is(true));
        assertThat(compare.byList(), is(true));
    }

    @Test
    public void whenDifferentNumbersThenFalse() {
        Integer[] left = {1, 5, 5, 2, 5, 3, 4};
        Integer[] right = {7, 5, 4, 3, 5, 1, 5};
        CompareArrays<Integer> compare = new CompareArrays<>(left, right);
        assertThat(compare.byHashMap(), is(false));
        assertThat(compare.byTreeMap(), is(false));
        assertThat(compare.byList(), is(false));
    }

    @Test
    public void whenEqualCharacterArraysThenTrue() {
        Character[] left = {'м', 'а', 'м', 'а', 'р', 'о', 'й', 'к', 'а'};
        Character[] right = {'а', 'м', 'м', 'а', 'о', 'й', 'к', 'а', 'р'};
        CompareArrays<Character> compare = new CompareArrays<>(left, right);
        assertThat(compare.byHashMap(), is(true));
        assertThat(compare.byTreeMap(), is(true));
        assertThat(compare.byList(), is(true));
    }

    @Test
    public void whenDifferentCharacterArraysThenFalse() {
        Character[] left = {'м', 'а', 'м', 'а', 'р', 'о', 'й', 'к', 'а'};
        Character[] right = {'а', 'м', 'м', 'а', 'о', 'й', 'к', 'а', 'т'};
        CompareArrays<Character> compare = new CompareArrays<>(left, right);
        assertThat(compare.byHashMap(), is(false));
        assertThat(compare.byTreeMap(), is(false));
        assertThat(compare.byList(), is(false));
    }

}