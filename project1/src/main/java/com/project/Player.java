package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;

public class Player extends Entity {
    private Entity player;
    private Animation animationComponent;
    

    public Player(String image) {
        animationComponent = new Animation(image); // ส่งชื่อไฟล์ไปยัง Animation
    }

    public Entity createPlayer() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        player = FXGL.entityBuilder()
                .at(944, 416)
                .type(EntityType.PLAYER)
                .bbox(new HitBox("Body", BoundingShape.box(50, 50)))
                .bbox(new HitBox("Body", new Point2D(12, 14), BoundingShape.box(50, 50)))
                .with(physics, animationComponent, new CollidableComponent(true),new Health(100)) // เพิ่ม AnimationComponent เข้าไป
                .buildAndAttach();

        return player;
    }

    
}
