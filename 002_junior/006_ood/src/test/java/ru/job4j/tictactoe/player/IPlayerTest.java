package ru.job4j.tictactoe.player;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class IPlayerTest {

    @Test
    public void testPlayerMoveGetters() {
        IPlayer.Move move = new IPlayer.Move(3, 4);
        assertThat(move.getIHeight(), is(3));
        assertThat(move.getIWidth(), is(4));
    }

    @Test
    public void testPlayerMoveEqualsHashCode() {
        IPlayer.Move move = new IPlayer.Move(3, 4);
        IPlayer.Move same = new IPlayer.Move(3, 4);
        IPlayer.Move otherIHeight = new IPlayer.Move(7, 4);
        IPlayer.Move otherIWidth = new IPlayer.Move(3, 8);
        assertNotEquals(move, null);
        assertNotEquals(move, "move");
        assertNotEquals(move, otherIHeight);
        assertNotEquals(move, otherIWidth);
        assertEquals(move, move);
        assertEquals(move, same);
        assertEquals(move.hashCode(), same.hashCode());
    }

}