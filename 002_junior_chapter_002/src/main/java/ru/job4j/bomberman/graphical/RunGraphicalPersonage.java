package ru.job4j.bomberman.graphical;

import ru.job4j.bomberman.Direction;
import ru.job4j.bomberman.WrongCoordinatesException;

import java.util.Random;

public class RunGraphicalPersonage implements Runnable {
    private final Direction[] directions = Direction.values();
    private final Random random;
    private GraphicalPersonage personage;
    private boolean working = true;

    public RunGraphicalPersonage(GraphicalPersonage personage) {
        this.personage = personage;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            this.personage.init();
            while (working && !Thread.currentThread().isInterrupted()) {
                this.nextMove();
            }
        } catch (InterruptedException | WrongCoordinatesException e) {
            this.working = false;
        } finally {
            this.working = false;
        }
    }

    private void nextMove() throws InterruptedException {
        Thread.sleep(750);
        GraphicalPersonage after;
        Direction direction;
        do {
            int i = random.nextInt(this.directions.length);
            direction = this.directions[i];
            after = this.personage.move(direction);
        } while (after == this.personage);
        this.personage = after;
    }
}
