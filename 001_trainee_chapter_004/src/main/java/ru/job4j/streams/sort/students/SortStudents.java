package ru.job4j.streams.sort.students;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sorting students using java9-11 new Stream API features.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SortStudents {

    /**
     * Returns list of students with score more then given.
     *
     * @param students Given list of students.
     * @param bound    Minimum score to pass.
     * @return List of students who passed.
     */
    public List<Student> levelOf(List<Student> students, int bound) {
        return students.stream()
                .flatMap(Stream::ofNullable)
                .sorted(Comparator.reverseOrder())
                .takeWhile(student -> student.getScore() >= bound)
                .collect(Collectors.toList());
    }
}
