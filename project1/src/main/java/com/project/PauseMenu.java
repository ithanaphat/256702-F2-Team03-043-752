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

import static com.almasb.fxgl.dsl.FXGL.spawn;

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
            SaveLoadManager.saveGame(stats, player.getX(), player.getY(), enemyPositions);
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
        button.setPrefSize(250, 60); // ปรับขนาดปุ่ม
        button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #00c6ff, #0072ff); " + // สีฟ้าไล่สี
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" + // ทำให้ปุ่มโค้งมน
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        );

        // ✅ เอฟเฟกต์ Hover (เปลี่ยนสีปุ่มเมื่อเอาเม้าส์ไปชี้)
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #00e6ff, #0088ff); " + // ฟ้าอ่อนขึ้น
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" +
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #00c6ff, #0072ff); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" +
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        ));

        return button;
    }

    private void loadPlayerFromSave(SaveData data) {
        if (data == null) {
            FXGL.getNotificationService().pushNotification("No Save Data Found!");
            return;
        }

        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(Entity::removeFromWorld);
        FXGL.getGameWorld().getEntitiesByType(EntityType.MONSTER).forEach(Entity::removeFromWorld);

        Player player = new Player("playerimage.png");
        Entity newPlayer = player.createPlayer(data.getHealth(), data.getAttack(), data.getLevel(), data.getExperience());

        newPlayer.setPosition(data.getPosX(), data.getPosY());

        Stats stats = newPlayer.getComponent(Stats.class);
        stats.setHealth(data.getHealth());
        stats.setMaxHealth(data.getMaxHealth());
        stats.setAttack(data.getAttack());
        stats.setLevel(data.getLevel());
        stats.setExperience(data.getExperience());

        FXGL.set("playerStats", stats);

        for (Point2D pos : data.getEnemyPositions()) {
            spawn("monster", pos.getX(), pos.getY());
        }

        FXGL.getNotificationService().pushNotification("Game Loaded!");
        FXGL.<UIManager>geto("uiManager").updateHealthDisplay();
    }
}
