package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DeathScreen extends FXGLMenu {

    public DeathScreen() {
        super(MenuType.GAME_MENU);

        
        // Play death sound
        FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("death_sound.mp3"));

        // Create background
        var bg = new javafx.scene.shape.Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        bg.setFill(javafx.scene.paint.Color.color(0, 0, 0, 0.7));
        getContentRoot().getChildren().add(bg);

        // Create death message
        Label deathMessage = new Label("You Died!");
        deathMessage.setStyle("-fx-font-size: 48px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Create button to go to main menu
        FXGLButton btnMainMenu = createStyledButton("Main Menu");
        btnMainMenu.setOnAction(e -> FXGL.getGameController().gotoMainMenu());

        // Arrange UI elements
        VBox vbox = new VBox(20, deathMessage, btnMainMenu);
        vbox.setAlignment(Pos.CENTER);
        vbox.setTranslateX(FXGL.getAppWidth() / 2 - 150);
        vbox.setTranslateY(FXGL.getAppHeight() / 2 - 100);

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
            "-fx-border-radius: 30;"
        );

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #ecd382, #d0a10b); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" +
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #fef4b7, #efc12d); " +
            "-fx-text-fill: black; " +
            "-fx-background-radius: 30;" +
            "-fx-border-color: black; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        ));

        return button;
    }
}