package override;

public class TryOverrideStatic {
    public static void main(String[] args) {
        System.out.println("From classes:");
        Parent.method();
        Child.method();
        System.out.println();
        System.out.println("From instances:");
        Parent parent = new Parent();
        Parent child = new Child();
        parent.method();
        child.method();
    }
}

class Parent {
    static void method() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    static void method() {
        System.out.println("Child");
    }
}

