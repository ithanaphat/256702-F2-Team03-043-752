package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FXGLButton;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.File;
import javafx.util.Duration;


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
            GameLoader.loadPlayerFromSave(data);
        }, Duration.seconds(0.5));
    }
}
