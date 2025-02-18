package com.project;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;

public class Player {
    private Entity player;
    private Animation animationComponent;

    public Player(String image) {
        animationComponent = new Animation(image); // ส่งชื่อไฟล์ไปยัง Animation
    }

    public Entity createPlayer(double width , double height) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        player = FXGL.entityBuilder()
                .at(300, 300)
                .type(EntityType.PLAYER)
                .bbox(new HitBox("Body", BoundingShape.box(width, height)))
                .bbox(new HitBox("Body", new Point2D(12, 14), BoundingShape.box(width, height)))
                .with(physics, animationComponent, new CollidableComponent(true)) // เพิ่ม AnimationComponent เข้าไป
                .buildAndAttach();

        return player;
    }

    
}
