package ru.job4j.foodwarehouse.control;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.foodwarehouse.checker.GoodsChecker;

import static org.junit.Assert.assertSame;

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
}