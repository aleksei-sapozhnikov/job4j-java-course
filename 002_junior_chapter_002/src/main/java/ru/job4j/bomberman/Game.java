package ru.job4j.bomberman;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<Integer, Thread> characters = new HashMap<>();

    public boolean add(Character character) {
        boolean result = !this.characters.containsKey(character.id());
        if (result) {
            Thread runner = new Thread(new RunCharacter(character));
            this.characters.put(character.id(), runner);
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
