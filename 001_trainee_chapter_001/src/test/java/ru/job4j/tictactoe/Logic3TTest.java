package ru.job4j.tictactoe;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Logic3TTest {

    @Test(expected = RuntimeException.class)
    public void whenCoordinatesOfDiagonalWrongThenThrowsException() {
        new Logic3T(new Figure3T[0][0]).isDiagonalFilled(0, 0, 4, 5, true);
    }

    @Test
    public void whenXWinnerThenFindsIt() {
        // horizontal
        Figure3T[][] tableH1 = {
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] tableH2 = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] tableH3 = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
        };
        assertThat(new Logic3T(tableH1).isWinnerX(), is(true));
        assertThat(new Logic3T(tableH1).isWinnerO(), is(false));
        assertThat(new Logic3T(tableH2).isWinnerX(), is(true));
        assertThat(new Logic3T(tableH2).isWinnerO(), is(false));
        assertThat(new Logic3T(tableH3).isWinnerX(), is(true));
        assertThat(new Logic3T(tableH3).isWinnerO(), is(false));
        // vertical
        Figure3T[][] tableV1 = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] tableV2 = {
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
        };
        Figure3T[][] tableV3 = {
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
        };
        assertThat(new Logic3T(tableV1).isWinnerX(), is(true));
        assertThat(new Logic3T(tableV1).isWinnerO(), is(false));
        assertThat(new Logic3T(tableV2).isWinnerX(), is(true));
        assertThat(new Logic3T(tableV2).isWinnerO(), is(false));
        assertThat(new Logic3T(tableV3).isWinnerX(), is(true));
        assertThat(new Logic3T(tableV3).isWinnerO(), is(false));
        // diagonal
        Figure3T[][] tableD1 = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
        };
        Figure3T[][] tableD2 = {
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(true), new Figure3T(), new Figure3T()},
        };
        assertThat(new Logic3T(tableD1).isWinnerX(), is(true));
        assertThat(new Logic3T(tableD1).isWinnerO(), is(false));
        assertThat(new Logic3T(tableD2).isWinnerX(), is(true));
        assertThat(new Logic3T(tableD2).isWinnerO(), is(false));
        // mixed
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
        };
        Logic3T login = new Logic3T(table);
        assertThat(login.isWinnerX(), is(true));
        assertThat(login.isWinnerO(), is(false));
    }

    @Test
    public void whenOWinnerThenFindsIt() {
        // horizontal
        Figure3T[][] tableH1 = {
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] tableH2 = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)},
                {new Figure3T(), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] tableH3 = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)},
        };
        assertThat(new Logic3T(tableH1).isWinnerO(), is(true));
        assertThat(new Logic3T(tableH1).isWinnerX(), is(false));
        assertThat(new Logic3T(tableH2).isWinnerO(), is(true));
        assertThat(new Logic3T(tableH2).isWinnerX(), is(false));
        assertThat(new Logic3T(tableH3).isWinnerO(), is(true));
        assertThat(new Logic3T(tableH3).isWinnerX(), is(false));
        // vertical
        Figure3T[][] tableV1 = {
                {new Figure3T(false), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] tableV2 = {
                {new Figure3T(), new Figure3T(false), new Figure3T()},
                {new Figure3T(), new Figure3T(false), new Figure3T()},
                {new Figure3T(), new Figure3T(false), new Figure3T()},
        };
        Figure3T[][] tableV3 = {
                {new Figure3T(), new Figure3T(), new Figure3T(false)},
                {new Figure3T(), new Figure3T(), new Figure3T(false)},
                {new Figure3T(), new Figure3T(), new Figure3T(false)},
        };
        assertThat(new Logic3T(tableV1).isWinnerO(), is(true));
        assertThat(new Logic3T(tableV1).isWinnerX(), is(false));
        assertThat(new Logic3T(tableV2).isWinnerO(), is(true));
        assertThat(new Logic3T(tableV2).isWinnerX(), is(false));
        assertThat(new Logic3T(tableV3).isWinnerO(), is(true));
        assertThat(new Logic3T(tableV3).isWinnerX(), is(false));
        // diagonal
        Figure3T[][] tableD1 = {
                {new Figure3T(false), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(false), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T(false)},
        };
        Figure3T[][] tableD2 = {
                {new Figure3T(), new Figure3T(), new Figure3T(false)},
                {new Figure3T(), new Figure3T(false), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T()},
        };
        assertThat(new Logic3T(tableD1).isWinnerO(), is(true));
        assertThat(new Logic3T(tableD1).isWinnerX(), is(false));
        assertThat(new Logic3T(tableD2).isWinnerO(), is(true));
        assertThat(new Logic3T(tableD2).isWinnerX(), is(false));
        // mixed
        Figure3T[][] table = {
                {new Figure3T(false), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(true), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T(true)},
        };
        Logic3T login = new Logic3T(table);
        assertThat(login.isWinnerO(), is(true));
        assertThat(login.isWinnerX(), is(false));
    }

    @Test
    public void whenNoWinnerThenFindsIt() {
        Figure3T[][] empty = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
        };
        Figure3T[][] table1 = {
                {new Figure3T(true), new Figure3T(true), new Figure3T(false)},
                {new Figure3T(false), new Figure3T(false), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T(true)},
        };
        Figure3T[][] table2 = {
                {new Figure3T(false), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(true), new Figure3T(false), new Figure3T(false)},
                {new Figure3T(false), new Figure3T(false), new Figure3T(true)},
        };
        assertThat(new Logic3T(empty).isWinnerX(), is(false));
        assertThat(new Logic3T(empty).isWinnerO(), is(false));
        assertThat(new Logic3T(table1).isWinnerX(), is(false));
        assertThat(new Logic3T(table1).isWinnerO(), is(false));
        assertThat(new Logic3T(table2).isWinnerX(), is(false));
        assertThat(new Logic3T(table2).isWinnerO(), is(false));
    }

    @Test
    public void whenHasGap() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
        };
        Logic3T login = new Logic3T(table);
        assertThat(login.hasGap(), is(true));
    }
}