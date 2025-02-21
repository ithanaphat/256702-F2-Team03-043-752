package com.project;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MonsterFactory implements EntityFactory {
    @Spawns("monster")
    public Entity newMonster(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        

        Entity monster = entityBuilder()
        .type(EntityType.MONSTER)
        .at(data.getX(), data.getY())
        .viewWithBBox("dracomachina.png")
        .bbox(new HitBox("Body", BoundingShape.box(50, 50)))
        .bbox(new HitBox("Body", new Point2D(12, 14), BoundingShape.box(50, 50)))
        .with(new CollidableComponent(true), physics,new Health(50))
        .build();

        monster.addComponent(new MonsterAI(physics));
        return monster;
    }

    private static class MonsterAI extends Component {
        private final PhysicsComponent physics;
        private static final double SPEED = 100;

        public MonsterAI(PhysicsComponent physics) {
            this.physics = physics;
        }

        @Override
        public void onUpdate(double tpf) {
            var players = getGameWorld().getEntitiesByType(EntityType.PLAYER);

            if (!players.isEmpty()) {
                Entity player = players.get(0);
                Point2D monsterPos = entity.getPosition();
                Point2D playerPos = player.getPosition();
                Point2D direction = playerPos.subtract(monsterPos).normalize(); // คำนวณทิศทาง
                // ตั้งค่าความเร็วให้มอนสเตอร์เคลื่อนที่เข้าหาผู้เล่น
                physics.setVelocityX(direction.getX() * SPEED);
                physics.setVelocityY(direction.getY() * SPEED);
            }
        }

        
    }
}