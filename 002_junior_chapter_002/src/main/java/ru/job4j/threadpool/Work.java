package ru.job4j.threadpool;

/**
 * Simple Work class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Work {
    /**
     * Name.
     */
    private final String name;
    /**
     * Constructor.
     *
     * @param name Work name.
     */
    public Work(String name) {
        this.name = name;
    }

    /**
     * Simple work.
     */
    public void doWork() {
        long start = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - start < 1000) {
            count++;
        }
    }

    /**
     * Returns name.
     * @return Work name.
     */
    public String getName() {
        return this.name;
    }

}
