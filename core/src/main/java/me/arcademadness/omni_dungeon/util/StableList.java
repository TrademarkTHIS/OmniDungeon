package me.arcademadness.omni_dungeon.util;

import java.util.ArrayList;
import java.util.List;

public class StableList<T> {

    public static final long INVALID_ID = Long.MAX_VALUE;

    private static final class Metadata {
        long id;
        long generation;

        Metadata(long id, long generation) {
            this.id = id;
            this.generation = generation;
        }
    }

    public static final class Handle<T> {

        private final long id;
        private final long generation;
        private final StableList<T> owner;

        private Handle(long id, long generation, StableList<T> owner) {
            this.id = id;
            this.generation = generation;
            this.owner = owner;
        }

        public boolean isValid() {
            return owner != null && owner.isValid(id, generation);
        }

        public T get() {
            if (!isValid()) {
                throw new IllegalStateException("Handle is invalid");
            }
            return owner.getById(id);
        }

        public T orNull() {
            return isValid() ? owner.getById(id) : null;
        }

        public long getId() {
            return id;
        }
    }

    private final List<T> data = new ArrayList<>();
    private final List<Metadata> metadata = new ArrayList<>();
    private final List<Integer> idToIndex = new ArrayList<>();

    private long getFreeId() {

        if (metadata.size() > data.size()) {
            Metadata m = metadata.get(data.size());
            m.generation++;
            return m.id;
        }

        long id = metadata.size();

        metadata.add(new Metadata(id, 0));
        idToIndex.add(0);

        return id;
    }

    public long add(T element) {

        long id = getFreeId();
        int index = data.size();

        data.add(element);

        idToIndex.set((int) id, index);

        Metadata m = metadata.get(index);
        m.id = id;

        return id;
    }

    public void removeById(long id) {

        int index = idToIndex.get((int) id);
        int lastIndex = data.size() - 1;

        Metadata removed = metadata.get(index);
        removed.generation++;

        int lastId = (int) metadata.get(lastIndex).id;

        T lastElement = data.get(lastIndex);
        data.set(index, lastElement);

        Metadata lastMeta = metadata.get(lastIndex);
        metadata.set(index, lastMeta);
        metadata.set(lastIndex, removed);

        idToIndex.set(lastId, index);

        data.remove(lastIndex);
    }

    public T getById(long id) {
        return data.get(idToIndex.get((int) id));
    }

    public int getDataIndex(long id) {
        return idToIndex.get((int) id);
    }

    public long getIdAtIndex(int index) {
        return metadata.get(index).id;
    }

    public Handle<T> createHandle(long id) {

        int index = idToIndex.get((int) id);
        Metadata m = metadata.get(index);

        return new Handle<>(id, m.generation, this);
    }

    public boolean isValid(long id, long generation) {

        if (id >= idToIndex.size()) {
            return false;
        }

        int index = idToIndex.get((int) id);

        if (index >= metadata.size()) {
            return false;
        }

        return metadata.get(index).generation == generation;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {

        data.clear();

        for (Metadata m : metadata) {
            m.generation++;
        }
    }

    public void reserve(int capacity) {
        ((ArrayList<T>) data).ensureCapacity(capacity);
        ((ArrayList<Metadata>) metadata).ensureCapacity(capacity);
        ((ArrayList<Integer>) idToIndex).ensureCapacity(capacity);
    }

    public List<T> data() {
        return data;
    }

    public void removeIf(java.util.function.Predicate<T> predicate) {

        for (int i = 0; i < data.size();) {
            if (predicate.test(data.get(i))) {
                removeById(metadata.get(i).id);
            } else {
                i++;
            }
        }
    }
}
