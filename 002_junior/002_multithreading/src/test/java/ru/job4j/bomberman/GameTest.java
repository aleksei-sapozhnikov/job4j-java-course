package ru.job4j.bomberman;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.bomberman.Direction.RIGHT;

public class GameTest {
    /**
     * Object to synchronize threads in tests.
     */
    private final Object sync = new Object();

    /**
     * Just run: only player automatically moving.
     */
    @Test
    public void notATestJustRunOnlyPlayer() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(2, 3);
        Game game = new Game();
        game.addAutomatic(new Personage(board, 0, "Player", 0, 0));
        game.startGame();
        Thread.sleep(6000);
        game.stopAutomatics();
    }

    /**
     * Just run to try adding blocks, automatics, starting and stopping game.
     */
    @Test
    public void notATestJustRun() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(5, 1);
        Game game = new Game();
        game.addBlock(new Personage(board, 1, "Block", 2, 0));
        game.addAutomatic(new Personage(board, 0, "Player", 0, 0));
        game.addAutomatic(new Personage(board, 1, "Monster", 4, 0));
        game.startGame();
        Thread.sleep(6000);
        game.stopAutomatics();
    }

    /**
     * Test addBlock() and addAutomatic()
     */
    @Test
    public void whenSameIdForPersonageOrBlockThenFalse() {
        Board board = new Board(3, 3);
        Game game = new Game();
        assertThat(game.addBlock(new Personage(board, 1, "Block1", 0, 2)), is(true));
        assertThat(game.addBlock(new Personage(board, 1, "Block2", 1, 3)), is(false));
        assertThat(game.addAutomatic(new Personage(board, 1, "Auto1", 0, 2)), is(true));
        assertThat(game.addAutomatic(new Personage(board, 1, "Auto2", 1, 3)), is(false));
    }

    /**
     * Test placeBlocks()
     */
    @Test
    public void whenBlockPlacedThenCannotMovePersonageToThatCell() throws WrongCoordinatesException, InterruptedException {
        Board board = new Board(3, 3);
        Game game = new Game();
        int[] counter = {0};
        // Game starts in other thread
        new Thread(() -> {
            try {
                synchronized (this.sync) {
                    game.addBlock(new Personage(board, 1, "Block1", 1, 1));
                    game.startGame();
                    counter[0] = 1;
                    this.sync.notify();
                }
            } catch (WrongCoordinatesException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // waiting for game to start
        synchronized (this.sync) {
            while (counter[0] != 1) {
                this.sync.wait();
            }
        }
        // main-thread personage tries to enter the game
        Personage trying = new Personage(board, 2, "Trying", 0, 1);
        assertThat(trying.tryMove(RIGHT) == trying, is(true));
    }
}