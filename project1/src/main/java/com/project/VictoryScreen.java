package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class VictoryScreen extends FXGLMenu {

    public VictoryScreen() {
        super(MenuType.GAME_MENU);

        // หยุดเพลงพื้นหลัง
        FXGL.getAudioPlayer().stopAllMusic();

        // เล่นเสียงเอฟเฟกต์ตอนชนะ
        FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("victory.mp3"));

        // เพิ่มพื้นหลัง (Background)
        ImageView background = new ImageView(FXGL.getAssetLoader().loadTexture("victory.png").getImage());
        background.setFitWidth(FXGL.getAppWidth());
        background.setFitHeight(FXGL.getAppHeight());
        getContentRoot().getChildren().add(background);

        // ปุ่มกลับไปยังเมนูหลัก
        FXGLButton btnMainMenu = createStyledButton("Main Menu");
        btnMainMenu.setOnAction(e -> FXGL.getGameController().gotoMainMenu());

        // จัดเรียง UI
        VBox vbox = new VBox(20, btnMainMenu);
        vbox.setAlignment(Pos.CENTER);
        vbox.setTranslateX(FXGL.getAppWidth() / 2 - 150);
        vbox.setTranslateY(FXGL.getAppHeight() / 2 - 50);

        getContentRoot().getChildren().add(vbox);
    }

    private FXGLButton createStyledButton(String text) {
        FXGLButton button = new FXGLButton(text);
        button.setPrefSize(300, 70);
        button.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: linear-gradient(to right, #fef4b7, #efc12d); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 30;");

        // เพิ่มเอฟเฟกต์ Hover
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: linear-gradient(to right, #ecd382, #d0a10b); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 30;"));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: linear-gradient(to right, #fef4b7, #efc12d); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 30;"));

        return button;
    }
}