package different;

public class Different {
    public static void main(String[] args) {
        int x = 5;
        int y = 7;

        System.out.format("begin: x = %s, y = %s%n", x, y);
        x = x ^ y;
        System.out.format("\"x=x^y\" : x = %s, y = %s%n", x, y);
        y = x ^ y;
        System.out.format("\"y=x^y\" : x = %s, y = %s%n", x, y);
        x = x ^ y;
        System.out.format("\"x=x^y\" : x = %s, y = %s%n", x, y);

    }
}
