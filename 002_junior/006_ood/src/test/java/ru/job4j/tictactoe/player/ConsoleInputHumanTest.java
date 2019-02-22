package ru.job4j.tictactoe.player;

import org.junit.Test;
import ru.job4j.tictactoe.cell.ICell;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ConsoleInputHumanTest {

    @Test
    public void whenDoMoveThenInputMoveFromConsole() throws Exception {
        Queue<String> input = new LinkedList<>(Collections.singletonList(
                "1 2"
        ));
        var table = new ICell[0][0];
        var output = new ArrayList<>();
        var result = new ConsoleInputHuman(input::poll, output::add).doMove(table);
        var expected = new IPlayer.Move(1, 2);
        assertThat(result, is(expected));
        assertThat(output, is(List.of(
                "Enter coordinates divided by space (e.g.: 3 2)"
        )));
    }

    @Test
    public void whenWrongInputThenRepeatInput() throws Exception {
        Queue<String> input = new LinkedList<>(Arrays.asList(
                "one two",
                "asdfkjsad",
                "hi!",
                "1 2"
        ));
        var table = new ICell[0][0];
        var output = new ArrayList<>();
        var result = new ConsoleInputHuman(input::poll, output::add).doMove(table);
        var expected = new IPlayer.Move(1, 2);
        assertThat(result, is(expected));
        assertThat(output, is(List.of(
                "Enter coordinates divided by space (e.g.: 3 2)",
                "Enter coordinates divided by space (e.g.: 3 2)",
                "Enter coordinates divided by space (e.g.: 3 2)",
                "Enter coordinates divided by space (e.g.: 3 2)"
        )));
    }
}