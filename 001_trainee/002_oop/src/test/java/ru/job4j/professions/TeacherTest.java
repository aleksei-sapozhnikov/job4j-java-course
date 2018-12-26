package ru.job4j.professions;

import org.junit.Test;
import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Mark;
import ru.job4j.professions.attributes.School;
import ru.job4j.professions.attributes.Student;

import static org.junit.Assert.assertEquals;

public class TeacherTest {

    /**
     * Test teach method.
     */
    @Test
    public void whenTeachStudentsThenStringMessage() {
        Diploma diploma = new Diploma("РГПУ имени Герцена");
        School school = new School("Школа 532");
        Teacher teacher = new Teacher("Пал Палыч", diploma, 82, school);
        Student[] students = new Student[]{
                new Student("Васечкин"),
                new Student("Петечкин"),
                new Student("Маришкина"),
                new Student("Гундяева"),
        };
        String result = teacher.teach(students);
        String expected = "Учитель Пал Палыч учит наукам студентов, в числе которых: Васечкин. Петечкин. Маришкина. Гундяева. ";
        assertEquals(result, expected);
    }

    /**
     * Test test method.
     */
    @Test
    public void whenTestStudentThenMarkWithName() {
        Diploma diploma = new Diploma("РГПУ имени Герцена");
        School school = new School("Школа 532");
        Teacher teacher = new Teacher("Пал Палыч", diploma, 82, school);
        Student student = new Student("Васечкин");
        Mark result = teacher.test(student);
        Mark expected = new Mark("Оценен оценкой");
        assertEquals(result.getName(), expected.getName());

    }
}