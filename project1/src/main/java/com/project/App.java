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
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

import javafx.scene.input.KeyCode;

public class App extends GameApplication {
    private Stats stats;
    private UIManager uiManager;
    private SkillSystem skillSystem;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("The Last Adventurer");
        settings.setVersion("0.1");

        settings.setMainMenuEnabled(true);
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
                    stats.damage(10); // ✅ ลดพลังชีวิตลง 10
                    uiManager.updateHealthDisplay(); // ✅ อัปเดต Health Bar
                });;
                
    }

    @Override
    protected void initInput() {
    Attack.init(); // ✅ เรียกใช้ระบบโจมตีเมื่อคลิกซ้าย
    
    FXGL.getInput().addAction(new UserAction("Skill Q") {
        @Override
        protected void onActionBegin() {
            skillSystem.activateSkill(KeyCode.Q);
        }
    }, KeyCode.Q);

    FXGL.getInput().addAction(new UserAction("Skill E") {
        @Override
        protected void onActionBegin() {
            skillSystem.activateSkill(KeyCode.E);
        }
    }, KeyCode.E);

    FXGL.getInput().addAction(new UserAction("Skill R") {
        @Override
        protected void onActionBegin() {
            skillSystem.activateSkill(KeyCode.R);
        }
    }, KeyCode.R);
    }   

    @Override
    protected void initGame() {
        //รูปภาพผู้เล่น
        String image = "playerimage.png";

        //ส่งรูปไปในanimation
        Player player = new Player(image);

        //สร้างพื้นหลัง
        FXGL.getGameWorld().addEntity(Background.createBackground());
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        //สร้างผู้เล่นs
        player.createPlayer(200, 0, 100, 1);

        // สร้างมอนสเตอร์
        getGameWorld().addEntityFactory(new MonsterFactory());
        spawn("monster", 100, 100);

        //สร้างกำแพง
        Entity wall = Wall.createWall(1.33, 596.00,766.67,137.33);
        FXGL.getGameWorld().addEntity(wall);

        //สร้างstats
        stats = new Stats(200, 0, 100, 1);

        uiManager = new UIManager(stats); // สร้าง UIManager ที่เชื่อมกับ Stats

        uiManager.initUI(); // เรียกใช้งานการสร้าง UI

        

        skillSystem = new SkillSystem(player); // ✅ สร้าง SkillSystem ที่เชื่อมกับ Player
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