package com.project;

import java.util.Map;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.text.Text;

public class App extends GameApplication {
    private Player player;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1600);
        settings.setHeight(800);
        settings.setTitle("The Last Adventurer");
        settings.setVersion("0.1");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntity(Background.createBackground());
        player = new Player(300, 300); // สร้างตัวละคร
        player.registerControls(); // ลงทะเบียนปุ่มกด
       
    }

    @Override
    protected void initInput() {
       
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
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
