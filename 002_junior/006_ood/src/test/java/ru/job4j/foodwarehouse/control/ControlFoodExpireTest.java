package ru.job4j.foodwarehouse.control;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.foodwarehouse.checker.GoodsChecker;
import ru.job4j.foodwarehouse.food.AbstractFood;
import ru.job4j.foodwarehouse.handler.HandlerByAge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;

public class ControlFoodExpireTest {

    @Test
    public void whenControlThenHandlerActionTaken() {
        var food = Mockito.mock(AbstractFood.class);
        @SuppressWarnings("unchecked") var checker = (GoodsChecker<AbstractFood>) Mockito.mock(GoodsChecker.class);
        @SuppressWarnings("unchecked") var handler = (HandlerByAge<AbstractFood>) Mockito.mock(HandlerByAge.class);
        var handleNew = new boolean[]{false};
        var handleMiddle = new boolean[]{false};
        var handleOld = new boolean[]{false};
        var handleExpired = new boolean[]{false};
        Mockito.doAnswer(i -> handleNew[0] = true).when(handler).handleNew(food);
        Mockito.doAnswer(i -> handleMiddle[0] = true).when(handler).handleMiddle(food);
        Mockito.doAnswer(i -> handleOld[0] = true).when(handler).handleOld(food);
        Mockito.doAnswer(i -> handleExpired[0] = true).when(handler).handleExpired(food);
        // just to assure
        assertFalse(handleNew[0]);
        assertFalse(handleMiddle[0]);
        assertFalse(handleOld[0]);
        assertFalse(handleExpired[0]);
        var controller = new ControlFoodExpire<>(checker, handler);

        // handle new food
        Mockito.when(checker.check(eq(food), anyLong())).thenReturn(5);
        controller.doControl(food);
        assertTrue(handleNew[0]);
        assertFalse(handleMiddle[0]);
        assertFalse(handleOld[0]);
        assertFalse(handleExpired[0]);
        handleNew[0] = false; //reset
        // handle middle-age food
        Mockito.when(checker.check(eq(food), anyLong())).thenReturn(50);
        controller.doControl(food);
        assertFalse(handleNew[0]);
        assertTrue(handleMiddle[0]);
        assertFalse(handleOld[0]);
        assertFalse(handleExpired[0]);
        handleMiddle[0] = false; //reset
        // handle old food
        Mockito.when(checker.check(eq(food), anyLong())).thenReturn(99);
        controller.doControl(food);
        assertFalse(handleNew[0]);
        assertFalse(handleMiddle[0]);
        assertTrue(handleOld[0]);
        assertFalse(handleExpired[0]);
        handleOld[0] = false; //reset
        // handle expired food
        Mockito.when(checker.check(eq(food), anyLong())).thenReturn(105);
        controller.doControl(food);
        assertFalse(handleNew[0]);
        assertFalse(handleMiddle[0]);
        assertFalse(handleOld[0]);
        assertTrue(handleExpired[0]);
        handleExpired[0] = false; //reset
    }
}