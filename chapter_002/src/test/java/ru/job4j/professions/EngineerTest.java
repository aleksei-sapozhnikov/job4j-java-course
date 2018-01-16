package ru.job4j.professions;

import org.junit.Test;
import ru.job4j.professions.attributes.Bureau;
import ru.job4j.professions.attributes.Construction;
import ru.job4j.professions.attributes.Diploma;
import ru.job4j.professions.attributes.Project;

import static org.junit.Assert.*;

/**
 * Test for Engineer class.
 */
public class EngineerTest {

    /**
     * Test design method.
     */
    @Test
    public void whenDesignThenProjectWithName() {
        Diploma diploma = new Diploma("Первое инженерное училище");
        Bureau bureau = new Bureau("Главное НИИ разработки");
        Engineer engineer = new Engineer("Захар Пряткин", diploma, 56, bureau);
        Project result = engineer.design();
        Project expected = new Project("Очередной проект");
        assertEquals(result.name, expected.name);
    }

    /**
     * Test build method.
     */
    @Test
    public void whenBuildProjectThenConstructionWithName() {
        Diploma diploma = new Diploma("Первое инженерное училище");
        Bureau bureau = new Bureau("Главное НИИ разработки");
        Engineer engineer = new Engineer("Захар Пряткин", diploma, 56, bureau);
        Project project = new Project("Очередной проект");
        Construction result = engineer.build(project);
        Construction expected = new Construction("Очередной дом");
        assertEquals(result.name, expected.name);
    }

    /**
     * Test study method.
     */
    @Test
    public void whenStudyConstructionThenProjectWithName() {
        Diploma diploma = new Diploma("Первое инженерное училище");
        Bureau bureau = new Bureau("Главное НИИ разработки");
        Engineer engineer = new Engineer("Захар Пряткин", diploma, 56, bureau);
        Construction construction = new Construction("Очередной дом");
        Project result = engineer.study(construction);
        Project expected = new Project("Чертеж изученной конструкции");
        assertEquals(result.name, expected.name);
    }
}