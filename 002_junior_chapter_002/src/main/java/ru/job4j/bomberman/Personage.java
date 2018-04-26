package ru.job4j.bomberman;

import java.util.Random;

import static ru.job4j.bomberman.Direction.*;

public class Personage {
    private final Board board;
    private final int id;
    private final String name;
    private final int x;
    private final int y;

    public Personage(Board board, int id, String name, int x, int y) {
        this.board = board;
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public void place() throws WrongCoordinatesException {
        this.board.lock(this.x, this.y);
        System.out.format("+ %s on board (%s, %s)%n", this.name, this.x, this.y);
    }

    public Personage nextMove(Direction[] possible) throws InterruptedException {
        boolean moved = false;
        Personage after = this;
        Direction direction = null;
        try {
            Random random = new Random();
            while (after == this) {
                direction = possible[random.nextInt(possible.length)];
                after = this.tryMove(direction);
            }
            moved = true;
            return after;
        } finally {
            if (moved) {
                System.out.format("%s moved %s: (%s, %s) --> (%s, %s)%n", this.name, direction, this.x, this.y, after.x, after.y);
                System.out.flush();
                this.board.unlock(this.x, this.y);
            }
        }
    }

    public Personage tryMove(Direction direction) throws InterruptedException {
        Personage result = this;
        int nextX = this.nextX(direction);
        int nextY = this.nextY(direction);
        boolean acquired = this.board.tryLock(nextX, nextY);
        if (acquired) {
            result = new Personage(this.board, this.id, this.name, nextX, nextY);
        }
        return result;
    }

    private int nextX(Direction direction) {
        int result;
        if (direction == UP || direction == DOWN) {
            result = this.x;
        } else {
            result = direction == RIGHT ? this.x + 1 : this.x - 1;
        }
        return result;
    }

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
