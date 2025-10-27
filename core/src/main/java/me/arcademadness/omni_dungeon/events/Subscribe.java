package me.arcademadness.omni_dungeon.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Marks a method in an {@link EventListener} as a subscriber to events.
 * <p>
 * Methods annotated with {@link Subscribe} will automatically be called
 * by the {@link EventBus} when the appropriate {@link Event} is posted.
 * Subscriber methods should have a single parameter of the event type they
 * wish to listen to.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {}
