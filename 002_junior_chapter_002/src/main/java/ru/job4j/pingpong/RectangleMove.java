package ru.job4j.pingpong;

import javafx.scene.shape.Rectangle;

/**
 * Thread moving rectangle - the whole logic is here.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.04.2018
 */
public class RectangleMove implements Runnable {
    /**
     * Step in horizontal direction.
     */
    private static final int STEP_X = 2;
    /**
     * Step in vertical direction.
     */
    private static final int STEP_Y = 3;
    /**
     * Moving object.
     */
    private final Rectangle rect;
    /**
     * Horizontal axis maximum value.
     */
    private final int limitX;
    /**
     * Vertical axis maximum value.
     */
    private final int limitY;
    /**
     * <tt>true</tt> means moving right,
     * <tt>false</tt> - moving left.
     */
    private boolean right = true;
    /**
     * <tt>true</tt> means moving down,
     * <tt>false</tt> means moving up.
     */
    private boolean down = true;

    /**
     * Constructs new object of movement logic.
     *
     * @param rect   rectangle to move.
     * @param limitX right edge of horizontal axis.
     * @param limitY lower edge of vertical axis.
     */
    public RectangleMove(Rectangle rect, int limitX, int limitY) {
        this.rect = rect;
        this.limitX = limitX;
        this.limitY = limitY;
    }

    /**
     * Runs thread.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                this.move();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates how to move rectangle and gives it new coordinates.
     * Then makes a small pause.
     *
     * @throws InterruptedException if thread was interrupted while waiting.
     */
    private void move() throws InterruptedException {
        int pause = 50;
        boolean xChange = this.right
                ? rect.getX() + rect.getWidth() >= this.limitX
                : rect.getX() <= 0;
        boolean yChange = this.down
                ? rect.getY() + rect.getHeight() >= this.limitY
                : rect.getY() <= 0;
        this.right = xChange != this.right; // changes direction if xChange == true
        this.down = yChange != this.down;
        this.rect.setX(this.rect.getX() + (this.right ? STEP_X : -STEP_X));
        this.rect.setY(this.rect.getY() + (this.down ? STEP_Y : -STEP_Y));
        Thread.sleep(pause);
    }
}