package ru.job4j.bomberman;

import static ru.job4j.bomberman.Direction.*;

public class Character {
    private final Board board;
    private final int id;
    private final String name;
    private final int x;
    private final int y;

    public Character(Board board, int id, String name, int x, int y) {
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

    public void init() throws WrongCoordinatesException {
        this.board.lock(this.x, this.y);
        System.out.format("    %s appeared on board (%s, %s).%n", this.name, this.x, this.y);
    }

    public Character move(Direction direction) throws InterruptedException {
        Character result = this;
        int nextX = this.nextX(direction);
        int nextY = this.nextY(direction);
        boolean acquired = this.board.tryLock(nextX, nextY);
        if (acquired) {
            result = new Character(this.board, this.id, this.name, nextX, nextY);
            this.board.unlock(this.x, this.y);
        } else {
            System.out.format("        %s: direction %s (%s, %s) locked%n", this.name(), direction.toString(), nextX, nextY);
        }
        return result;
    }

    int nextX(Direction direction) {
        int result;
        if (direction == UP || direction == DOWN) {
            result = this.x;
        } else {
            result = direction == RIGHT ? this.x + 1 : this.x - 1;
        }
        return result;
    }

    int nextY(Direction direction) {
        int result;
        if (direction == LEFT || direction == RIGHT) {
            result = this.y;
        } else {
            result = direction == DOWN ? this.y + 1 : this.y - 1;
        }
        return result;
    }
}
