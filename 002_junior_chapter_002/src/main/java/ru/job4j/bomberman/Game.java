package ru.job4j.bomberman;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Integer, Thread> characters = new HashMap<>();

    public boolean add(Personage personage) {
        boolean result = !this.characters.containsKey(personage.id());
        if (result) {
            Thread runner = new Thread(new RunPersonage(personage));
            this.characters.put(personage.id(), runner);
        }
        return result;
    }

    public void start() {
        for (Thread runner : this.characters.values()) {
            runner.start();
        }
    }

    public void stop() {
        try {
            for (Thread runner : this.characters.values()) {
                runner.interrupt();
            }
            for (Thread runner : this.characters.values()) {
                runner.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
