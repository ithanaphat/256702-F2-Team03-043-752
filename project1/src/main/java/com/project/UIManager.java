package com.project;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIManager {
    private Stats stats;
    private Rectangle healthBar; // ✅ ตัวแปรเก็บ Health Bar
    private Text healthText; // ✅ ข้อความแสดงค่า HP

    public UIManager(Stats stats) {
        this.stats = stats;
    }

    public void initUI() {
        // ✅ สร้างข้อความ "HP:"
        Text textLabel = new Text("HP: ");
        textLabel.setTranslateX(30);
        textLabel.setTranslateY(30);
        textLabel.setFill(Color.WHITE);
        textLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Health Bar
        Rectangle healthBarBackground = new Rectangle(200, 20);
        healthBarBackground.setTranslateX(60);
        healthBarBackground.setTranslateY(15);
        healthBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Health Bar สีแดง
        healthBar = new Rectangle(200, 20);
        healthBar.setTranslateX(60);
        healthBar.setTranslateY(15);
        healthBar.setFill(Color.RED);

        // ✅ แสดงค่า HP เป็นตัวเลข
        healthText = new Text();
        healthText.setTranslateX(270);
        healthText.setTranslateY(30);
        healthText.setFill(Color.WHITE);
        healthText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        healthText.setText("100 / 100");

        // ✅ เพิ่ม UI เข้าไปในเกม
        FXGL.getGameScene().addUINode(textLabel);
        FXGL.getGameScene().addUINode(healthBarBackground);
        FXGL.getGameScene().addUINode(healthBar);
        FXGL.getGameScene().addUINode(healthText);

        // ✅ อัปเดตค่าเริ่มต้น
        updateHealthDisplay();
    }

    // ✅ ฟังก์ชันอัปเดต Health Bar
    public void updateHealthDisplay() {
        int health = stats.getHealth();
        int maxHealth = stats.getMaxHealth();  

        // คำนวณความกว้างของ Health Bar
        double healthPercentage = (double) health / maxHealth;
        healthBar.setWidth(200 * healthPercentage);

        // อัปเดตค่า HP ที่แสดงผล
        healthText.setText(health + " / " + maxHealth);
    }
}
