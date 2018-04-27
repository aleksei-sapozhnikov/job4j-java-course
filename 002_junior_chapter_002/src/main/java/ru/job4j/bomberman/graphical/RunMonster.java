package ru.job4j.bomberman.graphical;

import ru.job4j.bomberman.WrongCoordinatesException;

public class RunMonster implements Runnable {
    private GraphicalPersonage gPersonage;
    private boolean working = true;

    public RunMonster(GraphicalPersonage gPersonage) {
        this.gPersonage = gPersonage;
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
