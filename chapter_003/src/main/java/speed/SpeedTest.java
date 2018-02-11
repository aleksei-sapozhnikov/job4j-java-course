package speed;

import java.util.*;

/**
 * Simple speed test for collections.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.02.2018
 */
public class SpeedTest {

    /**
     * Main method.
     *
     * @param args String arguments.
     */
    public static void main(String[] args) {
        SpeedTest test = new SpeedTest();
        test.test(100_000);
        test.test(200_000);
        test.test(300_000);
        test.test(500_000);
        test.test(750_000);
        test.test(1_000_000);
    }

    /**
     * Generates random String.
     *
     * @return random String of length = 10.
     */
    private String randomString() {
        int max = (int) Character.MAX_VALUE;
        Random rand = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            buffer.append((char) rand.nextInt(max + 1));
        }
        return buffer.toString();
    }

    /**
     * Tests time needed to perform add operations in specified Collection.
     * Adds elements to the end of the collection.
     *
     * @param collection Collection to test.
     * @param amount     How many elements to add.
     * @return Total time of the operation in milliseconds.
     */
    public long add(Collection<String> collection, int amount) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            collection.add(this.randomString());
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Tests time needed to perform delete operations in specified Collection.
     * Removes the first element in the collection for the specified amount of times.
     * Finally, the first {@code amount} elements are deleted.
     *
     * @param collection Collection to test.
     * @param amount     How many times delete the first element.
     * @return Total time of the operation in millisceconds.
     */
    public long delete(Collection<String> collection, int amount) {
        long start = System.currentTimeMillis();
        Iterator<String> iterator = collection.iterator();
        for (int i = 0; i < amount; i++) {
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            } else {
                break;
            }

        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Tests and prints time needed to add or remove elements into different collections.
     * Elements are of String type, added "to the end" and removed "from the beginning".
     * String elements are randomly generated from chars.
     *
     * @param addTimes How much elements to add.
     */
    private void test(int addTimes) {
        System.out.println();
        int delTimes = addTimes / 2;
        Collection<String> array = new ArrayList<>();
        Collection<String> arraySize = new ArrayList<>(addTimes);
        Collection<String> linked = new LinkedList<>();
        Collection<String> tree = new TreeSet<>();
        System.out.printf("=== Add %,d elements ===%n", addTimes);
        System.out.printf("ArrayList(default): %d ms%n", this.add(array, addTimes));
        System.out.printf("ArrayList(%,d): %d ms%n", addTimes, this.add(arraySize, addTimes));
        System.out.printf("LinkedList: %d ms%n", this.add(linked, addTimes));
        System.out.printf("TreeSet: %d ms%n", this.add(tree, addTimes));
        System.out.println();
        System.out.printf("=== Delete %,d elements: ===%n", delTimes);
        System.out.printf("ArrayList(default): %d ms%n", this.delete(array, delTimes));
        System.out.printf("ArrayList(%,d): %d ms%n", addTimes, this.delete(arraySize, delTimes));
        System.out.printf("LinkedList: %d ms%n", this.delete(linked, delTimes));
        System.out.printf("TreeSet: %d ms%n", this.delete(tree, delTimes));
    }


}
