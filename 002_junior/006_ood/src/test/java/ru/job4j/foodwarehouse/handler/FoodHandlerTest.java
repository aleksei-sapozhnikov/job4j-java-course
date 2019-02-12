package ru.job4j.foodwarehouse.handler;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.foodwarehouse.food.AbstractFood;
import ru.job4j.foodwarehouse.storage.Storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FoodHandlerTest {

    @Test
    public void testHandling() {
        // data needed to run control object
        @SuppressWarnings("unchecked") var warehouse = (Storage<AbstractFood>) Mockito.mock(Storage.class);
        @SuppressWarnings("unchecked") var shop = (Storage<AbstractFood>) Mockito.mock(Storage.class);
        @SuppressWarnings("unchecked") var trash = (Storage<AbstractFood>) Mockito.mock(Storage.class);
        var discount = 5;
        var food = Mockito.mock(AbstractFood.class);
        // assign actions
        var takenToWarehouse = new boolean[]{false};
        var takenToShop = new boolean[]{false};
        var takenToTrash = new boolean[]{false};
        var setDiscount = new boolean[]{false};
        Mockito.doAnswer(i -> takenToWarehouse[0] = true).when(warehouse).add(food);
        Mockito.doAnswer(i -> takenToShop[0] = true).when(shop).add(food);
        Mockito.doAnswer(i -> takenToTrash[0] = true).when(trash).add(food);
        Mockito.doAnswer(i -> setDiscount[0] = true).when(food).setDiscount(discount);
        // just to assure
        assertFalse(takenToWarehouse[0]);
        assertFalse(takenToShop[0]);
        assertFalse(takenToTrash[0]);
        assertFalse(setDiscount[0]);
        // begin checks
        var handler = new FoodHandler<>(warehouse, shop, trash, discount);
        // food to warehouse
        handler.handleNew(food);
        assertTrue(takenToWarehouse[0]);
        assertFalse(takenToShop[0]);
        assertFalse(takenToTrash[0]);
        assertFalse(setDiscount[0]);
        takenToWarehouse[0] = false; // reset
        // food to shop
        handler.handleMiddle(food);
        assertFalse(takenToWarehouse[0]);
        assertTrue(takenToShop[0]);
        assertFalse(takenToTrash[0]);
        assertFalse(setDiscount[0]);
        takenToShop[0] = false; // reset
        // food to shop with discount
        handler.handleOld(food);
        assertFalse(takenToWarehouse[0]);
        assertTrue(takenToShop[0]);
        assertFalse(takenToTrash[0]);
        assertTrue(setDiscount[0]);
        takenToShop[0] = false; // reset
        setDiscount[0] = false; // reset
        // food to trash
        handler.handleExpired(food);
        assertFalse(takenToWarehouse[0]);
        assertFalse(takenToShop[0]);
        assertTrue(takenToTrash[0]);
        assertFalse(setDiscount[0]);
    }


    @Test
    public void handleOld() {
    }

    @Test
    public void handleExpired() {
    }
}