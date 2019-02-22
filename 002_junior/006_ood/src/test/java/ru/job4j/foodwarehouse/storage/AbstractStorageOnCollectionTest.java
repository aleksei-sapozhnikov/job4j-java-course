package ru.job4j.foodwarehouse.storage;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class AbstractStorageOnCollectionTest {

    @Test
    public void whenGetStorageThenGivenStorageReturned() {
        @SuppressWarnings("unchecked")
        var collection = (Collection<Object>) Mockito.mock(Collection.class);
        var storage = new AbstractStorageOnCollection<>(collection) {
        };
        assertSame(storage.getCollection(), collection);
    }

    @Test
    public void whenAddThenCanGet() {
        var storage = new AbstractStorageOnCollection<>(new ArrayList<String>()) {
        };
        storage.add("hello");
        storage.add("my");
        storage.add("name");
        var result = storage.getAll();
        var expected = Arrays.asList("hello", "my", "name");
        result.sort(Comparator.naturalOrder());
        expected.sort(Comparator.naturalOrder());
        assertThat(result, is(expected));
    }

    @Test
    public void whenClearThenEmpty() {
        var storage = new AbstractStorageOnCollection<>(new ArrayList<String>()) {
        };
        storage.add("hello");
        storage.add("my");
        storage.add("name");
        storage.clear();
        assertThat(storage.getAll(), is(Collections.emptyList()));
    }
}