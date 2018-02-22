package ru.job4j.sort.departments;

import org.junit.Test;

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

}