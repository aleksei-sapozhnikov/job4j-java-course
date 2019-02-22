package ru.job4j.tictactoe.gamelogic;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tictactoe.board.IBoard;
import ru.job4j.tictactoe.player.IPlayer;
import ru.job4j.tictactoe.printer.IPrinter;
import ru.job4j.tictactoe.winchecker.IWinChecker;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConsoleGameFactoryTest {

    @SuppressWarnings("unchecked")
    private final Supplier<IPlayer> humanPlayerSupplier =
            (Supplier<IPlayer>) Mockito.mock(Supplier.class);

    @SuppressWarnings("unchecked")
    private final Supplier<IPlayer> computerPlayerSupplier =
            (Supplier<IPlayer>) Mockito.mock(Supplier.class);

    @SuppressWarnings("unchecked")
    private final BiFunction<Integer, Integer, IBoard> boardSupplier =
            (BiFunction<Integer, Integer, IBoard>) Mockito.mock(BiFunction.class);

    @SuppressWarnings("unchecked")
    private final Function<Integer, IWinChecker> winCheckerSupplier =
            (Function<Integer, IWinChecker>) Mockito.mock(Function.class);

    @SuppressWarnings("unchecked")
    private final Supplier<IPrinter> printerSupplier =
            (Supplier<IPrinter>) Mockito.mock(Supplier.class);

    @Test
    public void whenCreatePlayerThenComputerThenAllSuppliersUsed() {
        var factory = new ConsoleGameFactory(
                this.humanPlayerSupplier,
                this.computerPlayerSupplier,
                this.boardSupplier,
                this.winCheckerSupplier,
                this.printerSupplier
        );
        factory.humanThenComputer(5, 6, 3);
        Mockito.verify(this.humanPlayerSupplier).get();
        Mockito.verify(this.computerPlayerSupplier).get();
        Mockito.verify(this.boardSupplier).apply(5, 6);
        Mockito.verify(this.winCheckerSupplier).apply(3);
        Mockito.verify(this.printerSupplier).get();
    }


    @Test
    public void whenCreateComputerThenPlayerThenAllSuppliersUsed() {
        var factory = new ConsoleGameFactory(
                this.humanPlayerSupplier,
                this.computerPlayerSupplier,
                this.boardSupplier,
                this.winCheckerSupplier,
                this.printerSupplier
        );
        factory.computerThenHuman(8, 3, 2);
        Mockito.verify(this.computerPlayerSupplier).get();
        Mockito.verify(this.humanPlayerSupplier).get();
        Mockito.verify(this.boardSupplier).apply(8, 3);
        Mockito.verify(this.winCheckerSupplier).apply(2);
        Mockito.verify(this.printerSupplier).get();
    }
}