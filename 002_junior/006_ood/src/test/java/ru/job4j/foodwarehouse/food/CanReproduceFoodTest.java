package ru.job4j.foodwarehouse.food;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CanReproduceFoodTest {

    @Test
    public void testConstructorTakesValuesRight() {
        var basic = Mockito.mock(AbstractFood.class);
        Mockito.when(basic.getName()).thenReturn("food");
        Mockito.when(basic.getCreateDate()).thenReturn(50L);
        Mockito.when(basic.getExpireDate()).thenReturn(100L);
        Mockito.when(basic.getPrice()).thenReturn(10);
        Mockito.when(basic.getDiscount()).thenReturn(5);
        var food = new CanReproduceFood(basic, true);
        assertThat(food.getName(), is("food"));
        assertThat(food.getCreateDate(), is(50L));
        assertThat(food.getExpireDate(), is(100L));
        assertThat(food.getPrice(), is(10));
        assertThat(food.getDiscount(), is(5));
    }

    @Test
    public void whenSetCanReproduceThenValueChanged() {
        var basic = Mockito.mock(AbstractFood.class);
        var food = new CanReproduceFood(basic, true);
        assertTrue(food.isCanReproduce());
        food.setCanReproduce(false);
        assertFalse(food.isCanReproduce());
        food.setCanReproduce(true);
        assertTrue(food.isCanReproduce());
    }
}