package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.input.KeyCode;

public class Player {
    private Entity entity;

    public Player(double x, double y) {
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view("player.png").scale(1.0, 1.0)
                .buildAndAttach();
    }

    public Entity getEntity() {
        return entity;
    }

    public void moveLeft() {
        entity.translateX(-5);
        FXGL.inc("pixelsMoved", 5);
    }

    public void moveRight() {
        entity.translateX(5);
        FXGL.inc("pixelsMoved", 5);
    }

    public void moveUp() {
        entity.translateY(-5);
        FXGL.inc("pixelsMoved", 5);
    }

    public void moveDown() {
        entity.translateY(5);
        FXGL.inc("pixelsMoved", 5);
    }

    public void registerControls() {
        FXGL.onKey(KeyCode.LEFT, this::moveLeft);
        FXGL.onKey(KeyCode.RIGHT, this::moveRight);
        FXGL.onKey(KeyCode.UP, this::moveUp);
        FXGL.onKey(KeyCode.DOWN, this::moveDown);
    }
}
