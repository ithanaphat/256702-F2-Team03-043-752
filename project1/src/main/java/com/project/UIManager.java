package com.project;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIManager {
    private Stats stats;
    private Rectangle healthBar; // ✅ ตัวแปรเก็บ Health Bar
    private Rectangle manaBar;   // ✅ Mana Bar (สีฟ้า)
    private Rectangle levelBar; // ✅ ตัวแปรเก็บ Level Bar
    private Text healthText; // ✅ ข้อความแสดงค่า HP
    private Text manaText;       // ✅ ข้อความแสดงค่า MANA
    private Text levelText; // ✅ ข้อความแสดงค่า Level
    private Text attackText; // ✅ ข้อความแสดงค่าพลังโจมต

    public UIManager(Stats stats) {
        this.stats = stats;
    }

    public void initUI() {
        // ✅ สร้างข้อความ "HP:"
        Text hpLabel = new Text("HP: ");
        hpLabel.setTranslateX(65);
        hpLabel.setTranslateY(30);
        hpLabel.setFill(Color.WHITE);
        hpLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Health Bar
        Rectangle healthBarBackground = new Rectangle(200, 20);
        healthBarBackground.setTranslateX(100);
        healthBarBackground.setTranslateY(15);
        healthBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Health Bar สีแดง
        healthBar = new Rectangle(200, 20);
        healthBar.setTranslateX(100);
        healthBar.setTranslateY(15);
        healthBar.setFill(Color.RED);

        // ✅ แสดงค่า HP เป็นตัวเลข
        healthText = new Text();
        healthText.setTranslateX(320);
        healthText.setTranslateY(30);
        healthText.setFill(Color.WHITE);
        healthText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างข้อความ "MANA:"
        Text manaLabel = new Text("MANA: ");
        manaLabel.setTranslateX(30);
        manaLabel.setTranslateY(60);
        manaLabel.setFill(Color.WHITE);
        manaLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Mana Bar
        Rectangle manaBarBackground = new Rectangle(200, 20);
        manaBarBackground.setTranslateX(100);
        manaBarBackground.setTranslateY(45);
        manaBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Mana Bar สีฟ้า
        manaBar = new Rectangle(200, 20);
        manaBar.setTranslateX(100);
        manaBar.setTranslateY(45);
        manaBar.setFill(Color.CYAN);

        // ✅ แสดงค่า MANA เป็นตัวเลข
        manaText = new Text();
        manaText.setTranslateX(320);
        manaText.setTranslateY(60);
        manaText.setFill(Color.WHITE);
        manaText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างข้อความ "Level:"
        levelText = new Text();
        levelText.setTranslateX(30);
        levelText.setTranslateY(90);
        levelText.setFill(Color.WHITE);
        levelText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Level Bar
        Rectangle levelBarBackground = new Rectangle(200, 5);
        levelBarBackground.setTranslateX(60);
        levelBarBackground.setTranslateY(75);
        levelBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Level Bar สีน้ำเงิน
        levelBar = new Rectangle(200, 5);
        levelBar.setTranslateX(60);
        levelBar.setTranslateY(75);
        levelBar.setFill(Color.BLUE);

        // ✅ สร้างข้อความ "Attack:"
        attackText = new Text();
        attackText.setTranslateX(30);
        attackText.setTranslateY(110);
        attackText.setFill(Color.WHITE);
        attackText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ เพิ่ม UI เข้าไปในเกม
        FXGL.getGameScene().addUINode(hpLabel);
        FXGL.getGameScene().addUINode(healthBarBackground);
        FXGL.getGameScene().addUINode(healthBar);
        FXGL.getGameScene().addUINode(healthText);
        FXGL.getGameScene().addUINode(manaLabel);
        FXGL.getGameScene().addUINode(manaBarBackground);
        FXGL.getGameScene().addUINode(manaBar);
        FXGL.getGameScene().addUINode(manaText);
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
        int mana = stats.getMana();
        int maxMana = stats.getMaxMana();

        // คำนวณความกว้างของ Health Bar
        double healthPercentage = (double) health / maxHealth;
        healthBar.setWidth(200 * healthPercentage);

        // อัปเดตความกว้างของ Mana Bar
        double manaPercentage = (double) mana / maxMana;
        manaBar.setWidth(200 * manaPercentage);
        manaText.setText(mana + " / " + maxMana);

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
