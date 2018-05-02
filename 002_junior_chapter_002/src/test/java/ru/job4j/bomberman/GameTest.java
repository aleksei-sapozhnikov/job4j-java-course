package ru.job4j.bomberman;

import org.junit.Test;

public class GameTest {

    @Test
    public void runSomethingPlease() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(5, 4);
        Game game = new Game();
        game.addBlock(new Personage(board, 1, "Block_1", 0, 2));
        game.addBlock(new Personage(board, 2, "Block_2", 1, 2));
        game.addAutomatic(new Personage(board, 0, "Player", 0, 1));
        game.addAutomatic(new Personage(board, 1, "Monster_1", 0, 0));
        game.addAutomatic(new Personage(board, 2, "Monster_2", 1, 0));
        game.addAutomatic(new Personage(board, 3, "Monster_3", 2, 0));
        game.startGame();
        Thread.sleep(6000);
        game.stopAutomatics();
    }
}