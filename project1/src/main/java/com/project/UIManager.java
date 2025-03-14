package com.project;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIManager {
    private Stats stats;
    private Rectangle healthBar; // ✅ ตัวแปรเก็บ Health Bar
    private Rectangle levelBar; // ✅ ตัวแปรเก็บ Level Bar
    private Text healthText; // ✅ ข้อความแสดงค่า HP
    private Text levelText; // ✅ ข้อความแสดงค่า Level
    private Text attackText; // ✅ ข้อความแสดงค่าพลังโจมต

    public UIManager() {
        this.stats = FXGL.geto("playerStats");

        // เพิ่มการตั้งค่า UIManager ให้ฟังการอัปเดตจาก Stats
        stats.experienceProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Exp Updated in UIManager: " + newVal);
            updateHealthDisplay(); // อัปเดต UI ทันทีเมื่อค่าประสบการณ์เปลี่ยน
        });
    }

    public void initUI() {
        // ✅ สร้างข้อความ "HP:"
        Text hpLabel = new Text("HP: ");
        hpLabel.setTranslateX(30);
        hpLabel.setTranslateY(30);
        hpLabel.setFill(Color.WHITE);
        hpLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Health Bar
        Rectangle healthBarBackground = new Rectangle(200, 20);
        healthBarBackground.setTranslateX(120);
        healthBarBackground.setTranslateY(15);
        healthBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Health Bar สีแดง
        healthBar = new Rectangle(200, 20);
        healthBar.setTranslateX(120);
        healthBar.setTranslateY(15);
        healthBar.setFill(Color.RED);

        // ✅ แสดงค่า HP เป็นตัวเลข
        healthText = new Text();
        healthText.setTranslateX(330);
        healthText.setTranslateY(30);
        healthText.setFill(Color.WHITE);
        healthText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างข้อความ "Level:"
        levelText = new Text();
        levelText.setTranslateX(30);
        levelText.setTranslateY(60);
        levelText.setFill(Color.WHITE);
        levelText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Level Bar
        Rectangle levelBarBackground = new Rectangle(200, 5);
        levelBarBackground.setTranslateX(120);
        levelBarBackground.setTranslateY(50);
        levelBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Level Bar สีน้ำเงิน
        levelBar = new Rectangle(200, 5);
        levelBar.setTranslateX(120);
        levelBar.setTranslateY(50);
        levelBar.setFill(Color.BLUE);

        // ✅ สร้างข้อความ "Attack:"
        attackText = new Text();
        attackText.setTranslateX(30);
        attackText.setTranslateY(90);
        attackText.setFill(Color.WHITE);
        attackText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ เพิ่ม UI เข้าไปในเกม
        FXGL.getGameScene().addUINode(hpLabel);
        FXGL.getGameScene().addUINode(healthBarBackground);
        FXGL.getGameScene().addUINode(healthBar);
        FXGL.getGameScene().addUINode(healthText);
        FXGL.getGameScene().addUINode(levelText);
        FXGL.getGameScene().addUINode(levelBarBackground);
        FXGL.getGameScene().addUINode(levelBar);
        FXGL.getGameScene().addUINode(attackText);

        // ✅ อัปเดตค่าเริ่มต้น
        updateHealthDisplay();
    }

    // ✅ ฟังก์ชันอัปเดต Health Bar
    public void updateHealthDisplay() {
        Stats stats = FXGL.geto("playerStats");
        System.out.println("Stats in UIManager: " + stats.hashCode()); // ✅ ตรวจสอบ ID ของ Stats
        System.out.println("Exp in UIManager before update1: " + stats.getExperience());
        levelText.setText("Level: " + stats.getLevel());

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
        System.out.println("Exp in UIManager during update: " + stats.getExperience());
        double levelPercentage = (double) stats.getExperience() / stats.getExperienceForNextLevel();
        levelBar.setWidth(200 * levelPercentage);
    }
}
