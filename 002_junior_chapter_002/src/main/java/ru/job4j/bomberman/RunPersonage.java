package ru.job4j.bomberman;

import java.util.Random;

public class RunPersonage implements Runnable {
    private final Direction[] directions = Direction.values();
    private final Random random;
    private Personage personage;
    private boolean working = true;

    public RunPersonage(Personage personage) {
        this.personage = personage;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            this.personage.init();
            while (working && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                this.move();
            }
        } catch (InterruptedException e) {
            this.working = false;
        } catch (WrongCoordinatesException e) {
            System.out.format("            %s: %s caught WrongCoordinatesException (%s, %s), stopping.%n",
                    Thread.currentThread().getName(), this.personage.name(), this.personage.x(), this.personage.y());
        } finally {
            this.working = false;
        }
    }

    private void move() throws InterruptedException {
        Personage after;
        Direction direction;
        do {
            int i = random.nextInt(this.directions.length);
            direction = this.directions[i];
            after = this.personage.move(direction);
        } while (after == this.personage);
        this.personage = after;
        System.out.format("%s moved %s: now on (%s, %s)%n", this.personage.name(), direction.toString(), this.personage.x(), this.personage.y());
        Thread.sleep(1000);
    }
}
