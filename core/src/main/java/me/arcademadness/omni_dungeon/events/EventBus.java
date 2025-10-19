package me.arcademadness.omni_dungeon.events;

import java.lang.reflect.Method;
import java.util.*;

public class EventBus {

    private final Map<Class<?>, List<Handler>> handlers = new HashMap<>();

    public EventBus() {
        // No singleton — each Environment or system can create its own bus
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

    public void unregister(EventListener listener) {
        for (List<Handler> handlerList : handlers.values()) {
            handlerList.removeIf(h -> h.listener == listener);
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
            BaseEvent bEvent = (BaseEvent) event;
            if (!bEvent.isCanceled()) {
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
