package ru.job4j.bomberman;

public class RunPersonage implements Runnable {
    private Personage personage;
    private boolean working = true;

    public RunPersonage(Personage personage) {
        this.personage = personage;
    }

    @Override
    public void run() {
        try {
            this.personage.place();
            while (working && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                this.personage = this.personage.nextMove(Direction.values());
            }
        } catch (InterruptedException | WrongCoordinatesException e) {
            this.working = false;
        } finally {
            this.working = false;
        }
    }
}
