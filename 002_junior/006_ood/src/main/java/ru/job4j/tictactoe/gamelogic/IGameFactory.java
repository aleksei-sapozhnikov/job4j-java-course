package ru.job4j.tictactoe.gamelogic;

/**
 * Factory of tic tac toe game logic.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IGameFactory {

    /**
     * Returns game where first move is by human, second by computer.
     *
     * @param boardHeight Needed board height.
     * @param boardWidth  Needed board height.
     * @param winSize     Needed size of winning combination (e.g. length of line).
     * @return GameLogic object.
     */
    IGameLogic humanThenComputer(int boardHeight, int boardWidth, int winSize);

    /**
     * Returns game where first move is by computer, second by human.
     *
     * @param boardHeight Needed board height.
     * @param boardWidth  Needed board height.
     * @param winSize     Needed size of winning combination (e.g. length of line).
     * @return GameLogic object.
     */
    IGameLogic computerThenHuman(int boardHeight, int boardWidth, int winSize);
}
