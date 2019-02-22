package ru.job4j.tictactoe.cell;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void whenCellFactoryMethodThenRightCell() {
        ICellFactory factory = Cell.FREE;
        assertSame(factory.getFree(), Cell.FREE);
        assertSame(factory.getMarkX(), Cell.MARK_X);
        assertSame(factory.getMarkO(), Cell.MARK_O);
    }

    @Test
    public void whenTestMarkThenReturnsRight() {
        assertFalse(Cell.FREE.hasMarkX());
        assertFalse(Cell.FREE.hasMarkO());
        //
        assertTrue(Cell.MARK_X.hasMarkX());
        assertFalse(Cell.MARK_X.hasMarkO());
        //
        assertFalse(Cell.MARK_O.hasMarkX());
        assertTrue(Cell.MARK_O.hasMarkO());
    }
}