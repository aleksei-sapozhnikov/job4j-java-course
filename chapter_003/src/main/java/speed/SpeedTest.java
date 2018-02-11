package speed;

import java.util.*;

public class SpeedTest {

    public static void main(String[] args) {
        SpeedTest test = new SpeedTest();
        test.test(100_000);
        test.test(200_000);
        test.test(300_000);
        test.test(500_000);
        test.test(750_000);
        test.test(1_000_000);
    }

    void test(int addTimes) {
        System.out.println();
        int delTimes = addTimes / 2;
        System.out.println(String.format("====== Add times: %,d, Delete times: %,d ======", addTimes, delTimes));
        Collection<String> array = new ArrayList<>();
        Collection<String> arraySize = new ArrayList<>(addTimes);
        Collection<String> linked = new LinkedList<>();
        Collection<String> tree = new TreeSet<>();
        System.out.println("=== Add ===");
        System.out.println(String.format("ArrayList(default): %d ms", this.add(array, addTimes)));
        System.out.println(String.format("ArrayList(%,d): %d ms", addTimes, this.add(arraySize, addTimes)));
        System.out.println(String.format("LinkedList: %d ms", this.add(linked, addTimes)));
        System.out.println(String.format("TreeSet: %d ms", this.add(tree, addTimes)));
        System.out.println();
        System.out.println("=== Delete ===");
        System.out.println(String.format("ArrayList(default): %d ms", this.delete(array, delTimes)));
        System.out.println(String.format("ArrayList(%,d): %d ms", addTimes, this.delete(arraySize, delTimes)));
        System.out.println(String.format("LinkedList: %d ms", this.delete(linked, delTimes)));
        System.out.println(String.format("TreeSet: %d ms", this.delete(tree, delTimes)));
    }

    String randomString() {
        int max = (int) Character.MAX_VALUE;
        Random r = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            buffer.append((char) r.nextInt(max + 1));
        }
        return buffer.toString();
    }

    public long add(Collection<String> collection, int amount) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            collection.add(this.randomString());
        }
        return System.currentTimeMillis() - start;
    }

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


}
