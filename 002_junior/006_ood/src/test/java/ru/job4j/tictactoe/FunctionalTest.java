package ru.job4j.tictactoe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import ru.job4j.tictactoe.board.Board;
import ru.job4j.tictactoe.board.IBoard;
import ru.job4j.tictactoe.cell.Cell;
import ru.job4j.tictactoe.gamelogic.ConsoleGameFactory;
import ru.job4j.tictactoe.player.ConsoleInputHuman;
import ru.job4j.tictactoe.player.IPlayer;
import ru.job4j.tictactoe.player.StupidComputer;
import ru.job4j.tictactoe.printer.IPrinter;
import ru.job4j.tictactoe.printer.Printer;
import ru.job4j.tictactoe.userinterface.UserInterface;
import ru.job4j.tictactoe.winchecker.DoLineWinChecker;
import ru.job4j.tictactoe.winchecker.IWinChecker;
import ru.job4j.util.function.ConsumerEx;
import ru.job4j.util.function.SupplierEx;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Functional test. Tests that game works.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FunctionalTest {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(FunctionalTest.class);

    @Test
    public void integrativeCheckNoExceptions() throws Exception {
        Queue<String> input = new LinkedList<>(Arrays.asList(
                "3",
                "3",
                "p",
                "2 2",
                "2 1",
                "2 0"
        ));
        SupplierEx<String> in = input::poll;
        ConsumerEx<String> out = (s) -> {
        };

        Supplier<IPlayer> humanPlayerSupplier = () -> new ConsoleInputHuman(in, out);
        Supplier<IPlayer> computerPlayerSupplier = StupidComputer::new;
        BiFunction<Integer, Integer, IBoard> boardSupplier =
                (height, width) -> new Board(height, width, Cell.FREE);
        Function<Integer, IWinChecker> winCheckerSupplier = DoLineWinChecker::new;
        Supplier<IPrinter> printerSupplier = Printer::new;

        var gameFactory = new ConsoleGameFactory(
                humanPlayerSupplier,
                computerPlayerSupplier,
                boardSupplier,
                winCheckerSupplier,
                printerSupplier
        );

        var userInterface = new UserInterface(in, out, gameFactory);
        userInterface.play();
    }
}
