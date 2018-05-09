package locks;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Несколько READER-ов могут зайти через замок readLock, но только один WRITER может зайти через writeLock
 * и заблокировать объект для всех остальных, пока не закончит свои дела.
 */
public class ReadWrite {
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock readLock = rwl.readLock();
    Runnable reader = () -> {
        readLock.lock();
        try {
            System.out.println("    +READER work");
            System.out.flush();
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            System.out.println("    -READER exited");
            System.out.flush();
        }
    };
    private Lock writeLock = rwl.writeLock();
    Runnable writer = () -> {
        writeLock.lock();
        try {
            System.out.println(" +WRITER work");
            System.out.flush();
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
            System.out.println(" -WRITER exited");
            System.out.flush();
        }
    };

    public static void main(String[] args) throws InterruptedException {
        ReadWrite r = new ReadWrite();
        for (int i = 0; i < 10; i++) {
            new Thread(r.reader).start();
            new Thread(r.writer).start();
        }
    }
}
