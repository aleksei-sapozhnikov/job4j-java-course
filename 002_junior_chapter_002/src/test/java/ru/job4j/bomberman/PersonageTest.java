package ru.job4j.bomberman;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.bomberman.Direction.*;

public class PersonageTest {

    private final Object sync = new Object();

    /**
     * Test getters.
     */
    @Test
    public void whenGetterThenValue() {
        Personage personage = new Personage(new Board(3, 3), 1, "John", 2, 1);
        assertThat(personage.id(), is(1));
        assertThat(personage.name(), is("John"));
        assertThat(personage.x(), is(2));
        assertThat(personage.y(), is(1));
    }

    /**
     * Test tryMove()
     */
    @Test
    public void whenTryMoveToFreeCellThenPersonageWithNewCoordinatesAndUnlocksOldCell() throws InterruptedException {
        Board board = new Board(3, 3);
        // move RIGHT: (1,1) -> (2, 1)
        Personage personage = new Personage(board, 1, "John", 1, 1);
        Personage moved = personage.tryMove(RIGHT);
        assertThat(moved.x(), is(2));
        assertThat(moved.y(), is(1));
        // move DOWN: (2, 1) -> (2, 2)
        personage = moved;
        moved = personage.tryMove(DOWN);
        assertThat(moved.x(), is(2));
        assertThat(moved.y(), is(2));
        // move LEFT: (2, 2) -> (1, 2)
        personage = moved;
        moved = personage.tryMove(LEFT);
        assertThat(moved.x(), is(1));
        assertThat(moved.y(), is(2));
        // move UP: (1, 2) -> (1, 1)
        personage = moved;
        moved = personage.tryMove(UP);
        assertThat(moved.x(), is(1));
        assertThat(moved.y(), is(1));
    }

    @Test
    public void whenTryMoveToLockedCellThenThisPersonageObjectBack() throws InterruptedException {
        Board board = new Board(3, 3);
        int[] counter = {0};
        // blocker: thread to block cells
        new Thread(() -> {
            try {
                board.lock(2, 1);   // RIGHT
                board.lock(1, 2);   // DOWN
                board.lock(0, 1);   // LEFT
                board.lock(1, 0);   // UP
                counter[0] = 1;
                // blocker: give control to main thread
                synchronized (this.sync) {
                    this.sync.notify();
                    while (counter[0] != 2) {
                        this.sync.wait();
                    }
                }
            } catch (WrongCoordinatesException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // main thread: wait for blocker thread to block everything
        while (counter[0] != 1) {
            synchronized (this.sync) {
                sync.wait();
            }
        }
        // main thread: try moving
        Personage personage = new Personage(board, 1, "John", 1, 1);
        Personage movedRight = personage.tryMove(RIGHT);
        Personage movedDown = personage.tryMove(DOWN);
        Personage movedLeft = personage.tryMove(LEFT);
        Personage movedUp = personage.tryMove(UP);
        assertThat(movedRight == personage, is(true));
        assertThat(movedDown == personage, is(true));
        assertThat(movedLeft == personage, is(true));
        assertThat(movedUp == personage, is(true));
    }

    /**
     * Test place()
     */
    @Test
    public void place() {

    }

    @Test
    public void randomMove() {
    }


}