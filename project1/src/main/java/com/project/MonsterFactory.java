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

import com.almasb.fxgl.dsl.FXGL;

public class MonsterFactory implements EntityFactory {
    @Spawns("monster")
    public Entity newMonster(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
       


        Stats playerStats = FXGL.geto("playerStats");
        
        
        int playerLevel = playerStats.getLevel();
        String monsterImage = getMonsterImageByLevel(playerLevel);

        int monsterHealth = calculateMonsterHealth(playerLevel);
        int monsterDamage = calculateMonsterDamage(playerLevel);
        int monsterExp = calculateMonsterExp(playerLevel);

        Entity monster = entityBuilder()
                .type(EntityType.MONSTER)
                .at(data.getX(), data.getY())
                .viewWithBBox(monsterImage)

                .bbox(new HitBox("Body", BoundingShape.box(50, 50)))
                .bbox(new HitBox("Body", new Point2D(12, 14), BoundingShape.box(50, 50)))
                .with(new CollidableComponent(true), physics, new Health(monsterHealth))
                .build();

        monster.setProperty("expReward", monsterExp); // ใส่ EXP ที่จะได้รับ
        monster.setProperty("damage", monsterDamage); // ใส่ค่าดาเมจของมอนสเตอร์

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

    private int calculateMonsterHealth(int playerLevel) {
        if (playerLevel >= 3)
            return 80;
        else if (playerLevel >= 2)
            return 50;
        else
            return 20;
    }

    private int calculateMonsterExp(int playerLevel) {
        if (playerLevel >= 3)
            return 20;
        else if (playerLevel >= 2)
            return 10;
        else
            return 5;
    }

    private int calculateMonsterDamage(int playerLevel) {
        if (playerLevel >= 3)
            return 30;
        else if (playerLevel >= 2)
            return 20;
        else
            return 10;
    }

    private String getMonsterImageByLevel(int playerLevel) {
        if (playerLevel >= 3)
            return "en3.png";
        else if (playerLevel >= 2)
            return "en2.png";
        else
            return "en1.png";
    }
    

}