package com.project;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.core.math.FXGLMath;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class App extends GameApplication {
    private Stats stats;
    private UIManager uiManager;
    private SkillSystem skillSystem;
    private boolean bossSpawned = false;
    private double spawnTimer = 0; // ตัวจับเวลา

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
                .bbox(BoundingShape.box(20, getAppHeight())) // กำแพงด้านซ้าย
                .with(new PhysicsComponent())
                .buildAndAttach();

        entityBuilder()
                .at(getAppWidth() + 10, 0)
                .bbox(BoundingShape.box(20, getAppHeight())) // กำแพงด้านขวา
                .with(new PhysicsComponent())
                .buildAndAttach();
        onCollisionBegin(EntityType.MONSTER, EntityType.PLAYER, (monster, player) -> {
            int monsterDamage = monster.getInt("damage");
            stats.damage(monsterDamage);

            uiManager.updateHealthDisplay(); // ✅ อัปเดต Health Bar

        });

        onCollisionBegin(EntityType.BOSS, EntityType.PLAYER, (boss, player) -> {
            int monsterDamage = boss.getInt("damage");
            stats.damage(monsterDamage);

            uiManager.updateHealthDisplay(); // ✅ อัปเดต Health Bar

            BossAnimation bossAnimation = boss.getComponent(BossAnimation.class);
            bossAnimation.stun(2); // Stun the boss for 6 seconds
        });

    }

    @Override
    protected void initInput() {

        FXGL.getInput().clearAll();

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.right = true;
                anim.updateMovement();
            }

            @Override
            protected void onActionEnd() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.right = false;
                anim.updateMovement();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.left = true;
                anim.updateMovement();
            }

            @Override
            protected void onActionEnd() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.left = false;
                anim.updateMovement();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.up = true;
                anim.updateMovement();
            }

            @Override
            protected void onActionEnd() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.up = false;
                anim.updateMovement();
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.down = true;
                anim.updateMovement();
            }

            @Override
            protected void onActionEnd() {
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.down = false;
                anim.updateMovement();
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                // ดึงตำแหน่งของผู้เล่น
                Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
                Animation anim = player.getComponent(Animation.class);
                anim.attack(); // เรียกใช้เมธอด attack

                // หา Monster ที่ใกล้ที่สุด
                Entity nearestMonster = getGameWorld().getEntitiesByType(EntityType.MONSTER).stream()
                        .min((m1, m2) -> {
                            double d1 = m1.getPosition().distance(player.getPosition());
                            double d2 = m2.getPosition().distance(player.getPosition());
                            return Double.compare(d1, d2);
                        })
                        .orElse(null);

                // หา Boss ที่ใกล้ที่สุด
                Entity nearestBoss = getGameWorld().getEntitiesByType(EntityType.BOSS).stream()
                        .min((b1, b2) -> {
                            double d1 = b1.getPosition().distance(player.getPosition());
                            double d2 = b2.getPosition().distance(player.getPosition());
                            return Double.compare(d1, d2);
                        })
                        .orElse(null);

                // ถ้าเจอมอนสเตอร์ที่อยู่ในระยะ ให้ลดพลังชีวิต
                if (nearestMonster != null && player.getPosition().distance(nearestMonster.getPosition()) < 80) {
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
                        int expReward = nearestMonster.getInt("expReward");
                        playerStats.addExperience(expReward);

                        // อัปเดต UI
                        UIManager uiManager = FXGL.geto("uiManager");
                        uiManager.updateHealthDisplay();
                    }
                }

                // ถ้าเจอบอสที่อยู่ในระยะ ให้ลดพลังชีวิต
                if (nearestBoss != null && player.getPosition().distance(nearestBoss.getPosition()) < 80) {
                    Health health = nearestBoss.getComponent(Health.class);
                    int attackPower = stats.getAttack(); // ดึงค่าพลังโจมตีของผู้เล่น
                    health.damage(attackPower); // ✅ ลดพลังชีวิตของบอส

                    // ถ้าพลังชีวิตหมด ให้ลบบอสออกจากเกม

                    if (health.getHealth() <= 0) {

                        nearestBoss.removeFromWorld();

                        // ซ่อนแถบเลือดบอส
                        UIManager uiManager = FXGL.geto("uiManager");
                        uiManager.hideBossUI();

                        // เพิ่มค่า experience ให้กับผู้เล่น
                        Stats playerStats = FXGL.geto("playerStats");
                        int expReward = nearestBoss.getInt("expReward");
                        playerStats.addExperience(expReward);

                        // แสดงหน้าจอชัยชนะ
                        FXGL.getSceneService().pushSubScene(new VictoryScreen());
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

        // ล้างสถานะของเกม
        bossSpawned = false;

        // ใช้ FXGL.runOnce เพื่อหยุดเพลงทั้งหมดหลังจากการลบเอนทิตีเสร็จสิ้น
        FXGL.runOnce(() -> {
            FXGL.getAudioPlayer().stopAllMusic();

            // เล่นเพลงพื้นหลังเริ่มต้น
            FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("background.mp3"));
            FXGL.getSettings().setGlobalMusicVolume(0.5); // ตั้งค่าเสียงเพลงเริ่มต้น
        }, Duration.seconds(0.1));

        FXGL.getInput().clearAll(); // ✅ ล้าง Input ที่มีอยู่ก่อนเริ่มเกมใหม่

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        // สร้างพื้นหลัง
        FXGL.getGameScene().setBackgroundRepeat("water.png");
        FXGL.getGameWorld().addEntityFactory(new Fatory());
        FXGL.setLevelFromMap("maps.tmx");

        // ส่งรูปไปในanimation
        Player player = new Player("playerimage.png");
        // สร้างผู้เล่น
        Entity playerEntity = player.createPlayer(200, 0, 2, 1);
        // สร้างstats
        stats = player.getStats(); // ดึง Stats จาก Player
        FXGL.set("playerStats", stats);
        // ผูกกล้องกับเอนทิตีของผู้เล่น
        FXGL.getGameScene().getViewport().bindToEntity(playerEntity, getAppWidth() / 2.0, getAppHeight() / 2.0);
        FXGL.getGameScene().getViewport().setBounds(-200, -200, 1500, 900); // กำหนดขอบเขตกล้องตามขนาดแผนที่
        FXGL.getGameScene().getViewport().setZoom(1); // ซูมเข้าหน้าจอ

        FXGL.getInput().clearAll(); // ✅ ล้าง Input ที่มีอยู่ก่อนเริ่มเกมใหม่

        // สร้าง SkillSystem ที่เชื่อมกับ Player
        skillSystem = new SkillSystem(player);
        FXGL.set("skillSystem", skillSystem);

        uiManager = new UIManager(); // สร้าง UIManager ที่เชื่อมกับ Stats
        FXGL.set("uiManager", uiManager); // ตั้งค่า uiManager ใน FXGL context

        uiManager.initUI(); // เรียกใช้งานการสร้าง UI

        playerEntity.getComponent(PhysicsComponent.class).setVelocityX(0);
        playerEntity.getComponent(PhysicsComponent.class).setVelocityY(0);

        getGameWorld().addEntityFactory(new MonsterFactory());

      

    }

    @Override
    protected void initGameVars(java.util.Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);

        // เพิ่มเวลาในตัวจับเวลา
        spawnTimer += tpf;

        // ถ้าผ่านไป 3 วินาที ให้เรียกมอนสเตอร์
        if (spawnTimer >= 3) {
            for (int i = 0; i < 2; i++) {
                double x = FXGLMath.random(50, getAppWidth() - 50);
                double y = FXGLMath.random(50, getAppHeight() - 50);
                spawn("monster", x, y);
            }
            spawnTimer = 0; // รีเซ็ตตัวจับเวลา
        }

        // ตรวจสอบว่า HP ของผู้เล่นหมดหรือไม่
        if (stats.getHealth() <= 0) {
            FXGL.getSceneService().pushSubScene(new DeathScreen());
            return;
        }

        // ตรวจสอบเลเวลของผู้เล่น
        if (!bossSpawned && stats.getLevel() >= 30) {
            bossSpawned = true;

            // เปลี่ยนเพลงเมื่อถึงเลเวล 30
            FXGL.getAudioPlayer().stopAllMusic(); // หยุดเพลงเก่า
            FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("background_boss.mp3")); // เล่นเพลงใหม่

            // เรียกบอสออกมา
            Entity boss = spawn("boss", 700, 150);

            // อัปเดต UI เลือดบอส
            Health bossHealth = boss.getComponent(Health.class);
            UIManager uiManager = FXGL.geto("uiManager");
            uiManager.updateBossUI(bossHealth.getHealth(), 500); // 500 คือ Max HP ของบอส
        }

        // อัปเดต UI เลือดบอสระหว่างเกม
        Entity boss = getGameWorld().getEntitiesByType(EntityType.BOSS).stream().findFirst().orElse(null);
        if (boss != null) {
            Health bossHealth = boss.getComponent(Health.class);
            UIManager uiManager = FXGL.geto("uiManager");
            uiManager.updateBossUI(bossHealth.getHealth(), 500); // 200 คือ Max HP ของบอส
        } else {
            // ซ่อน UI หากไม่มีบอส
            UIManager uiManager = FXGL.geto("uiManager");
            uiManager.hideBossUI();
        }
    }

    protected FXGLMenu getMainMenu() {

        return new StartScreen();
    }

    public void reloadStatsAfterLoad() {
        this.stats = FXGL.geto("playerStats");
    }

    public void setBossSpawned(boolean bossSpawned) {
        this.bossSpawned = bossSpawned;
    }

}
