package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) throws OptimisticException {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        Base stored = memory.get(model.id());
        BiFunction<Integer, Base, Base> increase = (ver, base) -> {
            ver = stored.version();
            base = model;
            if (ver != model.version()) {
                throw new OptimisticException("Versions are not equal");
            }
            return new Base(base.id(), base.name(), ver + 1);
        };
        return memory.computeIfPresent(stored.id(), increase) != null;
    }

    public void delete(int id) {
        memory.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
