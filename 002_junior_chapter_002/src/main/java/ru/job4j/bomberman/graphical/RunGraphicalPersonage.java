package ru.job4j.bomberman.graphical;

import ru.job4j.bomberman.Direction;
import ru.job4j.bomberman.WrongCoordinatesException;

import java.util.Random;

public class RunGraphicalPersonage implements Runnable {
    private final Direction[] directions = Direction.values();
    private final Random random;
    private GraphicalPersonage gPersonage;
    private boolean working = true;

    public RunGraphicalPersonage(GraphicalPersonage gPersonage) {
        this.gPersonage = gPersonage;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            this.gPersonage.place();
            while (working && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                this.gPersonage = this.gPersonage.nextMove();
            }
        } catch (InterruptedException | WrongCoordinatesException e) {
            this.working = false;
        } finally {
            this.working = false;
        }
    }
}
