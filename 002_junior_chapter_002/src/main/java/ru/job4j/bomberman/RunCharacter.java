package ru.job4j.bomberman;

import java.util.Random;

public class RunCharacter implements Runnable {
    private final Direction[] directions = Direction.values();
    private final Random random;
    private Character character;
    private boolean working = true;

    public RunCharacter(Character character) {
        this.character = character;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            this.character.init();
            while (working && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
                this.move();
            }
        } catch (InterruptedException e) {
            this.working = false;
        } catch (WrongCoordinatesException e) {
            System.out.format("            %s: %s caught WrongCoordinatesException (%s, %s), stopping.%n",
                    Thread.currentThread().getName(), this.character.name(), this.character.x(), this.character.y());
        } finally {
            this.working = false;
        }
    }

    private void move() throws InterruptedException {
        Character after;
        Direction direction;
        do {
            int i = random.nextInt(4);
            direction = this.directions[i];
            after = this.character.move(direction);
        } while (after == this.character);
        this.character = after;
        System.out.format("%s moved %s: now on (%s, %s)%n", this.character.name(), direction.toString(), this.character.x(), this.character.y());
        Thread.sleep(1000);
    }
}
