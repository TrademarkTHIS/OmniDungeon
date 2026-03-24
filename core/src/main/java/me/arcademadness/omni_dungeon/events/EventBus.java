package me.arcademadness.omni_dungeon.events;

import me.arcademadness.omni_dungeon.util.StableList;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;

public class EventBus {

    private final Map<Class<?>, StableList<Handler>> handlers = new HashMap<>();

    public EventBus() {
    }

    public List<Registration> register(EventListener listener) {
        List<Registration> registrations = new ArrayList<>();

        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class)) {
                continue;
            }

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1) {
                throw new IllegalArgumentException(
                    "@Subscribe method must have exactly one parameter: " + method
                );
            }

            Class<?> eventType = params[0];
            method.setAccessible(true);

            MethodHandle handle;
            try {
                handle = MethodHandles.lookup().unreflect(method);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to create MethodHandle for " + method, e);
            }

            StableList<Handler> list =
                handlers.computeIfAbsent(eventType, k -> new StableList<>());

            Handler handler = new Handler(listener, handle);

            long id = list.add(handler);
            StableList.Handle<Handler> handleObj = list.createHandle(id);
            registrations.add(new Registration(list, handleObj));
        }

        return registrations;
    }

    public void unregister(Registration registration) {
        if (registration == null) return;
        StableList.Handle<Handler> handle = registration.handle;
        if (!handle.isValid()) return;
        registration.list.removeById(handle.getId());
    }

    public void unregister(EventListener listener) {
        for (StableList<Handler> list : handlers.values()) {
            list.removeIf(h -> h.listener == listener);
        }
    }

    public void unregister(Class<?> eventClass) {
        handlers.remove(eventClass);
    }

    public void post(Event event) {
        Class<?> eventClass = event.getClass();

        for (Map.Entry<Class<?>, StableList<Handler>> entry : handlers.entrySet()) {
            if (!entry.getKey().isAssignableFrom(eventClass)) continue;

            StableList<Handler> list = entry.getValue();

            int size = list.size();
            for (int i = size - 1; i >= 0; i--) {
                Handler handler = list.data().get(i);
                if (handler == null) continue;

                try {
                    handler.handle.invoke(handler.listener, event);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        if (event instanceof BaseEvent baseEvent && !baseEvent.isCanceled()) {
            baseEvent.execute();
        }
    }

    public void clear() {
        handlers.clear();
    }

    private record Handler(EventListener listener, MethodHandle handle) { }

    public static class Registration {
        private final StableList<Handler> list;
        private final StableList.Handle<Handler> handle;

        Registration(StableList<Handler> list, StableList.Handle<Handler> handle) {
            this.list = list;
            this.handle = handle;
        }
    }
}
