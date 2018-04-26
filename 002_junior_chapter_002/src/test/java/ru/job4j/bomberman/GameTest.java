package ru.job4j.bomberman;

import org.junit.Test;

public class GameTest {

    @Test
    public void runSomethingPlease() throws InterruptedException {
        Board board = new Board(3, 3);
        Game game = new Game();
        game.add(new Character(board, 1, "Player", 0, 0));
        game.add(new Character(board, 2, "Monster_1", 2, 0));
        game.add(new Character(board, 3, "Monster_2", 0, 0));
        game.start();
        Thread.sleep(120000);
        game.stop();
    }
}