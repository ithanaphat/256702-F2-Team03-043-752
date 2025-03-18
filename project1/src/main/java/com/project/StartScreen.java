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
       ImageView bg = new ImageView(FXGL.image("2.png")); // ใช้รูปภาพ background.png
       bg.setFitWidth(FXGL.getAppWidth());
       bg.setFitHeight(FXGL.getAppHeight());
       bg.setOpacity(1);

       

        // สร้างหัวข้อเกม
        Label title = new Label("The Last Adventurer");
        title.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");

        // ปุ่มเริ่มเกม
        FXGLButton btnStart = new FXGLButton("Start Game");
        System.out.println("Start Game button clicked"); // Debug จุดที่ 3
        btnStart.setOnAction(e -> fireNewGame());

        // ปุ่มออกจากเกม
        FXGLButton btnExit = new FXGLButton("Exit");
        btnExit.setOnAction(e -> fireExit());

        // จัดวาง UI
        VBox menuBox = new VBox(20, title, btnStart, btnExit);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2 - 100);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2 - 50);

        // เพิ่ม UI ลงใน Scene Graph
        getContentRoot().getChildren().addAll(bg, menuBox);
    }
}
