package me.arcademadness.omni_dungeon.controllers;

import java.util.Optional;

public interface Controller {
    Optional<ControlIntent> getIntent();
}
