package ru.job4j.tictactoe.printer;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tictactoe.cell.ICell;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PrinterTest {

    private final ICell free = Mockito.mock(ICell.class);
    private final ICell markX = Mockito.mock(ICell.class);
    private final ICell markO = Mockito.mock(ICell.class);

    public PrinterTest() {
        this.initCells();
    }

    private void initCells() {
        Mockito.when(this.free.hasMarkX()).thenReturn(false);
        Mockito.when(this.free.hasMarkO()).thenReturn(false);
        Mockito.when(this.markX.hasMarkX()).thenReturn(true);
        Mockito.when(this.markX.hasMarkO()).thenReturn(false);
        Mockito.when(this.markO.hasMarkX()).thenReturn(false);
        Mockito.when(this.markO.hasMarkO()).thenReturn(true);
    }

    @Test
    public void whenDifferentCellsThenResult() {
        var table = new ICell[][]{
                new ICell[]{this.free, this.free, this.free},
                new ICell[]{this.markX, this.free, this.markO},
                new ICell[]{this.markO, this.markX, this.markO}
        };
        var result = new Printer().print(table);
        var expected = String.join(System.lineSeparator(),
                "---",
                "X-O",
                "OXO"
        );
        assertThat(result, is(expected));
    }

    @Test
    public void whenEmptyTableThenIllegalArgumentException() {
        var table = new ICell[][]{};
        var wasException = false;
        try {
            new Printer().print(table);
        } catch (IllegalArgumentException e) {
            wasException = true;
        }
        assertTrue(wasException);
    }

    @Test
    public void whenRowHasLengthZeroThenIllegalArgumentException() {
        var table = new ICell[][]{new ICell[0], new ICell[0]};
        var wasException = false;
        try {
            new Printer().print(table);
        } catch (IllegalArgumentException e) {
            wasException = true;
        }
        assertTrue(wasException);
    }

    @Test
    public void whenHasNullValuesIllegalArgumentException() {
        var table = new ICell[][]{new ICell[]{this.free, this.markO, null}};
        var wasException = false;
        try {
            new Printer().print(table);
        } catch (IllegalArgumentException e) {
            wasException = true;
        }
        assertTrue(wasException);
    }
}