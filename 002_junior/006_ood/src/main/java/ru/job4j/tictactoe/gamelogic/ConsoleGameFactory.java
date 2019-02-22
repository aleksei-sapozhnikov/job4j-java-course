package ru.job4j.tictactoe.gamelogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.board.IBoard;
import ru.job4j.tictactoe.player.IPlayer;
import ru.job4j.tictactoe.printer.IPrinter;
import ru.job4j.tictactoe.winchecker.IWinChecker;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory of console-based game logic.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ConsoleGameFactory implements IGameFactory {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ConsoleGameFactory.class);
    /**
     * Supplies human player.
     */
    private final Supplier<IPlayer> humanPlayerSupplier;
    /**
     * Supplies computer player.
     */
    private final Supplier<IPlayer> computerPlayerSupplier;
    /**
     * Supplies game board.
     */
    private final BiFunction<Integer, Integer, IBoard> boardSupplier;
    /**
     * Supplies win checking algorithm.
     */
    private final Function<Integer, IWinChecker> winCheckerSupplier;
    /**
     * Supplies field printer.
     */
    private final Supplier<IPrinter> printerSupplier;

    /**
     * @param humanPlayerSupplier    Supplies human player.
     * @param computerPlayerSupplier Supplies computer player.
     * @param boardSupplier          Supplies game board.
     * @param winCheckerSupplier     Supplies win checking algorithm.
     * @param printerSupplier        Supplies field printer.
     */
    public ConsoleGameFactory(Supplier<IPlayer> humanPlayerSupplier,
                              Supplier<IPlayer> computerPlayerSupplier,
                              BiFunction<Integer, Integer, IBoard> boardSupplier,
                              Function<Integer, IWinChecker> winCheckerSupplier,
                              Supplier<IPrinter> printerSupplier
    ) {
        this.humanPlayerSupplier = humanPlayerSupplier;
        this.computerPlayerSupplier = computerPlayerSupplier;
        this.boardSupplier = boardSupplier;
        this.winCheckerSupplier = winCheckerSupplier;
        this.printerSupplier = printerSupplier;
    }

    /**
     * Returns game where first move is by human, second by computer.
     *
     * @param boardHeight Needed board height.
     * @param boardWidth  Needed board height.
     * @param winSize     Needed size of winning combination (e.g. length of line).
     * @return GameLogic object.
     */
    @Override
    public IGameLogic humanThenComputer(int boardHeight, int boardWidth, int winSize) {
        return new GameLogic(
                this.humanPlayerSupplier.get(),
                this.computerPlayerSupplier.get(),
                this.boardSupplier.apply(boardHeight, boardWidth),
                this.winCheckerSupplier.apply(winSize),
                this.printerSupplier.get()
        );
    }

    /**
     * Returns game where first move is by computer, second by human.
     *
     * @param boardHeight Needed board height.
     * @param boardWidth  Needed board height.
     * @param winSize     Needed size of winning combination (e.g. length of line).
     * @return GameLogic object.
     */
    @Override
    public IGameLogic computerThenHuman(int boardHeight, int boardWidth, int winSize) {
        return new GameLogic(
                this.computerPlayerSupplier.get(),
                this.humanPlayerSupplier.get(),
                this.boardSupplier.apply(boardHeight, boardWidth),
                this.winCheckerSupplier.apply(winSize),
                this.printerSupplier.get()
        );
    }
}
