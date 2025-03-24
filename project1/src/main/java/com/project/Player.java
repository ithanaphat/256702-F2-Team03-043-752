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
    private Stats stats; // เพิ่มตัวแปร Stats

    public Player(String image) {
        animationComponent = new Animation(image); // ส่งชื่อไฟล์ไปยัง Animation
    }

    public Entity createPlayer(int health, int score, int attack, int level) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        stats = new Stats(health, score, attack, level); // สร้าง Stats component

        player = FXGL.entityBuilder()
                .at(18.00, 598.00)
                .type(EntityType.PLAYER)
                .bbox(new HitBox("Body", new Point2D(12, 14), BoundingShape.box(40, 40)))
                .anchorFromCenter() // ทำให้ BBOX อยู่ตรงกลาง Entity
                .with(physics, animationComponent, new CollidableComponent(true), new Health(health), stats) // เพิ่ม
                                                                                                             // Stats
                                                                                                             // component
                                                                                                             // เข้าไป
                .buildAndAttach();

        return player;
    }

    public Entity getPlayerEntity() {
        return player;
    }

    public Stats getStats() {
        return stats;
    }

}
