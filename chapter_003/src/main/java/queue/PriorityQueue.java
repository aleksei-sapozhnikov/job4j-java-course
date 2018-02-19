package queue;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Simple priority queue of tasks.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 19.02.2018
 */
public class PriorityQueue {

    /**
     * List of tasks.
     */
    private LinkedList<Task> tasks = new LinkedList<>();

    /**
     * Add task into queue according to its priority.
     *
     * @param task new task.
     */
    void put(Task task) {
        int index = 0;
        for (Task temp : this.tasks) {
            if (task.priority() < temp.priority()) {
                break;
            }
            index++;
        }
        this.tasks.add(index, task);
    }

    /**
     * То же самое, что и put(), но вставляет элемент при помощи итератора, а не отдельным вызовом.
     * <p>
     * Насколько я понимаю, в методе put() мы проходимся по списку дважды:
     * сначала, чтобы найти индекс для элемента, а затем в методе
     * list.put(index, Task), проходя последовательно по индексам (ведь у нас LinkedList).
     * <p>
     * В этом же методе совершается лишь один последовательный проход.
     *
     * @param task new task.
     */
    void putByIterator(Task task) {
        boolean added = false;
        ListIterator<Task> iterator = this.tasks.listIterator();
        while (iterator.hasNext()) {
            if (task.priority() < iterator.next().priority()) {
                iterator.previous();
                iterator.add(task);
                added = true;
                break;
            }
        }
        if (!added) {
            iterator.add(task);
        }
    }

    /**
     * Take the first task from queue and delete it from the queue.
     *
     * @return next task in the queue.
     */
    Task poll() {
        return this.tasks.poll();
    }
}
