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

                .bbox(new HitBox("Body", BoundingShape.box(32, 32)))
                .bbox(new HitBox("Body", new Point2D(12, 14), BoundingShape.box(50, 50)))
                .with(new CollidableComponent(true), physics, new Health(monsterHealth))
                .scale(0.8, 0.8) // ปรับขนาดลงครึ่งนึง
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

                // Flip the monster texture based on the player's position
                if (playerPos.getX() < monsterPos.getX()) {
                    entity.setScaleX(0.8); // Flip to face left
                } else {
                    entity.setScaleX(-0.8); // Face right
                }
            }
        }

    }

    @Spawns("boss")
    public Entity newBoss(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        int bossHealth = 200;
        int bossDamage = 50;
        int bossExp = 100;

        Entity boss = entityBuilder()
                .type(EntityType.BOSS)
                .at(data.getX(), data.getY())
               // .viewWithBBox(bossImage)
                .bbox(new HitBox("Body", BoundingShape.box(64, 64)))
                .with(new CollidableComponent(true), physics, new Health(bossHealth))
                .with(new BossAnimation("boss.png")) // Add BossAnimation component
                .build();

        boss.setProperty("expReward", bossExp);
        boss.setProperty("damage", bossDamage);

        boss.addComponent(new BossAI(physics, boss.getComponent(BossAnimation.class)));
        return boss;
    }

    public static class BossAI extends Component {
        private final PhysicsComponent physics;
        private final BossAnimation bossAnimation;
        private static final double SPEED = 50;
        private boolean isStunned = false;
        private double stunTimer = 0;

        public BossAI(PhysicsComponent physics, BossAnimation bossAnimation) {
            this.physics = physics;
            this.bossAnimation = bossAnimation;

            
        }

        @Override
        public void onUpdate(double tpf) {
            if (isStunned) {
                stunTimer -= tpf;
                if (stunTimer <= 0) {
                    isStunned = false;
                }
                physics.setVelocityX(0);
                physics.setVelocityY(0);
                bossAnimation.attack(); // Trigger attack animation when stunned
                return;
            }

            var players = getGameWorld().getEntitiesByType(EntityType.PLAYER);

            if (!players.isEmpty()) {
                Entity player = players.get(0);
                Point2D bossPos = entity.getPosition();
                Point2D playerPos = player.getPosition();
                Point2D direction = playerPos.subtract(bossPos).normalize();
                physics.setVelocityX(direction.getX() * SPEED);
                physics.setVelocityY(direction.getY() * SPEED);

                // Update BossAnimation speed
                bossAnimation.setSpeedX((int) (direction.getX() * SPEED));
                bossAnimation.setSpeedY((int) (direction.getY() * SPEED));

                // Flip the boss texture based on the player's position
            if (playerPos.getX() < bossPos.getX()) {
                entity.setScaleX(-2); // Flip to face left
            } else {
                entity.setScaleX(2); // Face right
            }

            }
        }

        
    }

    private int calculateMonsterHealth(int playerLevel) {
        if (playerLevel >= 20)
            return 80;
        else if (playerLevel >= 10)
            return 50;
        else
            return 20;
    }

    private int calculateMonsterExp(int playerLevel) {
        if (playerLevel >= 20)
            return 50000;
        else if (playerLevel >= 10)
            return 25000;
        else
            return 5000;
    }

    private int calculateMonsterDamage(int playerLevel) {
        if (playerLevel >= 20)
            return 30;
        else if (playerLevel >= 10)
            return 20;
        else
            return 10;
    }

    private String getMonsterImageByLevel(int playerLevel) {
        if (playerLevel >= 20)
            return "en3.png";
        else if (playerLevel >= 10)
            return "en2.png";
        else
            return "en1.png";
    }

    

}