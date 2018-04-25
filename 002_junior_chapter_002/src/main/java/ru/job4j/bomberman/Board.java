package ru.job4j.bomberman;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Board {
    private final Lock[][] board;
    private final int xMax;
    private final int yMax;

    public Board(int xMax, int yMax) {
        this.board = new ReentrantLock[xMax + 1][yMax + 1];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                board[x][y] = new ReentrantLock();
            }
        }
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public boolean tryLock(int x, int y) throws InterruptedException {
        return this.isInBoard(x, y)
                && this.board[x][y].tryLock(500, MILLISECONDS);
    }

    public void lock(int x, int y) throws WrongCoordinatesException {
        if (!this.isInBoard(x, y)) {
            throw new WrongCoordinatesException("Cannot lock cell - wrong coordinates.");
        }
        this.board[x][y].lock();
    }

    public boolean isInBoard(int x, int y) {
        return x >= 0 && x <= this.xMax
                && y >= 0 && y <= this.yMax;
    }

    public void unlock(int x, int y) {
        if (this.isInBoard(x, y)) {
            this.board[x][y].unlock();
        }
    }
}
