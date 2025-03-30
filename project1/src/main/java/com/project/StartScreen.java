package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;

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
        Button btnStart = createStyledButton("New Game");
        btnStart.setOnAction(e -> fireNewGame());

        // ปุ่ม Load Game
        Button btnLoad = createStyledButton("Load Game");
        if (!new File(SaveLoadManager.SAVE_FILE).exists()) {
            btnLoad.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            btnLoad.setDisable(true);
            btnLoad.getStyleClass().add("button-disabled");
        } else {
            btnLoad.setOnAction(e -> {
                SaveData data = SaveLoadManager.loadGame();
                if (data != null) {
                    loadGameFromSave(data);
                }
            });

        }

        // ปุ่มออกจากเกม
        Button btnExit = createStyledButton("Exit");
        btnExit.setOnAction(e -> fireExit());

        // จัดวาง UI
        VBox menuBox = new VBox(20, btnStart, btnLoad, btnExit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(500);
        menuBox.setTranslateY(300);

        // เพิ่ม UI ลงใน Scene Graph
        getContentRoot().getChildren().addAll(bg, menuBox);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(300, 70);
        button.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // ใช้คลาส CSS
        button.getStyleClass().add("button-large");

        // เพิ่มเอฟเฟกต์ hover
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

    private void loadGameFromSave(SaveData data) {
        FXGL.getGameController().startNewGame();

        FXGL.runOnce(() -> {
            GameLoader.loadPlayerFromSave(data);
        }, Duration.seconds(0.5));
    }
}
