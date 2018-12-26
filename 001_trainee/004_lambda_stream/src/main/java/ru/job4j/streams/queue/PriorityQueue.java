package ru.job4j.streams.queue;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Add task into ru.job4j.queue according to its priority.
     *
     * @param task new task.
     */
    void put(Task task) {
        // туповато и прожорливо, но как через Stream API это сделать по-другому - я не придумал
        this.tasks = Stream.concat(this.tasks.stream(), Stream.of(task))
                .sorted(Comparator.comparingInt(Task::priority))
                .collect(Collectors.toCollection(LinkedList::new));
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
     * Take the first task from ru.job4j.queue and delete it from the ru.job4j.queue.
     *
     * @return next task in the queue.
     */
    Task poll() {
        return this.tasks.poll();
    }
}
