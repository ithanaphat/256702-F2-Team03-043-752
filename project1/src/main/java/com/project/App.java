package com.project;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
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
        Entity wall = Wall.createWall(1.33, 596.00,766.67,137.33);
        FXGL.getGameWorld().addEntity(wall);
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