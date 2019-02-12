package ru.job4j.foodwarehouse.handler;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.foodwarehouse.food.CanReproduceFood;
import ru.job4j.foodwarehouse.storage.Storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CanReproduceFoodHandlerTest {

    @Test
    public void whenHandleThenCalledRight() {
        var food = Mockito.mock(CanReproduceFood.class);
        @SuppressWarnings("unchecked") var refrigerator = (Storage<CanReproduceFood>) Mockito.mock(Storage.class);
        @SuppressWarnings("unchecked") var innerHandler = (HandlerByAge<CanReproduceFood>) Mockito.mock(HandlerByAge.class);
        var handleNewCall = new boolean[]{false};
        var handleMiddleCall = new boolean[]{false};
        var handleOldCall = new boolean[]{false};
        var handleExpiredCall = new boolean[]{false};
        var movedToRefrigerator = new boolean[]{false};
        Mockito.doAnswer(i -> handleNewCall[0] = true).when(innerHandler).handleNew(food);
        Mockito.doAnswer(i -> handleMiddleCall[0] = true).when(innerHandler).handleMiddle(food);
        Mockito.doAnswer(i -> handleOldCall[0] = true).when(innerHandler).handleOld(food);
        Mockito.doAnswer(i -> handleExpiredCall[0] = true).when(innerHandler).handleExpired(food);
        Mockito.doAnswer(i -> movedToRefrigerator[0] = true).when(refrigerator).add(food);
        // just to assure
        assertFalse(handleNewCall[0]);
        assertFalse(handleMiddleCall[0]);
        assertFalse(handleOldCall[0]);
        assertFalse(handleExpiredCall[0]);
        assertFalse(movedToRefrigerator[0]);
        var handler = new CanReproduceFoodHandler<>(innerHandler, refrigerator);

        // handle new food
        handler.handleNew(food);
        assertTrue(handleNewCall[0]);
        assertFalse(handleMiddleCall[0]);
        assertFalse(handleOldCall[0]);
        assertFalse(handleExpiredCall[0]);
        assertFalse(movedToRefrigerator[0]);
        handleNewCall[0] = false; //reset
        // handle middle-age food
        handler.handleMiddle(food);
        assertFalse(handleNewCall[0]);
        assertTrue(handleMiddleCall[0]);
        assertFalse(handleOldCall[0]);
        assertFalse(handleExpiredCall[0]);
        assertFalse(movedToRefrigerator[0]);
        handleMiddleCall[0] = false; //reset
        // handle old food
        handler.handleOld(food);
        assertFalse(handleNewCall[0]);
        assertFalse(handleMiddleCall[0]);
        assertTrue(handleOldCall[0]);
        assertFalse(handleExpiredCall[0]);
        assertFalse(movedToRefrigerator[0]);
        handleOldCall[0] = false; //reset
        // handle expired REPRODUCIBLE food
        Mockito.when(food.isCanReproduce()).thenReturn(true);
        handler.handleExpired(food);
        assertFalse(handleNewCall[0]);
        assertFalse(handleMiddleCall[0]);
        assertFalse(handleOldCall[0]);
        assertFalse(handleExpiredCall[0]);
        assertTrue(movedToRefrigerator[0]);
        movedToRefrigerator[0] = false; //reset
        // handle expired NON-REPRODUCIBLE food
        Mockito.when(food.isCanReproduce()).thenReturn(false);
        handler.handleExpired(food);
        assertFalse(handleNewCall[0]);
        assertFalse(handleMiddleCall[0]);
        assertFalse(handleOldCall[0]);
        assertTrue(handleExpiredCall[0]);
        assertFalse(movedToRefrigerator[0]);
        handleExpiredCall[0] = false; //reset
    }
}