package ru.job4j.set.hashset;

import org.junit.Test;

import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleHashSetTest {

    /**
     * Test add()
     */
    @Test
    public void whenAddThenElementsGoToCorrespondingBuckets() {
        SimpleHashSet<String> set = new SimpleHashSet<>(4);
        set.add("5"); // hash = 53 : 53 % 4 == 1 => bucket # 1
        set.add("6"); // hash = 54 => bucket # 2
        set.add("8"); // hash = 56 => bucket # 0
        String result = set.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket 0: 8")
                .add("bucket 1: 5")
                .add("bucket 2: 6")
                .add("bucket 3: null")
                .toString();
        assertThat(result, is(expected));
    }

    @Test
    public void whenAddNotContainedElementThenAddTrueAndViceVersa() {
        SimpleHashSet<String> set = new SimpleHashSet<>(4);
        assertThat(set.add("5"), is(true));
        assertThat(set.add("6"), is(true));
        assertThat(set.add("5"), is(false));
        assertThat(set.add("8"), is(true));
        assertThat(set.add("8"), is(false));
        String result = set.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket 0: 8")
                .add("bucket 1: 5")
                .add("bucket 2: 6")
                .add("bucket 3: null")
                .toString();
        assertThat(result, is(expected));
    }

    @Test
    public void whenHashCollisionThenAddElementFalse() {
        SimpleHashSet<String> set = new SimpleHashSet<>(4);
        assertThat(set.add("5"), is(true)); // hash = 53 => bucket # 1
        assertThat(set.add("9"), is(false)); // hash = 57 => bucket # 1

    }

    /**
     * Test contains()
     */
    @Test
    public void whenContainsElementThenContainsTrueAndViceVersa() {
        SimpleHashSet<String> set = new SimpleHashSet<>();
        set.add("5");
        set.add("6");
        set.add("8");
        assertThat(set.contains("5"), is(true));
        assertThat(set.contains("3"), is(false));
        assertThat(set.contains("6"), is(true));
        assertThat(set.contains("7"), is(false));
        assertThat(set.contains("9"), is(false));
        assertThat(set.contains("8"), is(true));
        assertThat(set.contains("1"), is(false));
    }

    /**
     * Test remove()
     */
    @Test
    public void whenRemoveExistingElementThenTrueAndBucketValueIsNullAndWhenRemoveNotExistingThenFalse() {
        SimpleHashSet<String> set = new SimpleHashSet<>(4);
        set.add("5"); // hash = 53 : 53 % 4 == 1 => bucket # 1
        set.add("6"); // hash = 54 => bucket # 2
        set.add("8"); // hash = 56 => bucket # 0
        assertThat(set.remove("8"), is(true));
        assertThat(set.remove("8"), is(false)); //already removed
        assertThat(set.remove("2"), is(false)); //never existed
        assertThat(set.remove("5"), is(true));
        String result = set.toString();
        String expected = new StringJoiner(System.lineSeparator())
                .add("bucket 0: null")
                .add("bucket 1: null")
                .add("bucket 2: 6")
                .add("bucket 3: null")
                .toString();
        assertThat(result, is(expected));
    }

    /**
     * Test remove()
     */
    @Test
    public void whenRemoveWithHashcodeForTheSameBucketButNotTheSameThenFalse() {
        SimpleHashSet<String> set = new SimpleHashSet<>(4);
        set.add("5"); // hash = 53 => bucket # 1
        set.add("6"); // hash = 54 => bucket # 2
        assertThat(set.remove("12"), is(false)); //hash for "12" == 1569 => bucket # 1 (1569 % 4 == 1);
    }

    /**
     * Test growIfNeeded()
     */
    @Test
    public void whenHashSetIfFullEnoughThenEnlarges() {
        SimpleHashSet<String> set = new SimpleHashSet<>(5);
        set.add("2"); // hash == 50 => bucket # 0
        set.add("3"); // bucket # 1
        set.add("4"); // bucket # 2
        set.add("5"); // bucket # 3, filled: 4 / 5 == 80%
        set.add("6"); // grow, new size = 5 * 3 / 2 + 1 = 8
        String result = set.toString();
        String expected = new StringJoiner(System.lineSeparator())  // now "2" bucket must be # 2 (50 % 8 == 2)
                .add("bucket 0: null")
                .add("bucket 1: null")
                .add("bucket 2: 2")
                .add("bucket 3: 3")
                .add("bucket 4: 4")
                .add("bucket 5: 5")
                .add("bucket 6: 6")
                .add("bucket 7: null")
                .toString();
        assertThat(result, is(expected));
    }
}