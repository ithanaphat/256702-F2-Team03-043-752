package com.project;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIManager {
    private Stats stats;
    private Rectangle healthBar; // ✅ ตัวแปรเก็บ Health Bar
    private Text healthText; // ✅ ข้อความแสดงค่า HP
    private Text levelText; // ✅ ข้อความแสดงค่า Level
    private Text attackText; // ✅ ข้อความแสดงค่าพลังโจมตี
    private Rectangle levelBar; // ✅ ตัวแปรเก็บ Level Bar

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

        // ✅ สร้างข้อความ "Level:"
        levelText = new Text();
        levelText.setTranslateX(30);
        levelText.setTranslateY(60);
        levelText.setFill(Color.WHITE);
        levelText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        levelText.setText("Level: " + stats.getLevel());

         // ✅ สร้างพื้นหลังของ Level Bar
         Rectangle levelBarBackground = new Rectangle(200, 5);
         levelBarBackground.setTranslateX(60);
         levelBarBackground.setTranslateY(45);
         levelBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Level Bar สีฟ้า
        levelBar = new Rectangle(200, 5);
        levelBar.setTranslateX(60);
        levelBar.setTranslateY(45);
        levelBar.setFill(Color.BLUE);

        // ✅ สร้างข้อความ "Attack:"
        attackText = new Text();
        attackText.setTranslateX(30);
        attackText.setTranslateY(90);
        attackText.setFill(Color.WHITE);
        attackText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        attackText.setText("Attack: " + stats.getAttack());


        // ✅ เพิ่ม UI เข้าไปในเกม
        FXGL.getGameScene().addUINode(textLabel);
        FXGL.getGameScene().addUINode(healthBarBackground);
        FXGL.getGameScene().addUINode(healthBar);
        FXGL.getGameScene().addUINode(healthText);
        FXGL.getGameScene().addUINode(levelText);
        FXGL.getGameScene().addUINode(levelBar);
        FXGL.getGameScene().addUINode(attackText);

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

        // อัปเดตค่า Level ที่แสดงผล
        levelText.setText("Level: " + stats.getLevel());

        // อัปเดตค่าพลังโจมตีที่แสดงผล
        attackText.setText("Attack: " + stats.getAttack());

        // คำนวณความกว้างของ Level Bar
        double levelPercentage = (double) stats.getExperience() / stats.getExperienceForNextLevel();
        levelBar.setWidth(200 * levelPercentage);
    }

    
}
