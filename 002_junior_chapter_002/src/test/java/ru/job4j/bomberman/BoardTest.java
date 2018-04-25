package ru.job4j.bomberman;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Board class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.04.2018
 */
public class BoardTest {

    /**
     * Test inBoard()
     */
    @Test
    public void whenCoordinatesInBoardThenTrueElseFalse() {
        Board board = new Board(2, 3);
        int[][] yes = {{0, 0}, {0, 1}, {1, 2}, {1, 0}, {0, 2}};
        int[][] no = {{-1, 0}, {1, -1}, {2, 1}, {1, 4}, {3, 5}};
        for (int[] y : yes) {
            assertThat(board.inBoard(y[0], y[1]), is(true));
        }
        for (int[] n : no) {
            assertThat(board.inBoard(n[0], n[1]), is(false));
        }
    }

    /**
     * Test tryLock()
     */
    @Test
    public void whenTryLockCoordinatesNotInBoardThenFalse() throws InterruptedException {
        Board board = new Board(2, 3);
        assertThat(board.tryLock(-1, 0), is(false));
        assertThat(board.tryLock(1, 4), is(false));
        assertThat(board.tryLock(3, 1), is(false));
    }

    @Test
    public void whenTryLockCelLLockedByAnotherThreadThenFalseOnlyForThisCell() throws InterruptedException {
        Board board = new Board(2, 3);
        new Thread(() -> {
            try {
                board.tryLock(1, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(100);
        assertThat(board.tryLock(1, 1), is(false));
        assertThat(board.tryLock(1, 0), is(true));
        assertThat(board.tryLock(0, 1), is(true));
    }

    /**
     * Test lock()
     */
    @Test(expected = WrongCoordinatesException.class)
    public void whenLockCellInBoardThenTrueElseException() throws WrongCoordinatesException, InterruptedException {
        Board board = new Board(2, 2);
        board.lockInterruptibly(0, 0);
        board.lockInterruptibly(1, 1);
        board.lockInterruptibly(2, 1); // out of board
    }

    /**
     * Test unlock()
     */
    @Test
    public void whenUnlockLockedCellThenTryLockTrueAgain() throws InterruptedException {
        Board board = new Board(2, 2);
        new Thread(() -> {
            try {
                board.tryLock(0, 0);
                board.lockInterruptibly(0, 1);
                Thread.sleep(2000);
                board.unlock(0, 0);
                board.unlock(0, 1);
            } catch (InterruptedException | WrongCoordinatesException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        assertThat(board.tryLock(0, 0), is(false));
        assertThat(board.tryLock(0, 1), is(false));
        Thread.sleep(2000);
        assertThat(board.tryLock(0, 0), is(true));
        assertThat(board.tryLock(0, 1), is(true));
    }

    @Test(expected = WrongCoordinatesException.class)
    public void whenUnlockCellNotInBoardThenException() throws WrongCoordinatesException {
        Board board = new Board(2, 2);
        board.unlock(0, 3);
    }
}