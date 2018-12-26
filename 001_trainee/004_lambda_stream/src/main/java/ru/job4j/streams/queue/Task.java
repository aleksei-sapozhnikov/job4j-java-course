package ru.job4j.streams.queue;

/**
 * Task with priority.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class Task {

    /**
     * Description of the task.
     */
    private String description;

    /**
     * Task priority level (lower - more urgent).
     */
    private int priority;

    /**
     * Constructor.
     *
     * @param description task description.
     * @param priority    priority level (lower - more urgent).
     */
    Task(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    /**
     * Get description.
     *
     * @return description field value.
     */
    String description() {
        return this.description;
    }

    /**
     * Get priority.
     *
     * @return priority field value.
     */
    int priority() {
        return this.priority;
    }
}
