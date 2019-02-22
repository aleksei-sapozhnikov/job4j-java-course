package ru.job4j.foodwarehouse.control;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.foodwarehouse.checker.GoodsChecker;
import ru.job4j.foodwarehouse.storage.Storage;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class AbstractGoodsControlTest {

    @Test
    public void whenGetCheckerThenSameCheckerObject() {
        @SuppressWarnings("unchecked")
        var given = (GoodsChecker<Object>) Mockito.mock(GoodsChecker.class);
        var found = new AbstractGoodsControl<>(given) {
            public void doControl(Object obj) {
            }
        }.getChecker();
        assertSame(given, found);
    }

    @Test
    public void whenResortThenDoControlOnEveryElement() {
        @SuppressWarnings("unchecked") var given = (GoodsChecker<String>) Mockito.mock(GoodsChecker.class);
        var calledOn = new ArrayList<String>();
        var control = new AbstractGoodsControl<>(given) {
            public void doControl(String obj) {
                calledOn.add("a");    // to assure ww did something
                calledOn.add(obj);
            }
        };
        @SuppressWarnings("unchecked") var storage = (Storage<String>) Mockito.mock(Storage.class);
        Mockito.when(storage.getAll()).thenReturn(List.of("1", "2", "3"));
        control.resort(storage);
        assertThat(calledOn, is(List.of("a", "1", "a", "2", "a", "3")));
    }
}