package ru.job4j.professions;

import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Mark;
import ru.job4j.professions.attributes.School;
import ru.job4j.professions.attributes.Student;

/**
 * Teacher class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Teacher extends Profession {

    /**
     * Fields.
     */
    public School work;

    /**
     * Constructor.
     *
     * @param name    Name.
     * @param diploma Diploma.
     * @param age     Age (full years).
     * @param work    Place of work.
     */
    public Teacher(String name, Diploma diploma, int age, School work) {
        this.name = name;
        this.diploma = diploma;
        this.age = age;
        this.work = work;
    }

    /**
     * Teach some students.
     *
     * @param students Array of Students.
     */
    public void teach(Student[] students) {
        // list all students
        StringBuilder sb = new StringBuilder();
        for (Student stud : students) {
            sb
                    .append(stud.getName())
                    .append(". ");
        }
        String studentsNames = sb.toString();
        // do method
        System.out.println(
                "Учитель " + this.getName() +
                        " учит наукам студентов, в числе которых: " + studentsNames
        );
    }

    /**
     * Test student.
     *
     * @param student Student to test.
     * @return Mark gained by the student.
     */
    public Mark test(Student student) {
        Mark mark = new Mark("Оценен оценкой");
        System.out.println(
                "Учитель " + this.getName() +
                        "проверил знания студента " + student.getName() +
                        "и поставил ему оценку: \"" + mark.getName() + "\"" +
                        "."
        );
        return mark;
    }


}
