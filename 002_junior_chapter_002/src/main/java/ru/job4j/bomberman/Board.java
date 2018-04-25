package ru.job4j.bomberman;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Playing board for Bomber-man game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.04.2018
 */
public class Board {
    /**
     * Table of cells.
     */
    private final Lock[][] cells;
    /**
     * Board width.
     */
    private final int width;
    /**
     * Board height.
     */
    private final int height;

    /**
     * Constructs new Board.
     *
     * @param width  board width (cells).
     * @param height board height (cells).
     */
    public Board(int width, int height) {
        this.cells = new ReentrantLock[width][height];
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                cells[x][y] = new ReentrantLock();
            }
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if given coordinates point to a cell in the board.
     *
     * @param x horizontal coordinate ("width").
     * @param y vertical coordinate ("height").
     * @return <tt>true</tt> if cell is in board, <tt>false</tt> if not.
     */
    public boolean inBoard(int x, int y) {
        return x >= 0 && x < this.width
                && y >= 0 && y < this.height;
    }

    /**
     * Tries to acquire lock of the cell given by coordinates for some time.
     * If couldn't acquire lock, returns <tt>false</tt>.
     *
     * @param x horizontal coordinate ("width").
     * @param y vertical coordinate ("height").
     * @return <tt>true</tt> if cell was locked, <tt>false</tt> if not.
     * @throws InterruptedException if was interrupted while waiting during tryLock operation.
     */
    public boolean tryLock(int x, int y) throws InterruptedException {
        return this.inBoard(x, y)
                && this.cells[x][y].tryLock(500, MILLISECONDS);
    }

    /**
     * Locks cell given by coordinates.
     * If cell is already locked, waits until it will be unlocked.
     *
     * @param x horizontal coordinate ("width").
     * @param y vertical coordinate ("height").
     * @throws WrongCoordinatesException if given coordinates do not point to any cell in the board.
     */
    public void lockInterruptibly(int x, int y) throws WrongCoordinatesException, InterruptedException {
        if (!this.inBoard(x, y)) {
            throw new WrongCoordinatesException("Cannot lock cell - wrong coordinates.");
        }
        this.cells[x][y].lockInterruptibly();
    }

    /**
     * Unlocks locked cell.
     *
     * @param x horizontal coordinate ("width").
     * @param y vertical coordinate ("height").
     * @throws WrongCoordinatesException if given coordinates do not point to any cell in the board.
     */
    public void unlock(int x, int y) throws WrongCoordinatesException {
        if (!this.inBoard(x, y)) {
            throw new WrongCoordinatesException("Cannot lock cell - wrong coordinates.");
        }
        this.cells[x][y].unlock();
    }
}
