package com.project;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import javafx.util.Duration;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class App extends GameApplication {
    private Stats stats;
    private UIManager uiManager;
    private SkillSystem skillSystem;
    private Entity player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("The Last Adventurer");
        settings.setVersion("0.1");

        settings.setMainMenuEnabled(true); // ✅ ต้องเปิดใช้งาน

        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new StartScreen();
            }

            @Override
            public FXGLMenu newGameMenu() { // ✅ เพิ่ม Game Menu (Pause Menu)
                return new PauseMenu();
            }
        });

    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.setGravity(0, 0);

        entityBuilder()
                .at(-30, -30)
                .bbox(BoundingShape.box(getAppWidth(), 10)) // กำแพงด้านบน
                .with(new PhysicsComponent())
                .buildAndAttach();

        entityBuilder()
                .at(0, getAppHeight() - 0)
                .bbox(BoundingShape.box(getAppWidth(), 10)) // กำแพงด้านล่าง
                .with(new PhysicsComponent())
                .buildAndAttach();

        entityBuilder()
                .at(-30, -30)
                .bbox(BoundingShape.box(10, getAppHeight())) // กำแพงด้านซ้าย
                .with(new PhysicsComponent())
                .buildAndAttach();

        entityBuilder()
                .at(getAppWidth() + 30, 0)
                .bbox(BoundingShape.box(10, getAppHeight())) // กำแพงด้านขวา
                .with(new PhysicsComponent())
                .buildAndAttach();
                onCollisionBegin(EntityType.MONSTER, EntityType.PLAYER, (monster, player) -> {
                    stats.damage(20); // ✅ ลดพลังชีวิตลง 20
                    uiManager.updateHealthDisplay(); // ✅ อัปเดต Health Bar
        
                });

    }

    @Override
    protected void initInput() {

        FXGL.getInput().clearAll();

        FXGL.getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                // ดึงตำแหน่งของผู้เล่น
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Point2D playerPos = player.getPosition();

                // Trigger attack effect
                Animation.playAttackEffect(playerPos, Color.RED, Color.ORANGE);

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
                    int attackPower = stats.getAttack(); // ดึงค่าพลังโจมตีของผู้เล่น
                    health.damage(attackPower); // ✅ ลดพลังชีวิตของมอนสเตอร์

                    // ถ้าพลังชีวิตหมด ให้ลบมอนสเตอร์ออกจากเกม
                    if (health.getHealth() <= 0) {
                        // Trigger death effect
                        Animation.playEffect(nearestMonster.getPosition(), Color.BLACK, Color.BLACK);

                        nearestMonster.removeFromWorld();

                        // เพิ่มค่า experience ให้กับผู้เล่น
                        Stats playerStats = FXGL.geto("playerStats");
                        playerStats.addExperience(5);


                        // อัปเดต UI
                        UIManager uiManager = FXGL.geto("uiManager");
                        uiManager.updateHealthDisplay();
                    }
                }
            }
        }, MouseButton.PRIMARY); // ✅ ใช้คลิกซ้ายเพื่อโจมตี

        FXGL.getInput().addAction(new UserAction("Skill Q") {
            @Override
            protected void onActionBegin() {
                skillSystem.activateSkill(KeyCode.Q);

                uiManager.updateHealthDisplay();
            }
        }, KeyCode.Q);

        FXGL.getInput().addAction(new UserAction("Skill E") {
            @Override
            protected void onActionBegin() {
                skillSystem.activateSkill(KeyCode.E);

                uiManager.updateHealthDisplay();
            }
        }, KeyCode.E);

        FXGL.getInput().addAction(new UserAction("Skill R") {
            @Override
            protected void onActionBegin() {
                skillSystem.activateSkill(KeyCode.R);

            }
        }, KeyCode.R);

        FXGL.getInput().addAction(new UserAction("Pause") {
            @Override
            protected void onActionBegin() {
                FXGL.getSceneService().pushSubScene(new PauseMenu());
            }
        }, KeyCode.ESCAPE);

    }

    @Override
    protected void initGame() {
        FXGL.getInput().clearAll(); // ✅ ล้าง Input ที่มีอยู่ก่อนเริ่มเกมใหม่

        // สร้างพื้นหลัง
        FXGL.getGameWorld().addEntity(Background.createBackground());
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        // ส่งรูปไปในanimation
        Player player = new Player("playerimage.png");
        // สร้างผู้เล่น
        Entity playerEntity = player.createPlayer(200, 0, 2, 1);
        // สร้างstats
        stats = player.getStats(); // ดึง Stats จาก Player
        FXGL.set("playerStats", stats);
        FXGL.getInput().clearAll(); // ✅ ล้าง Input ที่มีอยู่ก่อนเริ่มเกมใหม่

        // สร้าง SkillSystem ที่เชื่อมกับ Player
        skillSystem = new SkillSystem(player);
        FXGL.set("skillSystem", skillSystem);

        // สร้างกำแพง
        Entity wall = Wall.createWall(1.33, 596.00, 766.67, 137.33);
        FXGL.getGameWorld().addEntity(wall);

        uiManager = new UIManager(); // สร้าง UIManager ที่เชื่อมกับ Stats
        FXGL.set("uiManager", uiManager); // ตั้งค่า uiManager ใน FXGL context

        uiManager.initUI(); // เรียกใช้งานการสร้าง UI

        playerEntity.getComponent(PhysicsComponent.class).setVelocityX(0);
        playerEntity.getComponent(PhysicsComponent.class).setVelocityY(0);

        // Spawn a monster every 5 seconds
        // สร้างมอนสเตอร์
        getGameWorld().addEntityFactory(new MonsterFactory());
        FXGL.getGameTimer().runAtInterval(() -> {
            double x = FXGLMath.random(0, getAppWidth() - 64); // Random x position
            double y = FXGLMath.random(0, getAppHeight() - 64); // Random y position
            spawn("monster", x, y);
        }, Duration.seconds(2));
    }

    @Override
    protected void initGameVars(java.util.Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
    }

    protected FXGLMenu getMainMenu() {

        return new StartScreen();
    }

}
