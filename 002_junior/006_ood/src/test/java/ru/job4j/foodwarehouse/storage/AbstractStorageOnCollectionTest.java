package ru.job4j.foodwarehouse.storage;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        assertThat(storage.getAll(), is(List.of("hello")));
    }
}