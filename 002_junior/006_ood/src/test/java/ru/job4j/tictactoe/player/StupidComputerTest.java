package ru.job4j.tictactoe.player;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tictactoe.cell.ICell;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StupidComputerTest {

    private final ICell free = Mockito.mock(ICell.class);
    private final ICell markX = Mockito.mock(ICell.class);
    private final ICell markO = Mockito.mock(ICell.class);

    public StupidComputerTest() {
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
    public void whenEmptyTableTopLeftCell() {
        ICell[][] cells = {
                {this.free, this.free, this.free},
                {this.free, this.free, this.free}
        };
        var result = new StupidComputer().doMove(cells);
        var expected = new IPlayer.Move(0, 0);
        assertThat(result, is(expected));
    }

    @Test
    public void whenNotEmptyTableThenFirstFreeTopLeftCell() {
        ICell[][] cells = {
                {this.markO, this.markX, this.free},
                {this.markO, this.free, this.markX}
        };
        var result = new StupidComputer().doMove(cells);
        var expected = new IPlayer.Move(0, 2);
        assertThat(result, is(expected));
    }
}