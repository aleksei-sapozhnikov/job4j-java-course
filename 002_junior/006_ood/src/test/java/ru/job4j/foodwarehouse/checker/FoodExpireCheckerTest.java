package ru.job4j.foodwarehouse.checker;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.foodwarehouse.food.AbstractFood;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FoodExpireCheckerTest {

    @Test
    public void whenNotCreatedYetThenPercentNegative() {
        long current = 5;
        var createDate = current + 100;
        var expireDate = current + 200;
        var expected = -100;
        this.checkAndCompareExpirePercent(current, createDate, expireDate, expected);
    }

    @Test
    public void whenJustCreatedThenPercent0() {
        long current = 5;
        var createDate = current;
        var expireDate = current + 50;
        var expected = 0;
        this.checkAndCompareExpirePercent(current, createDate, expireDate, expected);
    }

    @Test
    public void whenHalfExpiredThenPercent50() {
        long current = 5;
        var createDate = current - 100;
        var expireDate = current + 100;
        var expected = 50;
        this.checkAndCompareExpirePercent(current, createDate, expireDate, expected);
    }

    @Test
    public void whenJustExpiredThenPercent100() {
        long current = 5;
        var createDate = current - 100;
        var expireDate = current;
        var expected = 100;
        this.checkAndCompareExpirePercent(current, createDate, expireDate, expected);
    }

    @Test
    public void whenTwiceExpiredThenPercent200() {
        long current = 5;
        var createDate = current - 100;
        var expireDate = current - 50;
        var expected = 200;
        this.checkAndCompareExpirePercent(current, createDate, expireDate, expected);
    }

    /**
     * Calculates expiration percent and asserts it is as expected.
     *
     * @param current               Current time.
     * @param createDate            Create date.
     * @param expireDate            Expiration date.
     * @param expectedExpirePercent Expected expiration percent.
     */
    private void checkAndCompareExpirePercent(long current, long createDate, long expireDate, int expectedExpirePercent) {
        var food = Mockito.mock(AbstractFood.class);
        Mockito.when(food.getCreateDate()).thenReturn(createDate, current);
        Mockito.when(food.getExpireDate()).thenReturn(expireDate, current);
        var result = new FoodExpireChecker<>().check(food, current);
        assertThat(result, is(expectedExpirePercent));
    }
}