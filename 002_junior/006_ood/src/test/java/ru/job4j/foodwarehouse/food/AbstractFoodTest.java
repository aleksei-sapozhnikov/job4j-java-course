package ru.job4j.foodwarehouse.food;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AbstractFoodTest {

    @Test
    public void testGetters() {
        var food = new AbstractFood(
                "food", 100, 200, 500, 10) {
        };
        assertThat(food.getName(), is("food"));
        assertThat(food.getCreateDate(), is(100L));
        assertThat(food.getExpireDate(), is(200L));
        assertThat(food.getPrice(), is(500));
        assertThat(food.getDiscount(), is(10));
    }

    @Test
    public void testSetters() {
        var food = new AbstractFood(
                "food", 100, 200, 500, 10) {
        };
        food.setPrice(4);
        food.setDiscount(1000);
        assertThat(food.getPrice(), is(4));
        assertThat(food.getDiscount(), is(1000));
    }
}