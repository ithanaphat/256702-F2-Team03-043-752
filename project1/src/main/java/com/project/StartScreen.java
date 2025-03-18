package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StartScreen extends FXGLMenu {

    public StartScreen() {
        super(MenuType.MAIN_MENU);
        System.out.println("StartScreen Loaded"); // ✅ Debug

        // สร้างพื้นหลัง
        ImageView bg = new ImageView(FXGL.image("mapboss.png")); 
        bg.setFitWidth(FXGL.getAppWidth());
        bg.setFitHeight(FXGL.getAppHeight());
        bg.setOpacity(1);

        // สร้างหัวข้อเกม
        Label title = new Label("The Last Adventurer");
        title.setStyle("-fx-font-size: 48px; -fx-text-fill: white; -fx-font-weight: bold;");

        // ปุ่มเริ่มเกม
        FXGLButton btnStart = createStyledButton("New Game");
        btnStart.setOnAction(e -> fireNewGame());

        // ปุ่มออกจากเกม
        FXGLButton btnExit = createStyledButton("Exit");
        btnExit.setOnAction(e -> fireExit());

        // จัดวาง UI
        VBox menuBox = new VBox(20, title, btnStart, btnExit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2 - 150);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2 - 100);

        // เพิ่ม UI ลงใน Scene Graph
        getContentRoot().getChildren().addAll(bg, menuBox);
    }

    // ✅ ฟังก์ชันช่วยสร้างปุ่มที่มีสไตล์
    private FXGLButton createStyledButton(String text) {
        FXGLButton button = new FXGLButton(text);
        button.setPrefSize(250, 60); // ปรับขนาดปุ่ม
        button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #160b9e, #74f52f); " + // สีแดง-ม่วงไล่สี
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" + // ทำให้ปุ่มโค้ง
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        );

        // ✅ เอฟเฟกต์ตอน Hover
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #160b9e, #74f52f); " + // สีส้ม-แดงไล่สี
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" +
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: linear-gradient(to right, #160b9e, #74f52f); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 30;" +
            "-fx-border-color: white; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 30;"
        ));

        return button;
    }
}

