package ru.job4j.professions;

import ru.job4j.professions.attributes.Diagnosis;
import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Hospital;
import ru.job4j.professions.attributes.Patient;

/**
 * Doctor class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Doctor extends Profession {

    /**
     * Fields.
     */
    private Hospital work;

    /**
     * Constructor.
     *
     * @param name    Name.
     * @param diploma Diploma.
     * @param age     Age (full years).
     * @param work    Place of work.
     */
    public Doctor(String name, Diploma diploma, int age, Hospital work) {
        super(name, diploma, age);
        this.work = work;
    }

    /**
     * Diagnose patient.
     *
     * @param patient Patient to diagnose.
     * @return Diagnosis for this patient.
     */
    public Diagnosis diagnose(Patient patient) {
        Diagnosis diagnosis = new Diagnosis("Болеет болезнью");
        System.out.println(
                "Доктор " + this.getName()
                        + " диагностирует пациента " + patient.getName()
                        + " и ставит диагноз: \"" + diagnosis.getName() + "\""
                        + "."
        );
        return diagnosis;
    }

    /**
     * Heal patient.
     *
     * @param patient   Patient to heal.
     * @param diagnosis Diagnosis to heal.
     */
    public String heal(Patient patient, Diagnosis diagnosis) {
        String result = "Доктор " + this.getName()
                + " вылечил пациента " + patient.getName()
                + ", у которого был диагноз: \"" + diagnosis.getName() + "\""
                + ".";
        System.out.println(
                "Доктор " + this.getName()
                        + " вылечил пациента " + patient.getName()
                        + ", у которого был диагноз: \"" + diagnosis.getName() + "\""
                        + "."
        );
        return result;
    }

}
