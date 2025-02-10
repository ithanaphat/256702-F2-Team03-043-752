package com.project;

import java.util.Map;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.text.Text;

public class App extends GameApplication {
    private Entity player; // ตัวละครหลัก

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

               
    }

    @Override
    protected void initGameVars(java.util.Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void initUI() {
        Text textLabel = new Text("Moved: ");
        Text textPixels = new Text();

        textLabel.setTranslateX(30);
        textLabel.setTranslateY(50);
        textPixels.setTranslateX(80);
        textPixels.setTranslateY(50);

        textPixels.textProperty().bind(
                FXGL.getWorldProperties().intProperty("pixelsMoved").asString()
        );

        FXGL.getGameScene().addUINode(textLabel);
        FXGL.getGameScene().addUINode(textPixels);
    }
}