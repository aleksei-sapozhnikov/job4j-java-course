package ru.job4j.sort.departments;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Department class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.02.2018
 */
public class DepartmentTest {

    /**
     * Test hierarchyString(String delimiter) method.
     */
    @Test
    public void whenHierarchyStringThenReturnRightString() {
        Department k = new Department("K", null);
        Department sk = new Department("SK", k);
        Department ssk = new Department("SSK", sk);
        Department sssk = new Department("SSSK", ssk);
        String delimiter = "\\";
        String result = sssk.hierarchyString(delimiter);
        String expected = "K\\SK\\SSK\\SSSK";
        assertThat(result, is(expected));
    }

    /**
     * Test findLevel() private method.
     */
    @Test
    public void whenFindLevelThenRightLevel() {
        Department k = new Department("K", null);
        Department sk = new Department("SK", k);
        Department ssk = new Department("SSK", sk);
        assertThat(k.level(), is(0));
        assertThat(sk.level(), is(1));
        assertThat(ssk.level(), is(2));
    }

    @Test
    public void compare() {
        Department k1 = new Department("K1", null);
        Department sk1 = new Department("SK1", k1);
        Department ssk1 = new Department("SK1", sk1);

        Department k2 = new Department("K2", null);
        Department sk2 = new Department("SK1", k2);

        Set<Department> set = new TreeSet<>(Arrays.asList(
                k1, sk1, ssk1, k2, sk2
        ));

        System.out.println(set);

        System.out.println(ssk1.compareTo(sk2));
        System.out.println(ssk1);
        System.out.println(sk2);


    }

}