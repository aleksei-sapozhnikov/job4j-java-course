package ru.job4j.tictactoe.winchecker;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tictactoe.cell.ICell;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DoLineWinCheckerTest {

    private final ICell free = Mockito.mock(ICell.class);
    private final ICell markX = Mockito.mock(ICell.class);
    private final ICell markO = Mockito.mock(ICell.class);

    public DoLineWinCheckerTest() {
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


    @Test(expected = IllegalArgumentException.class)
    public void whenTableLengthZeroThenIllegalArgumentException() {
        new DoLineWinChecker(3).isWinnerX(new ICell[0][0]);
    }


    /**
     * Tests with 3x3 field.
     */
    @Test
    public void whenXWinnerHorizontalThenFindsIt() {
        ICell[][] tableH1 = {
                {this.markX, this.markX, this.markX},
                {this.free, this.free, this.free},
                {this.free, this.free, this.free},
        };
        ICell[][] tableH2 = {
                {this.free, this.free, this.free},
                {this.markX, this.markX, this.markX},
                {this.free, this.free, this.free},
        };
        ICell[][] tableH3 = {
                {this.free, this.free, this.free},
                {this.free, this.free, this.free},
                {this.markX, this.markX, this.markX},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerX(tableH1), is(true));
        assertThat(logic.isWinnerO(tableH1), is(false));
        assertThat(logic.isWinnerX(tableH2), is(true));
        assertThat(logic.isWinnerO(tableH2), is(false));
        assertThat(logic.isWinnerX(tableH3), is(true));
        assertThat(logic.isWinnerO(tableH3), is(false));
    }

    @Test
    public void whenXWinnerVerticalThenFindsIt() {
        ICell[][] tableV1 = {
                {this.markX, this.free, this.free},
                {this.markX, this.free, this.free},
                {this.markX, this.free, this.free},
        };
        ICell[][] tableV2 = {
                {this.free, this.markX, this.free},
                {this.free, this.markX, this.free},
                {this.free, this.markX, this.free},
        };
        ICell[][] tableV3 = {
                {this.free, this.free, this.markX},
                {this.free, this.free, this.markX},
                {this.free, this.free, this.markX},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerX(tableV1), is(true));
        assertThat(logic.isWinnerO(tableV1), is(false));
        assertThat(logic.isWinnerX(tableV2), is(true));
        assertThat(logic.isWinnerO(tableV2), is(false));
        assertThat(logic.isWinnerX(tableV3), is(true));
        assertThat(logic.isWinnerO(tableV3), is(false));
    }

    @Test
    public void whenXWinnerDiagonalThenFindsIt() {
        ICell[][] tableD1 = {
                {this.markX, this.free, this.free},
                {this.free, this.markX, this.free},
                {this.free, this.free, this.markX},
        };
        ICell[][] tableD2 = {
                {this.free, this.free, this.markX},
                {this.free, this.markX, this.free},
                {this.markX, this.free, this.free},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerX(tableD1), is(true));
        assertThat(logic.isWinnerO(tableD1), is(false));
        assertThat(logic.isWinnerX(tableD2), is(true));
        assertThat(logic.isWinnerO(tableD2), is(false));
    }

    @Test
    public void whenXWinnerMixedThenFindsIt() {
        ICell[][] table = {
                {this.markX, this.free, this.free},
                {this.free, this.markX, this.free},
                {this.free, this.free, this.markX},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerX(table), is(true));
        assertThat(logic.isWinnerO(table), is(false));
    }

    @Test
    public void whenOWinnerHorizontalThenFindsIt() {
        // horizontal
        ICell[][] tableH1 = {
                {this.markO, this.markO, this.markO},
                {this.free, this.free, this.free},
                {this.free, this.free, this.free},
        };
        ICell[][] tableH2 = {
                {this.free, this.free, this.free},
                {this.markO, this.markO, this.markO},
                {this.free, this.free, this.free},
        };
        ICell[][] tableH3 = {
                {this.free, this.free, this.free},
                {this.free, this.free, this.free},
                {this.markO, this.markO, this.markO},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerO(tableH1), is(true));
        assertThat(logic.isWinnerX(tableH1), is(false));
        assertThat(logic.isWinnerO(tableH2), is(true));
        assertThat(logic.isWinnerX(tableH2), is(false));
        assertThat(logic.isWinnerO(tableH3), is(true));
        assertThat(logic.isWinnerX(tableH3), is(false));
    }

    @Test
    public void whenOWinnerVerticalThenFindsIt() {
        // vertical
        ICell[][] tableV1 = {
                {this.markO, this.free, this.free},
                {this.markO, this.free, this.free},
                {this.markO, this.free, this.free},
        };
        ICell[][] tableV2 = {
                {this.free, this.markO, this.free},
                {this.free, this.markO, this.free},
                {this.free, this.markO, this.free},
        };
        ICell[][] tableV3 = {
                {this.free, this.free, this.markO},
                {this.free, this.free, this.markO},
                {this.free, this.free, this.markO},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerO(tableV1), is(true));
        assertThat(logic.isWinnerX(tableV1), is(false));
        assertThat(logic.isWinnerO(tableV2), is(true));
        assertThat(logic.isWinnerX(tableV2), is(false));
        assertThat(logic.isWinnerO(tableV3), is(true));
        assertThat(logic.isWinnerX(tableV3), is(false));
    }

    @Test
    public void whenOWinnerDiagonalThenFindsIt() {
        // diagonal
        ICell[][] tableD1 = {
                {this.markO, this.free, this.free},
                {this.free, this.markO, this.free},
                {this.free, this.free, this.markO},
        };
        ICell[][] tableD2 = {
                {this.free, this.free, this.markO},
                {this.free, this.markO, this.free},
                {this.markO, this.free, this.free},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerO(tableD1), is(true));
        assertThat(logic.isWinnerX(tableD1), is(false));
        assertThat(logic.isWinnerO(tableD2), is(true));
        assertThat(logic.isWinnerX(tableD2), is(false));
    }

    @Test
    public void whenOWinnerMixedThenFindsIt() {
        // mixed
        ICell[][] table = {
                {this.markO, this.free, this.free},
                {this.markO, this.markX, this.free},
                {this.markO, this.free, this.markX},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerO(table), is(true));
        assertThat(logic.isWinnerX(table), is(false));
    }


    /**
     * Tests with other sizes
     */
    @Test
    public void whenXWinnerHorizontalOtherSizeThenFindsIt() {
        ICell[][] table = {
                {this.free, this.free, this.free, this.free},
                {this.free, this.markX, this.markX, this.free}
        };
        DoLineWinChecker logic2 = new DoLineWinChecker(2);
        assertThat(logic2.isWinnerX(table), is(true));
        assertThat(logic2.isWinnerO(table), is(false));
        DoLineWinChecker logic3 = new DoLineWinChecker(3);
        assertThat(logic3.isWinnerX(table), is(false));
        assertThat(logic3.isWinnerO(table), is(false));
    }

    @Test
    public void whenXWinnerVerticalOtherSizeThenFindsIt() {
        ICell[][] table = {
                {this.free, this.free, this.free},
                {this.free, this.markX, this.free},
                {this.free, this.markX, this.free},
                {this.free, this.free, this.free},
        };
        DoLineWinChecker logic2 = new DoLineWinChecker(2);
        assertThat(logic2.isWinnerX(table), is(true));
        assertThat(logic2.isWinnerO(table), is(false));
        DoLineWinChecker logic3 = new DoLineWinChecker(3);
        assertThat(logic3.isWinnerX(table), is(false));
        assertThat(logic3.isWinnerO(table), is(false));
    }

    @Test
    public void whenXWinnerDiagonalOtherSizeUpRightThenFindsIt() {
        ICell[][] table = {
                {this.free, this.free, this.free, this.free},
                {this.free, this.free, this.markX, this.free},
                {this.free, this.markX, this.free, this.free},
                {this.free, this.free, this.free, this.free},
        };
        DoLineWinChecker logic2 = new DoLineWinChecker(2);
        assertThat(logic2.isWinnerX(table), is(true));
        assertThat(logic2.isWinnerO(table), is(false));
        DoLineWinChecker logic3 = new DoLineWinChecker(3);
        assertThat(logic3.isWinnerX(table), is(false));
        assertThat(logic3.isWinnerO(table), is(false));
    }

    @Test
    public void whenXWinnerDiagonalOtherSizeDownLeftThenFindsIt() {
        ICell[][] table = {
                {this.free, this.free, this.free, this.free},
                {this.free, this.free, this.markX, this.free},
                {this.free, this.free, this.free, this.markX},
                {this.free, this.free, this.free, this.free},
        };
        DoLineWinChecker logic2 = new DoLineWinChecker(2);
        assertThat(logic2.isWinnerX(table), is(true));
        assertThat(logic2.isWinnerO(table), is(false));
        DoLineWinChecker logic3 = new DoLineWinChecker(3);
        assertThat(logic3.isWinnerX(table), is(false));
        assertThat(logic3.isWinnerO(table), is(false));
    }

    @Test
    public void whenNoWinnerThenFindsIt() {
        ICell[][] empty = {
                {this.free, this.free, this.free},
                {this.free, this.free, this.free},
                {this.free, this.free, this.free},
        };
        ICell[][] table1 = {
                {this.markX, this.markX, this.markO},
                {this.markO, this.markO, this.free},
                {this.free, this.markX, this.markX},
        };
        ICell[][] table2 = {
                {this.markO, this.markX, this.markX},
                {this.markX, this.markO, this.markO},
                {this.markO, this.markO, this.markX},
        };
        var logic = new DoLineWinChecker(3);
        assertThat(logic.isWinnerX(empty), is(false));
        assertThat(logic.isWinnerO(empty), is(false));
        assertThat(logic.isWinnerX(table1), is(false));
        assertThat(logic.isWinnerO(table1), is(false));
        assertThat(logic.isWinnerX(table2), is(false));
        assertThat(logic.isWinnerO(table2), is(false));
    }

    @Test
    public void whenHasGap() {
        ICell[][] table = {
                {this.markX, this.free, this.free},
                {this.free, this.markX, this.free},
                {this.free, this.free, this.markX},
        };
        DoLineWinChecker logic = new DoLineWinChecker(3);
        assertThat(logic.hasGap(table), is(true));
    }

}