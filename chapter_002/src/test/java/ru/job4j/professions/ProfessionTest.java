package ru.job4j.professions;

import org.junit.Test;
import ru.job4j.professions.attributes.Bureau;
import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Hospital;
import ru.job4j.professions.attributes.School;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class ProfessionTest {

    /**
     * Test getName method.
     */
    @Test
    public void whenGetNameOfAnyProfessionThenName() {
        //doctor
        Diploma diplomaDoctor = new Diploma("1-й медицинский институт");
        Hospital workDoctor = new Hospital("2-я городская больница");
        Doctor doctor = new Doctor("Сидор Иванов", diplomaDoctor, 32, workDoctor);
        //engineer
        Diploma diplomaEngineer = new Diploma("Первое инженерное училище");
        Bureau workEngineer = new Bureau("Главное НИИ разработки");
        Engineer engineer = new Engineer("Захар Пряткин", diplomaEngineer, 56, workEngineer);
        // teacher
        Diploma diplomaTeacher = new Diploma("РГПУ имени Герцена");
        School workTeacher = new School("Школа 532");
        Teacher teacher = new Teacher("Пал Палыч", diplomaTeacher, 82, workTeacher);
        // array
        Profession[] professions = {doctor, engineer, teacher};
        // getters
        String[] result = new String[professions.length];
        for (int i = 0; i < professions.length; i++) {
            result[i] = professions[i].getName();
        }
        String[] expected = {
                "Сидор Иванов",
                "Захар Пряткин",
                "Пал Палыч"
        };
        assertArrayEquals(result, expected);
    }

    /**
     * Test getDiploma method.
     */
    @Test
    public void whenGetOfAnyProfessionThenDiplomaOfAnyProfession() {
        //doctor
        Diploma diplomaDoctor = new Diploma("1-й медицинский институт");
        Hospital workDoctor = new Hospital("2-я городская больница");
        Doctor doctor = new Doctor("Сидор Иванов", diplomaDoctor, 32, workDoctor);
        //engineer
        Diploma diplomaEngineer = new Diploma("Первое инженерное училище");
        Bureau workEngineer = new Bureau("Главное НИИ разработки");
        Engineer engineer = new Engineer("Захар Пряткин", diplomaEngineer, 56, workEngineer);
        // teacher
        Diploma diplomaTeacher = new Diploma("РГПУ имени Герцена");
        School workTeacher = new School("Школа 532");
        Teacher teacher = new Teacher("Пал Палыч", diplomaTeacher, 82, workTeacher);
        // array
        Profession[] professions = {doctor, engineer, teacher};
        // getters
        String[] result = new String[professions.length];
        for (int i = 0; i < professions.length; i++) {
            result[i] = professions[i].getDiploma().getName();
        }
        String[] expected = {
                "1-й медицинский институт",
                "Первое инженерное училище",
                "РГПУ имени Герцена"
        };
        assertArrayEquals(result, expected);
    }

    /**
     * Test getAge method.
     */
    @Test
    public void whenGetAgeOfAnyProfessionThenAgeOfAnyProfession() {
        //doctor
        Diploma diplomaDoctor = new Diploma("1-й медицинский институт");
        Hospital workDoctor = new Hospital("2-я городская больница");
        Doctor doctor = new Doctor("Сидор Иванов", diplomaDoctor, 32, workDoctor);
        //engineer
        Diploma diplomaEngineer = new Diploma("Первое инженерное училище");
        Bureau workEngineer = new Bureau("Главное НИИ разработки");
        Engineer engineer = new Engineer("Захар Пряткин", diplomaEngineer, 56, workEngineer);
        // teacher
        Diploma diplomaTeacher = new Diploma("РГПУ имени Герцена");
        School workTeacher = new School("Школа 532");
        Teacher teacher = new Teacher("Пал Палыч", diplomaTeacher, 82, workTeacher);
        // array
        Profession[] professions = {doctor, engineer, teacher};
        // getters
        int[] result = new int[professions.length];
        for (int i = 0; i < professions.length; i++) {
            result[i] = professions[i].getAge();
        }
        int[] expected = {
                32,
                56,
                82
        };
        assertThat(result, is(expected));
    }
}