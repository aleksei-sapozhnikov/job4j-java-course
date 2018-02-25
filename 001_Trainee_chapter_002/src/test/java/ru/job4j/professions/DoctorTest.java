package ru.job4j.professions;

import org.junit.Test;
import ru.job4j.professions.attributes.Diagnosis;
import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Hospital;
import ru.job4j.professions.attributes.Patient;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Doctor class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 16.01.2018
 */
public class DoctorTest {

    /**
     * Test diagnose method.
     */
    @Test
    public void whenDiagnosePatientThenDiagnosisWithName() {
        Diploma diploma = new Diploma("1-й медицинский институт");
        Hospital hospital = new Hospital("2-я городская больница");
        Doctor doctor = new Doctor("Сидор Иванов", diploma, 32, hospital);
        Patient patient = new Patient("Иван Сидоров");
        Diagnosis result = doctor.diagnose(patient);
        Diagnosis expected = new Diagnosis("Болеет болезнью");
        assertEquals(result.getName(), expected.getName());
    }

    /**
     * Test heal method.
     */
    @Test
    public void whenHealPatientThenStringMessage() {
        Diploma diploma = new Diploma("1-й медицинский институт");
        Hospital hospital = new Hospital("2-я городская больница");
        Doctor doctor = new Doctor("Сидор Иванов", diploma, 32, hospital);
        Patient patient = new Patient("Иван Сидоров");
        Diagnosis diagnosis = new Diagnosis("Болеет болезнью");
        String result = doctor.heal(patient, diagnosis);
        String expected = "Доктор Сидор Иванов вылечил пациента Иван Сидоров, у которого был диагноз: \"Болеет болезнью\".";
        assertEquals(result, expected);
    }
}