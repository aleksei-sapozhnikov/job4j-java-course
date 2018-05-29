package initialize.blocks;

import java.io.IOException;
import java.util.ArrayList;

public class WhatBlocksCanBe {
    static int a;

    //static initialization block
    static {
        a = 0;
        // b = 2; // cannot be done
        if (a == 0) {
            System.out.println("statement");
            // throw new RuntimeException(); // can be thrown and cause ExceptionInInitializerError
        }
    }

    int b;

    // dynamic initialization block
    {
        a = 3;
        b = 5;
        if (b == 5) {
            //throw new IOException();
            throw new RuntimeException();
        }
    }

    //constructor
    WhatBlocksCanBe() throws IOException {
        System.out.println("statement");
    }

    public static void main(String[] args) throws IOException {
        // anonymous class dynamic initialization block
        new ArrayList<String>() {
            // now initialization block:
            {
                add("hello");
                add("goodbye");
            }
        };
        new WhatBlocksCanBe();
    }
}