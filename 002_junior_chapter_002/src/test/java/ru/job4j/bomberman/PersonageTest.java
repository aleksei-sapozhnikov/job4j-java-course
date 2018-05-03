package ru.job4j.bomberman;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.bomberman.Direction.*;

/**
 * Tests for Personage class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.04.2018
 */
public class PersonageTest {
    /**
     * Object to synchronize threads in tests.
     */
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
     * Test place()
     */
    @Test
    public void whenPlacePersonageThenThisCellIsBlockedOthersFree() throws InterruptedException {
        Board board = new Board(3, 3);
        Personage personage = new Personage(board, 1, "aaa", 1, 1); // lock (1, 1)
        int[] counter = {0};
        // personage thread: places personage
        new Thread(() -> {
            try {
                personage.place();
                counter[0] = 1;
                // give control to main thread
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
        // main thread: wait for personage thread to place()
        while (counter[0] != 1) {
            synchronized (this.sync) {
                sync.wait();
            }
        }
        // main thread: now trying to block cells
        assertThat(board.tryLock(1, 1), is(false));
        // others free
        assertThat(board.tryLock(0, 0), is(true));
        assertThat(board.tryLock(1, 0), is(true));
        assertThat(board.tryLock(2, 0), is(true));
        assertThat(board.tryLock(0, 1), is(true));
        assertThat(board.tryLock(2, 1), is(true));
        assertThat(board.tryLock(2, 0), is(true));
        assertThat(board.tryLock(2, 1), is(true));
        assertThat(board.tryLock(2, 2), is(true));
    }

    /**
     * Test tryMove()
     */
    @Test
    public void whenTryMoveToFreeCellThenPersonageWithNewCoordinatesAndUnlocksOldCell() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(3, 3);
        Personage personage = new Personage(board, 1, "John", 1, 1);
        personage.place();
        // move RIGHT: (1,1) -> (2, 1)
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
    public void whenTryMoveToLockedCellThenThisPersonageObjectBack() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(3, 3);
        int[] counter = {0};
        // blocker: thread to block cells
        new Thread(() -> {
            try {
                board.lockInterruptibly(2, 1);   // RIGHT
                board.lockInterruptibly(1, 2);   // DOWN
                board.lockInterruptibly(0, 1);   // LEFT
                board.lockInterruptibly(1, 0);   // UP
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
     * Test tryMove() and place() together
     */
    @Test
    public void whenTryMoveToCellWithOtherPersonageThenFalseElseTrue() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(4, 1);
        Personage left = new Personage(board, 1, "our", 0, 0);
        Personage right = new Personage(board, 1, "right", 1, 0);
        int[] counter = {0};
        // right mover - blocks cells
        new Thread(() -> {
            try {
                synchronized (this.sync) {
                    right.place();          // stays and blocks (1, 0)
                    counter[0] = 1;
                    this.sync.notify();
                    while (counter[0] != 2) {       // now left will try to move to blocked (1, 0)
                        this.sync.wait();
                    }
                    right.tryMove(RIGHT); // right goes away, now (1, 0) is free
                    counter[0] = 3;
                    this.sync.notify();
                    while (counter[0] != 4) {
                        this.sync.wait();
                    }
                }
            } catch (InterruptedException | WrongCoordinatesException e) {
                e.printStackTrace();
            }
        }).start();
        // left mover - tries to move
        left.place();
        synchronized (this.sync) {
            while (counter[0] != 1) {       // wait for right to place()
                this.sync.wait();
            }
            assertThat(left.tryMove(RIGHT) == left, is(true)); // tries to move, but (1, 0) is blocked by right
            counter[0] = 2;
            this.sync.notify();
            while (counter[0] != 3) {       // wait for right to go away
                this.sync.wait();
            }
            assertThat(left.tryMove(RIGHT).x(), is(1)); // now (1, 0) is free!
        }
    }

    /**
     * Test randomMove()
     */
    @Test
    public void whenRandomMoveOnlyRightLeftPossibleThenRightLeft() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(2, 1);
        Personage prisoner = new Personage(board, 1, "prisoner", 0, 0);
        prisoner.place();
        // only way - right
        Personage moved = prisoner.randomMove();
        assertThat(moved.x(), is(1));
        assertThat(moved.y(), is(0));
        //only way - left
        prisoner = moved;
        moved = prisoner.randomMove();
        assertThat(moved.x(), is(0));
        assertThat(moved.y(), is(0));
    }

    @Test
    public void whenRandomMoveOnlyDownAndUpPossibleThenDownUp() throws InterruptedException, WrongCoordinatesException {
        Board board = new Board(1, 2);
        Personage prisoner = new Personage(board, 1, "prisoner", 0, 0);
        prisoner.place();
        // only way - down
        Personage moved = prisoner.randomMove();
        assertThat(moved.x(), is(0));
        assertThat(moved.y(), is(1));
        //only way - up
        prisoner = moved;
        moved = prisoner.randomMove();
        assertThat(moved.x(), is(0));
        assertThat(moved.y(), is(0));
    }


}