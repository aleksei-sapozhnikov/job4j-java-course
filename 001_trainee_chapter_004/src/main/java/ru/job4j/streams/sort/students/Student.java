package ru.job4j.streams.sort.students;

import java.util.Objects;

/**
 * Simple comparable student class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Student implements Comparable<Student> {
    /**
     * Student name.
     */
    private String name;
    /**
     * Student graduate score.
     */
    private int score;

    /**
     * Constructs new instance.
     *
     * @param name  Student name.
     * @param score Student score.
     */
    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Returns score.
     *
     * @return Value of score field.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Compares this student to other.
     *
     * @param other Other student object.
     * @return negative if this is less then other, zero if the are equal,
     * positive if this is larger then other.
     */
    @Override
    public int compareTo(Student other) {
        int byScore = Integer.compare(this.score, other.score);
        return byScore != 0
                ? byScore
                : this.name.compareTo(other.name);
    }

    /**
     * Equals method.
     *
     * @param o Another object.
     * @return <tt>true</tt> if objects are equal, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return score == student.score
                && Objects.equals(name, student.name);
    }

    /**
     * Returns integer hashcode of the object.
     *
     * @return Integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }
}