package com.project;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.entity.level.tiled.TiledMap;
import com.almasb.fxgl.entity.level.tiled.TilesetLoader;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;


public class App extends GameApplication {
    private Entity player; // ตัวละครหลัก
    private Stats stats;
    private UIManager uiManager; // สร้างตัวแปร UIManager

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
    protected void initGame() {
        FXGL.getGameWorld().addEntity(Background.createBackground());
        /*Level level = FXGL.getAssetLoader().loadLevel("assets/textures/map.tmx", new TMXLevelLoader());
        FXGL.getGameWorld().setLevel(level);*/
        
        // ✅ ใช้ AnimationComponent เพื่อให้ตัวละครมีอนิเมชัน
        player = FXGL.entityBuilder()
                .at(300, 300)
                .viewWithBBox(new Rectangle(64, 64, Color.BLUE))
                .with(new AnimationComponent()) // 🎥 ใส่อนิเมชันที่ย้ายมาจาก SpriteSheetAnimationApp
                .buildAndAttach();

        // สร้าง Stats และ UIManager
        stats = new Stats(100, 0,100,1);
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