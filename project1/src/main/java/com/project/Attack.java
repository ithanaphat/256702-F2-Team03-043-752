package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Attack {
    public static void init() {
        FXGL.getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                // ดึงตำแหน่งของผู้เล่น
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Point2D playerPos = player.getPosition();

                // หา Monster ที่ใกล้ที่สุด
                Entity nearestMonster = getGameWorld().getEntitiesByType(EntityType.MONSTER).stream()
                    .min((m1, m2) -> {
                        double d1 = m1.getPosition().distance(playerPos);
                        double d2 = m2.getPosition().distance(playerPos);
                        return Double.compare(d1, d2);
                    })
                    .orElse(null);

                // ถ้าเจอมอนสเตอร์ที่อยู่ในระยะ ให้ลดพลังชีวิต
                if (nearestMonster != null && playerPos.distance(nearestMonster.getPosition()) < 80) {
                    Health health = nearestMonster.getComponent(Health.class);
                    health.damage(10); // ✅ ลดพลังชีวิตของมอนสเตอร์

                    // ถ้าพลังชีวิตหมด ให้ลบมอนสเตอร์ออกจากเกม
                    if (health.getHealth() <= 0) {
                        nearestMonster.removeFromWorld();
                    }
                }
            }
        }, MouseButton.PRIMARY); // ✅ ใช้คลิกซ้ายเพื่อโจมตี
    }
}
