package ru.job4j.nonblock;

/**
 * Simple model to use in non-blocking cache which compares versions.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 20.04.2018
 */
class SimpleModel {
    /**
     * Model id.
     */
    private final int id;
    /**
     * Version. Must be incremented by 1 during every change by incrementVersion() method.
     */
    private final int version;
    /**
     * Model name.
     */
    private final String name;

    /**
     * Constructs new SimpleModel object.
     *
     * @param id   model id.
     * @param name model name.
     */
    SimpleModel(int id, String name) {
        this(id, name, 0);
    }

    /**
     * Constructs new object with needed version - to use in this class use only.
     * *
     *
     * @param id      model id.
     * @param name    model name.
     * @param version model version.
     */
    protected SimpleModel(int id, String name, int version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    /**
     * Returns model id.
     *
     * @return model id.
     */
    int id() {
        return this.id;
    }

    /**
     * Returns model version.
     *
     * @return model version.
     */
    int version() {
        return this.version;
    }

    /**
     * Returns model name.
     *
     * @return model name.
     */
    String name() {
        return this.name;
    }

    /**
     * Sets new model name - returns a new model.
     *
     * @param name new name.
     */
    SimpleModel changeName(String name) {
        return new SimpleModel(this.id, name, this.version + 1);
    }

    /**
     * Checks equality of this model to another object.
     *
     * @param other object to compare to.
     * @return <tt>true</tt> if objects are equal, <tt>false</tt> if not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        SimpleModel that = (SimpleModel) other;
        return this.id == that.id;
    }

    /**
     * Returns integer hashcode.
     *
     * @return integer hashcode for this object.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}
