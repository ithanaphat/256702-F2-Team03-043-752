package com.project;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;


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

        // ✅ ใช้ AnimationComponent เพื่อให้ตัวละครมีอนิเมชัน
        player = FXGL.entityBuilder()
                .at(300, 300)
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