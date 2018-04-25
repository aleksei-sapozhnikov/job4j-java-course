/**
 * Пример, как получить dead lock - когда два потока работают работу друг друга
 * до бесконечности.
 * <p>
 * Объект класса Deadlock (a) содержит в себе ссылку на другой объект (b), тоже класса DeadLock.
 * В методе method() a хочет запустить такой же method() у b.
 * <p>
 * Метод method() синхронизирован, то есть, пока поток выполняет a.method(), больше a.method() недоступен никому.
 * <p>
 * Происходит следующее: поток с объектом a входит в метод a.method(), блокируюя a.method() для всех остальных. Затем
 * поток засыпает на 1000 миллисекунд. За это время запускается поток с объектом b, запускает и блокирует b.method().
 * Засыпает он дальше или нет - это уже неважно.
 * <p>
 * Когда поток с объектом a просыпается, он читает инструкцию: вызвать b.method(). Обращаясь, он видит: b.method() занят
 * потоком с объектом b. Поток с объектом a встает в ожидание...
 * <p>
 * Теперь просыпается поток с объектом b. Он читает инструкцию: вызвать a.method(). Обращается и тоже видит: a.method()
 * занят потомком с объектом a. Поток с объектом b встает в ожидание...
 * <p>
 * Итак: метод a.method() не может завершиться, потому что ждет окончания метода b.method().
 * Метод b.method() точно так же ждет окончания a.method(). Мы встали намертво. Дэдлок.
 */
public class Deadlock {
    private Deadlock other = null;
    private String name;

    public Deadlock(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Deadlock a = new Deadlock("First");
        Deadlock b = new Deadlock("Second");
        a.setOther(b);
        b.setOther(a);
        Deadlock main = new Deadlock("Main");
        Thread[] threads = main.threads(a, b);
        for (Thread obj : threads) {
            obj.start();
        }
    }

    void setOther(Deadlock other) {
        this.other = other;
    }

    synchronized void method() {
        System.out.format("%s: begin%n", this.name);
        try {
            Thread.sleep(1000);
            System.out.format("%s: calling %s%n", this.name, other.name);
            other.method();
            System.out.format("%s: ended (never happens)%n", this.name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Thread[] threads(Deadlock... objects) {
        Thread[] result = new Thread[objects.length];
        for (int i = 0; i < result.length; i++) {
            int finalI = i;
            result[i] = new Thread(() -> {
                objects[finalI].method();
            });
        }
        return result;
    }
}