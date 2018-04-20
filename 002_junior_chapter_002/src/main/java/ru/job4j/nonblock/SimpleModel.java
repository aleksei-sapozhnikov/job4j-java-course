package ru.job4j.nonblock;

class SimpleModel {
    private final int id;
    private int version = 0;

    SimpleModel(int id) {
        this.id = id;
    }

    int id() {
        return this.id();
    }

    int version() {
        return this.version;
    }

    void nextVersion() {
        this.version++;
    }

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

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}
