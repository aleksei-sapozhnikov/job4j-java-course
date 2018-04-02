package ru.job4j.problems;

import org.junit.Test;

/**
 * Tests for RacerWorker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.03.2018
 */
public class RacerWorkerTest {

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