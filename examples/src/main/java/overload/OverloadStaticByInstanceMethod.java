package overload;

public class OverloadStaticByInstanceMethod {

    static void method() {
        System.out.println("Static");
    }

    public static void main(String[] args) {
        method();
        new OverloadStaticByInstanceMethod().method(24);
    }

    void method(int param) {
        System.out.println("Non-static");
    }
}
