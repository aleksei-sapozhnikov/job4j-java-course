package ru.job4j.bomberman;

import java.util.HashMap;
import java.util.Map;

/**
 * Main game class. Can add characters, start and stop game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.04.2018
 */
public class Game {
    /**
     * Map of blocks (not-moving objects locking cells).
     */
    private final Map<Integer, Personage> blocks = new HashMap<>();
    /**
     * Map of threads for automatic-moving personages.
     */
    private final Map<Integer, Thread> automatics = new HashMap<>();

    public void startGame() throws WrongCoordinatesException {
        this.placeBlocks();
        this.startAutomatics();
    }

    /**
     * Adds new block to the game.
     *
     * @param block block to add.
     * @return <tt>true</tt> if added, <tt>false</tt> if personage with such id already exists in the game.
     */
    public boolean addBlock(Personage block) {
        boolean result = !this.blocks.containsKey(block.id());
        if (result) {
            this.blocks.put(block.id(), block);
            System.out.format("> Block \"%s\" added (%s, %s)%n", block.name(), block.x(), block.y());
            System.out.flush();
        }
        return result;
    }

    /**
     * Places all blocks (locks their cells on board).
     *
     * @throws WrongCoordinatesException if block coordinates do not point to any cell inside the board.
     */
    private void placeBlocks() throws WrongCoordinatesException {
        for (Personage block : this.blocks.values()) {
            block.place();
            System.out.format("> Block \"%s\" placed (%s, %s)%n", block.name(), block.x(), block.y());
            System.out.flush();
        }
    }

    /**
     * Adds new automatic-moving personage.
     *
     * @param automatic automatic-moving personage.
     * @return <tt>true</tt> if added, <tt>false</tt> if personage with such id already exists in the game.
     */
    public boolean addAutomatic(Personage automatic) {
        boolean result = !this.automatics.containsKey(automatic.id());
        if (result) {
            Thread runner = new Thread(new RunPersonageRandomMove(automatic));
            this.automatics.put(automatic.id(), runner);
            System.out.format("+ Automatic \"%s\" added (%s, %s)%n", automatic.name(), automatic.x(), automatic.y());
            System.out.flush();
        }
        return result;
    }

    /**
     * Starts all automatic-personages threads.
     */
    private void startAutomatics() {
        for (Thread automatic : this.automatics.values()) {
            automatic.start();
        }
        System.out.println("> Started all automatics");
        System.out.flush();
    }

    /**
     * Interrupts all automatic personages.
     */
    public void stopAutomatics() throws InterruptedException {
        for (Thread runner : this.automatics.values()) {
            runner.interrupt();
        }
        for (Thread runner : this.automatics.values()) {
            runner.join();
        }
    }


}
