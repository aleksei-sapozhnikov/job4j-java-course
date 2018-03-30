package ru.job4j.problems;

import org.junit.Test;

/**
 * Runs race conditions for racers.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.03.2018
 */
public class RaceConditions {

    /**
     * Race condition and mutable object - every run result is different.
     */
    @Test
    public void sameOperationsDifferentResults() throws InterruptedException {
        int times = 10;
        for (int i = 0; i < times; i++) {
            MutableInteger shared = new MutableInteger(3);
            Thread[] threads = new Thread[100];
            for (int num = 0; num < threads.length; num++) {
                threads[num] = new Thread(new RacerValue(shared, num + 1));
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            System.out.printf("Попытка №%s, результат: %s%n", i, shared.getValue());
        }
    }

    /**
     * Race condition and mutable object - every run result is different.
     */
    @Test
    public void professionsAbracadabra() throws InterruptedException {
        String[] professions = {
                "Пирожник ",
                "Сапожник ",
                "Хирург ",
                "Строитель ",
                "Гончар ",
                "Президент ",
                "Ванька-дурак "
        };
        String[] duties = {
                "будет печь пироги.",
                "будет чинить сапоги.",
                "будет вырезать аппендицит.",
                "будет строить дом.",
                "будет лепить вазу.",
                "будет управлять страной",
                "будет валяться на печи и ждать щуку."
        };
        int size = Math.min(professions.length, duties.length);
        System.out.println("===== SINGLE THREAD (EXPECTING) =====");
        for (int num = 0; num < size; num++) {
            RacerWorker worker = new RacerWorker(num, professions, duties);
            worker.run();
        }
        System.out.println();
        System.out.println("===== MULTI-THREADING (REALITY) =====");
        Thread[] threads = new Thread[size];
        for (int num = 0; num < threads.length; num++) {
            threads[num] = new Thread(new RacerWorker(num, professions, duties));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }


}