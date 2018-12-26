package ru.job4j.problems;

/**
 * Thread changing value in a mutable object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.03.2018
 */
class RacerValue implements Runnable {
    /**
     * Mutable object shared between threads.
     */
    private MutableInteger shared;
    /**
     * Each thread multiplies value by given multiplier.
     */
    private int multiplier;

    /**
     * Constructs a new object.
     *
     * @param shared     mutable object shared between threads.
     * @param multiplier value to multiply to.
     */
    RacerValue(MutableInteger shared, int multiplier) {
        this.shared = shared;
        this.multiplier = multiplier;
    }

    /**
     * Runs racer.
     */
    @Override
    public void run() {
        try {
            int val = this.shared.getValue();
            Thread.sleep(50);
            int res = val * this.multiplier;
            Thread.sleep(50);
            this.shared.setValue(res);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }


}
