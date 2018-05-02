package ru.job4j.bomberman;

/**
 * Runnable class controlling random-moving personage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.04.2018
 */
public class RunPersonageRandomMove implements Runnable {
    /**
     * Moving personage.
     */
    private Personage personage;
    /**
     * Flag for the thread to continue working.
     */
    private boolean working = true;

    /**
     * Constructs new Runnable random-moving object.
     *
     * @param personage personage to take control of.
     */
    public RunPersonageRandomMove(Personage personage) {
        this.personage = personage;
    }

    /**
     * Operations to do.
     */
    @Override
    public void run() {
        try {
            this.personage.place();
            System.out.format("> Automatic \"%s\" placed (%s, %s)%n", this.personage.name(), this.personage.x(), this.personage.y());
            System.out.flush();
            while (working && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
                this.personage = this.personage.randomMove();
            }
        } catch (InterruptedException | WrongCoordinatesException e) {
            this.working = false;
        }
    }
}
