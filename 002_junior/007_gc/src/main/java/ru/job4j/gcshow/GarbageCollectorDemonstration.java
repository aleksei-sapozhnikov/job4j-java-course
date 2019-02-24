package ru.job4j.gcshow;

/**
 * Sample demonstration of Garbage collector work.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class GarbageCollectorDemonstration {
    /**
     * Demonstration method. Start with jvm option: -Xmx6m.
     *
     * @param args String args.
     */
    public static void main(String[] args) {
        System.out.printf("Max memory (kB): %d%n", Runtime.getRuntime().maxMemory() / 1024);
        System.out.printf("Total memory (kB): %d%n", Runtime.getRuntime().totalMemory() / 1024);
        System.out.printf("Free memory (kB): %d%n", Runtime.getRuntime().freeMemory() / 1024);
        User user;
        for (int i = 0; i < 1000; i++) {
            user = new User(String.format("user-%s", i), i);
            System.out.printf("created: %s, free memory: %s bytes, thread: %s%n",
                    user, Runtime.getRuntime().freeMemory(), Thread.currentThread().getName());
            System.out.flush();
        }
    }

    /**
     * Sample class.
     * <p>
     * Estimated size of User object (on 64-bit JVM).
     * <p>
     * First, minimum size (empty object):
     * -- header: 2 machine words = 2 * 64 bits = 2 * 8 bytes = 16 bytes.
     * -- ref to String 'name': 32 bits = 4 bytes (because heap is < 32 Gb).
     * -- int field 'age': 4 bytes.
     * Sum: 16 + 4 + 4 = 24 bytes.
     * Each object size must be multiple of 8. 24 % 8 == 0, so it's ok.
     * <p>
     * Estimated size of String object:
     * -- header: 16 bytes.
     * -- int fields: 'offset', 'count', 'hash": 3 * 4 bytes = 12 bytes.
     * -- ref to char[] 'value': 4 bytes.
     * Sum: 16 + 12 + 4 = 32 bytes.
     * 32 % 8 == 0.
     * <p>
     * Estimated size of char[] object:
     * -- header: 16 bytes.
     * -- array length field: 4 bytes.
     * -- char symbols: each is 2 bytes.
     * Sum: 16 + 4 + (2 * n) = 20 + (2 * n), n is array length.
     * <p>
     * Minimum cost of creating User object is 24 bytes (empty object or all composite object already created).
     * Maximum size is 24 + 32 + 20 + (2 * n) = 76 + (2 * n) bytes.
     * <p>
     * User object size estimation: from 24 to (76 + 2*n) bytes.
     */
    public static class User {
        /**
         * Name.
         */
        private String name;
        /**
         * Age.
         */
        private int age;

        /**
         * Constructor.
         *
         * @param name Name.
         * @param age  Age.
         */
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        /**
         * Returns object status as string.
         *
         * @return Object status as string.
         */
        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        /**
         * Called before object destruction.
         *
         * @throws Throwable If problems while finalization occur.
         */
        @Override
        protected void finalize() throws Throwable {
            System.out.printf("finalizing: %s, free memory = %s, thread: %s%n", this, Runtime.getRuntime().freeMemory(), Thread.currentThread().getName());
            System.out.flush();
            super.finalize();
        }
    }
}

