package ru.job4j.tictactoe.userinterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.WrongInputException;
import ru.job4j.tictactoe.gamelogic.IGameFactory;
import ru.job4j.tictactoe.gamelogic.IGameLogic;
import ru.job4j.util.function.ConsumerEx;
import ru.job4j.util.function.SupplierEx;

/**
 * Console-based user interface.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class UserInterface implements IUserInterface {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UserInterface.class);

    /**
     * User commands supplier.
     */
    private final SupplierEx<String> in;
    /**
     * To-user messages consumer.
     */
    private final ConsumerEx<String> out;
    /**
     * Game logic.
     */
    private final IGameLogic game;

    /**
     * Flag showing if first or second player is moving now.
     */
    private boolean firstPlayerMove = true;

    /**
     * Constructor.
     *
     * @param in      User commands supplier.
     * @param out     To-user messages consumer.
     * @param factory ame logic.
     * @throws Exception In case of I/O problems.
     */
    public UserInterface(SupplierEx<String> in, ConsumerEx<String> out, IGameFactory factory) throws Exception {
        this.in = in;
        this.out = out;
        int boardSize = this.getBoardSize();
        int winSize = this.getWinSize(boardSize);
        this.game = this.getGame(factory, boardSize, winSize);
    }

    /**
     * Returns board size defined by user.
     *
     * @return Board size.
     * @throws Exception In case of I/O problems.
     */
    private int getBoardSize() throws Exception {
        String input;
        do {
            this.out.accept("Enter game field size (e.g.: 3)");
            input = this.in.get();
        } while (!(input.matches("\\d+")));
        return Integer.parseInt(input);
    }

    /**
     * Returns winning combination size defined by user.
     *
     * @param boardSize Board size.
     * @return Winning combination size.
     * @throws Exception In case of I/O problems.
     */
    private int getWinSize(int boardSize) throws Exception {
        String input;
        int winSize = Integer.MAX_VALUE;
        do {
            this.out.accept(String.format(
                    "Enter winning line size (e.g.: 3). Must be: 0 < winSize <= boardSize (%s)", boardSize));
            input = this.in.get();
            if (input.matches("\\d+")) {
                winSize = Integer.parseInt(input);
            }
        } while (winSize < 0 || winSize > boardSize);
        return winSize;
    }

    /**
     * Returns game logic defined by user.
     *
     * @param factory   Game logic factory.
     * @param boardSize Board size.
     * @param winSize   Winning combination size;
     * @return Game logic object.
     * @throws Exception In case of I/O problems.
     */
    private IGameLogic getGame(IGameFactory factory, int boardSize, int winSize) throws Exception {
        String input;
        do {
            this.out.accept("Choose first: player (p) or computer (c)");
            input = this.in.get();
        } while (!("p".equals(input) || "c".equals(input)));
        return "p".equals(input)
                ? factory.humanThenComputer(boardSize, boardSize, winSize)
                : factory.computerThenHuman(boardSize, boardSize, winSize);
    }

    /**
     * Performs full game play.
     *
     * @throws Exception In case of I/O problems.
     */
    @Override
    public void play() throws Exception {
        this.printBeginGame();
        this.makeMoves();
        this.printEndGame();
    }

    /**
     * Prints initial messages at game start.
     *
     * @throws Exception In case of I/O problems.
     */
    private void printBeginGame() throws Exception {
        this.out.accept("Begin game:");
        this.out.accept(String.format("%s%n", this.game.printBoard()));
    }

    /**
     * Iteratively makes moves until game can be continued.
     *
     * @throws Exception In case of I/O problems.
     */
    private void makeMoves() throws Exception {
        while (this.game.canContinue()) {
            this.out.accept(this.firstPlayerMove ? "First player move:" : "Second player move:");
            this.doNextMove();
            this.out.accept(String.format("%s%n", this.game.printBoard()));
            this.firstPlayerMove = !firstPlayerMove;
        }
    }

    /**
     * Makes next move.
     *
     * @throws Exception In case of I/O problems.
     */
    private void doNextMove() throws Exception {
        var result = false;
        do {
            try {
                this.game.nextMove();
                result = true;
            } catch (WrongInputException e) {
                this.out.accept(String.format("Exception: %s", e.getMessage()));
            }
        } while (!result);
    }

    /**
     * Prints game result at the game end.
     *
     * @throws Exception In case of I/O problems.
     */
    private void printEndGame() throws Exception {
        if (this.game.isWinnerFirst()) {
            this.out.accept("First player wins!");
        } else if (this.game.isWinnerSecond()) {
            this.out.accept("Second player wins!");
        } else {
            this.out.accept("Field is full, game over");
        }
    }


}
