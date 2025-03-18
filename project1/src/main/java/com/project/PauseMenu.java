package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PauseMenu extends FXGLMenu {
    public PauseMenu() {
        super(MenuType.GAME_MENU);

        // พื้นหลังสีดำโปร่งแสง
        var bg = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.color(0, 0, 0, 0.7));
        getContentRoot().getChildren().add(bg);

        // ปุ่ม Resume (เล่นเกมต่อ)
        Button btnResume = new Button("Resume");
        btnResume.setOnAction(e -> FXGL.getGameController().gotoPlay());

        // ปุ่ม Save Game
        Button btnSave = new Button("Save Game");
        btnSave.setOnAction(e -> {
            Entity player = FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
            Stats stats = player.getComponent(Stats.class);
            SaveLoadManager.saveGame(stats, player.getX(), player.getY());
            FXGL.getNotificationService().pushNotification("Game Saved!");
        });

        // ปุ่ม Load Game
        Button btnLoad = new Button("Load Game");
        btnLoad.setOnAction(e -> {
            SaveData data = SaveLoadManager.loadGame();
            if (data != null) {
                loadPlayerFromSave(data);
                FXGL.getNotificationService().pushNotification("Game Loaded!");
            }
        });

        // ปุ่ม Back to Main Menu (กลับไปหน้าเมนูหลัก)
        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setOnAction(e -> FXGL.getGameController().gotoMainMenu());

        // ปุ่ม Quit (ออกจากเกม)
        Button btnQuit = new Button("Quit");
        btnQuit.setOnAction(e -> FXGL.getGameController().exit());

        // จัดเรียงปุ่มในแนวตั้ง
        VBox menuBox = new VBox(15, btnResume, btnSave, btnLoad, btnMainMenu, btnQuit);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2 - 50);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2 - 100);

        getContentRoot().getChildren().add(menuBox);
    }

    // โหลดข้อมูลจากไฟล์เซฟ
    private void loadPlayerFromSave(SaveData data) {
        if (data == null) {
            FXGL.getNotificationService().pushNotification("No Save Data Found!");
            return;
        }

        // ลบตัวละครเก่าทิ้ง
        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(Entity::removeFromWorld);

        // สร้างตัวละครใหม่จากข้อมูลเซฟ
        Player player = new Player("playerimage.png");
        Entity newPlayer = player.createPlayer(data.getHealth(), 0, data.getAttack(), data.getLevel());

        // กำหนดตำแหน่งใหม่
        newPlayer.setPosition(data.getPosX(), data.getPosY());

        // กำหนดค่า Stats ที่โหลดมา
        Stats stats = newPlayer.getComponent(Stats.class);
        stats.setExperience(data.getExperience());

        FXGL.getNotificationService().pushNotification("Game Loaded!");
    }

}
