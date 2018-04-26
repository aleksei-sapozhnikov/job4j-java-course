package ru.job4j.bomberman;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Board {
    private final Lock[][] cells;
    private final int width;
    private final int height;

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

    public boolean tryLock(int x, int y) throws InterruptedException {
        return this.isInBoard(x, y)
                && this.cells[x][y].tryLock(500, MILLISECONDS);
    }

    public void lock(int x, int y) throws WrongCoordinatesException {
        if (!this.isInBoard(x, y)) {
            throw new WrongCoordinatesException("Cannot lock cell - wrong coordinates.");
        }
        this.cells[x][y].lock();
    }

    public boolean isInBoard(int x, int y) {
        return x >= 0 && x < this.width
                && y >= 0 && y < this.height;
    }

    public void unlock(int x, int y) {
        if (this.isInBoard(x, y)) {
            this.cells[x][y].unlock();
        }
    }
}
