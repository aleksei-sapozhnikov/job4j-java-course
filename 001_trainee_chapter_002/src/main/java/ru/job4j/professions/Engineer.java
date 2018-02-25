package ru.job4j.professions;

import ru.job4j.professions.attributes.Bureau;
import ru.job4j.professions.attributes.Construction;
import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Project;

/**
 * Engineer class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Engineer extends Profession {

    /**
     * Fields.
     */
    private Bureau work;

    /**
     * Constructor.
     *
     * @param name    Name.
     * @param diploma Diploma.
     * @param age     Age (full years).
     * @param work    Place of work.
     */
    public Engineer(String name, Diploma diploma, int age, Bureau work) {
        super(name, diploma, age);
        this.work = work;
    }

    /**
     * Design new project.
     *
     * @return New Project.
     */
    public Project design() {
        Project project = new Project("Очередной проект");
        System.out.println(
                "Инженер " + this.getName()
                        + " разработал новый проект: \"" + project.getName() + "\""
                        + "."
        );
        return project;
    }

    /**
     * Build construction.
     *
     * @param project Project for the construction.
     * @return Construction built.
     */
    public Construction build(Project project) {
        Construction construction = new Construction("Очередной дом");
        System.out.println(
                "Инженер " + this.getName()
                        + " построил сооружение \"" + construction.getName() + "\""
                        + " по проекту \"" + project.getName() + "\""
                        + "."
        );
        return construction;
    }

    /**
     * Study construction to make its project.
     *
     * @param construction Construction to study.
     * @return Project of the construction.
     */
    public Project study(Construction construction) {
        Project project = new Project("Чертеж изученной конструкции");
        System.out.println(
                "Инженер " + this.getName()
                        + " изучил конструкцию \"" + construction.getName() + "\""
                        + " и создал проект \"" + project.getName() + "\""
                        + "."
        );
        return project;
    }
}
