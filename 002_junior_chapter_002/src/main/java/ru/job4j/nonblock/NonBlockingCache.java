package ru.job4j.nonblock;

import java.util.concurrent.ConcurrentHashMap;

class NonBlockingCache<T extends SimpleModel> {
    private ConcurrentHashMap<Integer, T> map = new ConcurrentHashMap<>();

    boolean add(T model) {
        return this.map.putIfAbsent(model.id(), model) == null;
    }

    boolean update(T newModel) {
        boolean result = false;
        T value = this.map.get(newModel.id());
        if (value != null) {
            this.map.put(newModel.id(), newModel);
            result = true;
        }
        return result;
    }

    T delete(int id) {
        return this.map.remove(id);
    }
}
