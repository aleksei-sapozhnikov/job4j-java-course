package ru.job4j.sort.departments;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Tests {

    public static void main(String[] args) {
        String s1 = "K1\\SK1\\SSK1\\SSSK1";
        String s2 = "K1\\SK1\\SSK1";
        String s3 = "K1\\SK1";
        String s4 = "K1\\SK1\\SSK2";
        String s5 = "K1\\SK2\\SSK2";
        Set<String> set = new TreeSet<>(Arrays.asList(s1, s2, s3, s4, s5));
        System.out.println(set);

    }

}
