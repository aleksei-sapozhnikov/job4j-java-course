package ru.job4j.coffee;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for CoffeeMachine class.
 */
public class CoffeeMachineTest {

    /**
     * Test change() method.
     */
    @Test
    public void whenGivenMoneyThenArrayOfCoins() throws NotEnoughMoneyException {
        int given = 50;
        int price = 35;
        int[] result = new CoffeeMachine().change(given, price);
        int[] expected = {10, 5};
        assertThat(result, is(expected));
    }

    @Test
    public void whenGivenMoneyThenArrayOfCoins2() throws NotEnoughMoneyException {
        int given = 67;
        int price = 9;
        int[] result = new CoffeeMachine().change(given, price);
        int[] expected = {10, 10, 10, 10, 10, 5, 2, 1};
        assertThat(result, is(expected));
    }

    @Test
    public void whenGivenMoneyThenSumOfCoinsAsExpected() throws NotEnoughMoneyException {
        int given = 5000;
        int price = 1;
        int expected = 4999;
        int[] array = new CoffeeMachine().change(given, price);
        int result = 0;
        for (int n : array) {
            result += n;
        }
        assertThat(result, is(expected));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void whenGivenMoneyNotEnoughThenNotEnoughMoneyException() throws NotEnoughMoneyException {
        int given = 50;
        int price = 65;
        new CoffeeMachine().change(given, price);
    }

    @Test
    public void whenGivenEqualsPriceThenEmptyArray() throws NotEnoughMoneyException {
        int given = 50;
        int price = 50;
        int[] result = new CoffeeMachine().change(given, price);
        int[] expected = {};
        assertThat(result, is(expected));
    }
}