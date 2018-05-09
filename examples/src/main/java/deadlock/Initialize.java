package deadlock;

/**
 * Дело в том, что согласно §5.5 спецификации JVM у каждого класса есть уникальный initialization lock,
 * который захватывается на время инициализации. Когда другой поток попытается обратиться к инициализируемому классу,
 * он будет заблокирован на этом локе до завершения инициализации первым потоком.
 * При конкурентной инициализации нескольких ссылающихся друг на друга классов нетрудно наткнуться на взаимную блокировку.
 */
public class Initialize {
    public static void main(String[] args) {
        new Thread(A::new).start();
        new B();
    }

    static class A {
        static final B b = new B();
    }

    static class B {
        static final A a = new A();
    }

}

