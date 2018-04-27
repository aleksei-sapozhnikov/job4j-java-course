package ru.job4j.bomberman.graphical;

import javafx.scene.input.KeyCode;
import ru.job4j.bomberman.Direction;
import ru.job4j.bomberman.WrongCoordinatesException;

public class RunPlayer implements Runnable {
    private GraphicalPersonage gPersonage;
    private boolean working = true;

    public RunPlayer(GraphicalPersonage gPersonage) {
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

    private void handleKeys(KeyCode key, GraphicalPersonage player) throws InterruptedException, WrongCoordinatesException {
        if (key == KeyCode.UP) {
            System.out.println("UP");
            player.tryMove(Direction.UP);
        } else if (key == KeyCode.RIGHT) {
            System.out.println("RIGHT");
            player.tryMove(Direction.RIGHT);
        } else if (key == KeyCode.DOWN) {
            System.out.println("DOWN");
            player.tryMove(Direction.DOWN);
        } else if (key == KeyCode.LEFT) {
            System.out.println("LEFT");
            player.tryMove(Direction.LEFT);
        }
    }

}
