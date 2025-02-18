package com.project;

import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;


public class App extends GameApplication {
    private Stats stats;
    private UIManager uiManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("The Last Adventurer");
        settings.setVersion("0.1");
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physicsWorld = getPhysicsWorld();
        physicsWorld.setGravity(0, 0);

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
        player.createPlayer(50,50);
        //สร้างกำแพง
        Entity wall1 = Wall.createWall(649.60, 10.22,309.47,147.44);
        Entity wall2 = Wall.createWall(0, 595.26,780.31,154.74);
        Entity wall3 = Wall.createWall(970.75, 13.14,145.98,464.21);
        FXGL.getGameWorld().addEntity(wall1);
        FXGL.getGameWorld().addEntity(wall2);
        FXGL.getGameWorld().addEntity(wall3);
        //สร้างstats
        stats = new Stats(100, 0, 100, 1);
        uiManager = new UIManager(stats); // สร้าง UIManager ที่เชื่อมกับ Stats
        uiManager.initUI(); // เรียกใช้งานการสร้าง UI


    }

    @Override
    protected void initGameVars(java.util.Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);

        // อัปเดตพลังชีวิตและคะแนนใน World Properties
        FXGL.getWorldProperties().setValue("health", stats.getHealth());
        FXGL.getWorldProperties().setValue("score", stats.getScore());
    }
}