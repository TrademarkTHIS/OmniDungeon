package me.arcademadness.omni_dungeon.controllers;

import me.arcademadness.omni_dungeon.entities.Entity;

public interface Controller {
    ControlIntent getIntent();
}
