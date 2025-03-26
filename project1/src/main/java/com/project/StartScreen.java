package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FXGLButton;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.File;
import javafx.util.Duration;
import com.almasb.fxgl.entity.Entity;

public class StartScreen extends FXGLMenu {

    public StartScreen() {
        super(MenuType.MAIN_MENU);

        // สร้างพื้นหลัง
        ImageView bg = new ImageView(FXGL.image("wallpaper.png"));
        bg.setFitWidth(FXGL.getAppWidth());
        bg.setFitHeight(FXGL.getAppHeight());

        // สร้างหัวข้อเกม
        Label title = new Label("The Last Adventurer");
        title.setStyle("-fx-font-size: 48px; -fx-text-fill: white; -fx-font-weight: bold;");

        // ปุ่มเริ่มเกม
        FXGLButton btnStart = createStyledButton("New Game");
        btnStart.setOnAction(e -> fireNewGame());

        // ปุ่ม Load Game
        FXGLButton btnLoad = createStyledButton("Load Game");
        if (!new File(SaveLoadManager.SAVE_FILE).exists()) {
            btnLoad.setDisable(true);
            btnLoad.setStyle(
                    "-fx-font-size: 24px; -fx-background-color: gray; -fx-text-fill: white; -fx-background-radius: 30;");
        } else {
            btnLoad.setOnAction(e -> {
                SaveData data = SaveLoadManager.loadGame();
                if (data != null) {
                    loadGameFromSave(data);
                }
            });

        }

        // ปุ่มออกจากเกม
        FXGLButton btnExit = createStyledButton("Exit");
        btnExit.setOnAction(e -> fireExit());

        // จัดวาง UI
        VBox menuBox = new VBox(20, btnStart, btnLoad, btnExit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(500);
        menuBox.setTranslateY(300);

        // เพิ่ม UI ลงใน Scene Graph
        getContentRoot().getChildren().addAll(bg, menuBox);
    }

    private FXGLButton createStyledButton(String text) {
        FXGLButton button = new FXGLButton(text);
        button.setPrefSize(300, 70);
        button.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: linear-gradient(to right,#fef4b7,#efc12d); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 30;");

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: linear-gradient(to right,#ecd382,#d0a10b); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 30;"));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: linear-gradient(to right,#fef4b7,#efc12d); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 30;"));

        return button;
    }

    private void loadGameFromSave(SaveData data) {
        FXGL.getGameController().startNewGame();

        FXGL.runOnce(() -> {
            // Remove existing player entities to avoid duplication
            FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(Entity::removeFromWorld);

            Player player = new Player("playerimage.png");
            Entity newPlayer = player.createPlayer(data.getHealth(), data.getAttack(), data.getLevel(),
                    data.getExperience());
            newPlayer.setPosition(data.getPosX(), data.getPosY());

            Stats stats = newPlayer.getComponent(Stats.class);
            stats.setHealth(data.getHealth());
            stats.setMaxHealth(data.getMaxHealth());
            stats.setAttack(data.getAttack());
            stats.setLevel(data.getLevel());
            stats.setExperience(data.getExperience());

            FXGL.set("playerStats", stats);
            FXGL.<UIManager>geto("uiManager").reloadStats();

            SkillSystem skillSystem = FXGL.geto("skillSystem");
            skillSystem.setPlayer(player);

            for (Point2D pos : data.getEnemyPositions()) {
                FXGL.spawn("monster", pos.getX(), pos.getY());
            }

            // Rebind the camera to the new player entity
            FXGL.getGameScene().getViewport().bindToEntity(newPlayer, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
            FXGL.getGameScene().getViewport().setBounds(-200, -200, 1500, 900); // Adjust the bounds as needed
            FXGL.getGameScene().getViewport().setZoom(1); // ซูมเข้าหน้าจอ


            FXGL.getNotificationService().pushNotification("Game Loaded!");
            ((App) FXGL.getApp()).reloadStatsAfterLoad();
        }, Duration.seconds(0.5));
    }
}
