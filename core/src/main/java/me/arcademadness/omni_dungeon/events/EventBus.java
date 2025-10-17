package me.arcademadness.omni_dungeon.events;

import me.arcademadness.omni_dungeon.entities.PlayerEntity;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EventBus {

    private final Map<Class<?>, List<Handler>> handlers = new HashMap<>();

    private EventBus() {}

    private static class Holder {
        private static final EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return Holder.INSTANCE;
    }

    public void register(EventListener listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length != 1) {
                    throw new IllegalArgumentException(
                        "@Subscribe method must have exactly one parameter: " + method
                    );
                }
                Class<?> eventType = params[0];
                method.setAccessible(true);

                handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(new Handler(listener, method));
            }
        }
    }

    public void post(Event event) {
        List<Handler> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (Handler h : eventHandlers) {
                try {
                    h.method.invoke(h.listener, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (event instanceof BaseEvent) {
            if (!event.isCanceled()) {
                BaseEvent bEvent = (BaseEvent) event;
                bEvent.execute();
            }
        }
    }

    private static class Handler {
        final Object listener;
        final Method method;

        Handler(Object listener, Method method) {
            this.listener = listener;
            this.method = method;
        }
    }
}
