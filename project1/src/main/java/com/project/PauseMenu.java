package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Pos;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



import java.util.List;
import java.util.stream.Collectors;

public class PauseMenu extends FXGLMenu {
    public PauseMenu() {
        super(MenuType.GAME_MENU);

        // พื้นหลังสีดำโปร่งแสง
        var bg = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.color(0, 0, 0, 0.7));
        getContentRoot().getChildren().add(bg);

        // ปุ่ม Resume (เล่นเกมต่อ)
        Button btnResume = createStyledButton("Resume");
        btnResume.setOnAction(e -> FXGL.getGameController().gotoPlay());

        // ปุ่ม Save Game
        Button btnSave = createStyledButton("Save Game");
        btnSave.setOnAction(e -> {
            Entity player = FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
            Stats stats = player.getComponent(Stats.class);
            List<Point2D> enemyPositions = FXGL.getGameWorld().getEntitiesByType(EntityType.MONSTER)
                    .stream()
                    .map(Entity::getPosition)
                    .collect(Collectors.toList());
            
            // Assuming you have a way to get the boss entity and its stats
            Entity boss = FXGL.getGameWorld().getEntitiesByType(EntityType.BOSS).stream().findFirst().orElse(null);
            


            SaveLoadManager.saveGame(stats, player.getX(), player.getY(), enemyPositions, boss);
            FXGL.getNotificationService().pushNotification("Game Saved!");
        });

        // ปุ่ม Load Game
        Button btnLoad = createStyledButton("Load Game");
        btnLoad.setOnAction(e -> {
            SaveData data = SaveLoadManager.loadGame();
            if (data != null) {
                loadPlayerFromSave(data);
                FXGL.getNotificationService().pushNotification("Game Loaded!");
            }
        });

        // ปุ่ม Back to Main Menu (กลับไปหน้าเมนูหลัก)
        Button btnMainMenu = createStyledButton("Main Menu");
        btnMainMenu.setOnAction(e -> FXGL.getGameController().gotoMainMenu());

        // ปุ่ม Quit (ออกจากเกม)
        Button btnQuit = createStyledButton("Quit");
        btnQuit.setOnAction(e -> FXGL.getGameController().exit());

        // จัดเรียงปุ่มในแนวตั้ง
        VBox menuBox = new VBox(15, btnResume, btnSave, btnLoad, btnMainMenu, btnQuit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2 - 125);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2 - 150);

        getContentRoot().getChildren().add(menuBox);
    }

    // ✅ ฟังก์ชันสร้างปุ่มที่มีสไตล์
    private Button createStyledButton(String text) {

        Button button = new Button(text);
        button.setPrefSize(250, 60);
        button.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());


        // ใช้คลาส CSS
        button.getStyleClass().add("button-large");

        // เอฟเฟกต์ Hover
        button.setOnMouseEntered(e -> {
            button.getStyleClass().remove("button-large");
            button.getStyleClass().add("button-large-hover");
        });

        button.setOnMouseExited(e -> {
            button.getStyleClass().remove("button-large-hover");
            button.getStyleClass().add("button-large");
        });

        return button;
    }

    private void loadPlayerFromSave(SaveData data) {
        // โหลดข้อมูลผู้เล่นจาก SaveData
        GameLoader.loadPlayerFromSave(data);
    
      
    
        
    }

    

}
