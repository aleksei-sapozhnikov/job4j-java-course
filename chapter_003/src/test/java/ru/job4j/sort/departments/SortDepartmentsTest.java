package ru.job4j.sort.departments;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the SortDepartments class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.02.2018
 */
public class SortDepartmentsTest {

    /**
     * Test addString(String str, String delimiter) test.
     */
    @Test
    public void whenGivenStringToEmptyStructureThenStructure() {
        SortDepartments sort = new SortDepartments();
        String delimiter = "\\";
        String input = "K\\SK\\SSK\\SSSK";
        sort.addString(input, delimiter);
        sort.printAll(delimiter);
    }

    @Test
    public void whenGivenStringToNotEmptyStructureThenAddedStructure() {
        SortDepartments sort = new SortDepartments();
        String delimiter = "\\";
        String input1 = "K2\\SK1";
        String input2 = "K1\\SK1";
        String input3 = "K1\\SK2";
        String input4 = "K1\\SK3";
        String input5 = "K1\\SK2\\SSK1";
        String input6 = "K1\\SK2\\SSK2";
        String input7 = "K1\\SK2\\SK1";
        sort.addString(input1, delimiter);
        sort.addString(input2, delimiter);
        sort.addString(input3, delimiter);
        sort.addString(input4, delimiter);
        sort.addString(input5, delimiter);
        sort.addString(input6, delimiter);
        sort.addString(input7, delimiter);
        sort.printAll(delimiter);
    }

}