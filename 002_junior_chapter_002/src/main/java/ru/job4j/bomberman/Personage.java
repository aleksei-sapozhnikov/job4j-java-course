package ru.job4j.bomberman;

import java.util.Random;

import static ru.job4j.bomberman.Direction.*;

/**
 * Class for a personage moving and locking cell
 * on board (player, monster or block).
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.04.2018
 */
public class Personage {
    /**
     * Playing board where the personage stays.
     */
    private final Board board;
    /**
     * Personage unique id.
     */
    private final int id;
    /**
     * Personage name.
     */
    private final String name;
    /**
     * Personage "x" (horizontal) coordinate (in board cells).
     */
    private final int x;
    /**
     * Personage "y" (vertical) coordinate (in board cells).
     */
    private final int y;
    /**
     * Possible directions to move using random move.
     */
    private final Direction[] directions = Direction.values();

    /**
     * Constructs new Personage.
     *
     * @param board playing board where the personage stays.
     * @param id    unique id.
     * @param name  personage name.
     * @param x     "x" (horizontal) coordinate (in board cells).
     * @param y     "y"  (vertical) coordinate (in board cells).
     */
    public Personage(Board board, int id, String name, int x, int y) {
        this.board = board;
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns personage id.
     *
     * @return personage id.
     */
    public int id() {
        return this.id;
    }

    /**
     * Returns personage name.
     *
     * @return personage name.
     */
    public String name() {
        return this.name;
    }

    /**
     * Returns "x" (horizontal) coordinate (in board cells).
     *
     * @return "x" (horizontal) coordinate (in board cells).
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns "y"  (vertical) coordinate (in board cells).
     *
     * @return "y"  (vertical) coordinate (in board cells).
     */
    public int y() {
        return this.y;
    }

    /**
     * Places this personage on board (locks his cell).
     *
     * @throws WrongCoordinatesException if personage coordinates do not point to a cell in board.
     */
    public void place() throws WrongCoordinatesException {
        this.board.lock(this.x, this.y);
    }

    /**
     * Makes random move attempts until move is variants. Makes this variants move.
     *
     * @return new Personage staying on new coordinates (after the move made).
     * @throws InterruptedException      if interrupted while trying to lock a destination cell.
     * @throws WrongCoordinatesException if during unlock attempt personage coordinates do not point to a cell in board.
     */
    public Personage randomMove() throws InterruptedException, WrongCoordinatesException {
        Personage after;
        do {
            Direction direction = this.directions[new Random().nextInt(this.directions.length)];
            after = this.tryMove(direction);
        } while (after == this);
        return after;
    }

    /**
     * Tries to mae a move in given direction.
     *
     * @param direction direction where to try move in.
     * @return new Personage with new coordinates if move was made successfully
     * or this (old) personage if move was not possible and coordinates didn't change.
     * @throws InterruptedException if was interrupted while tryLock operation in board.
     */
    public Personage tryMove(Direction direction) throws InterruptedException, WrongCoordinatesException {
        boolean acquired = false;
        try {
            int nextX = this.nextX(direction);
            int nextY = this.nextY(direction);
            acquired = this.board.tryLock(nextX, nextY);
            return acquired ? new Personage(this.board, this.id, this.name, nextX, nextY) : this;
        } finally {
            if (acquired) {
                this.board.unlock(this.x, this.y);
            }
        }
    }

    /**
     * Calculates next "x" (horizontal) coordinate if moving in given direction.
     *
     * @param direction direction to move in.
     * @return next "x" (horizontal) coordinate.
     */
    private int nextX(Direction direction) {
        int result;
        if (direction == UP || direction == DOWN) {
            result = this.x;
        } else {
            result = direction == RIGHT ? this.x + 1 : this.x - 1;
        }
        return result;
    }

    /**
     * Calculates next "y" (vertical) coordinate if moving in given direction.
     *
     * @param direction direction to move in.
     * @return next "y" (vertical) coordinate.
     */
    private int nextY(Direction direction) {
        int result;
        if (direction == LEFT || direction == RIGHT) {
            result = this.y;
        } else {
            result = direction == DOWN ? this.y + 1 : this.y - 1;
        }
        return result;
    }
}
