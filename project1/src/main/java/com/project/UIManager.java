package com.project;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIManager {

    private Stats stats;

    // สร้าง UI ที่เกี่ยวข้อง
    public UIManager(Stats stats) {
        this.stats = stats;
    }

    public void initUI() {
        // แสดงจำนวนการเคลื่อนที่ (ลบออกถ้าไม่ต้องการ)
        Text textLabel = new Text("health: ");
        Text textPixels = new Text();
        textLabel.setTranslateX(30);
        textLabel.setTranslateY(30);
        textPixels.setTranslateX(80);
        textPixels.setTranslateY(30);
        textPixels.textProperty().bind(
                FXGL.getWorldProperties().intProperty("pixelsMoved").asString()
        );

        FXGL.getGameScene().addUINode(textLabel);
        FXGL.getGameScene().addUINode(textPixels);

        // แสดงค่าพลังโจมตี
        Text attackLabel = new Text("Attack: ");
        Text attackValue = new Text();
        attackLabel.setTranslateX(30);
        attackLabel.setTranslateY(110);
        attackValue.setTranslateX(80);
        attackValue.setTranslateY(110);
        attackValue.textProperty().bind(stats.attackProperty().asString());

        FXGL.getGameScene().addUINode(attackLabel);
        FXGL.getGameScene().addUINode(attackValue);

        // แสดงค่าคะแนน (score)
        Text scoreLabel = new Text("Score: ");
        Text scoreValue = new Text();
        scoreLabel.setTranslateX(30);
        scoreLabel.setTranslateY(140);
        scoreValue.setTranslateX(80);
        scoreValue.setTranslateY(140);
        scoreValue.textProperty().bind(stats.scoreProperty().asString());

        FXGL.getGameScene().addUINode(scoreLabel);
        FXGL.getGameScene().addUINode(scoreValue);

        // สร้างกล่องสำหรับแสดงหลอดพลังชีวิต
        Rectangle healthBarBackground = new Rectangle(200, 20);  // ขนาดพื้นหลังของหลอด
        healthBarBackground.setTranslateX(30);
        healthBarBackground.setTranslateY(50);
        healthBarBackground.setFill(Color.GRAY);  // สีพื้นหลังของหลอด

        Rectangle healthBar = new Rectangle(200, 20);  // ขนาดของหลอดพลังชีวิต
        healthBar.setTranslateX(30);
        healthBar.setTranslateY(50);
        healthBar.setFill(Color.RED);  // สีของหลอดพลังชีวิต

        // อัปเดตหลอดพลังชีวิต
        updateHealthDisplay(healthBar);

        // เพิ่ม UI หลัก
        FXGL.getGameScene().addUINode(healthBarBackground);
        FXGL.getGameScene().addUINode(healthBar);

        // สร้างกล่องสำหรับแสดง Level Bar
        Rectangle levelBarBackground = new Rectangle(200, 10);  // ขนาดพื้นหลังของ Level Bar
        levelBarBackground.setTranslateX(30);
        levelBarBackground.setTranslateY(80);  // ตั้งตำแหน่งให้ห่างจาก health bar
        levelBarBackground.setFill(Color.GRAY);  // สีพื้นหลังของ Level Bar

        Rectangle levelBar = new Rectangle(200, 10);  // ขนาดของ Level Bar
        levelBar.setTranslateX(30);
        levelBar.setTranslateY(80);  // ตั้งตำแหน่งให้ห่างจาก health bar
        levelBar.setFill(Color.YELLOW);  // สีของ Level Bar

        // อัปเดต Level Bar
        updateLevelDisplay(levelBar);

        // เพิ่ม Level Bar ลงใน UI
        FXGL.getGameScene().addUINode(levelBarBackground);
        FXGL.getGameScene().addUINode(levelBar);
    }

    // อัปเดตการแสดงผลหลอดพลังชีวิต
    public void updateHealthDisplay(Rectangle healthBar) {
        int health = stats.getHealth();
        int maxHealth = 100;  // ค่าสูงสุดของพลังชีวิต

        // คำนวณความยาวของหลอดตามพลังชีวิตที่มี
        double healthPercentage = (double) health / maxHealth;
        healthBar.setWidth(200 * healthPercentage);  // ปรับความยาวของหลอดพลังชีวิต
    }

    // อัปเดตการแสดงผล Level Bar
    public void updateLevelDisplay(Rectangle levelBar) {
        int level = stats.getLevel();
        int maxLevel = 100;  // ค่าสูงสุดของเลเวล

        // คำนวณความยาวของหลอดตามเลเวลที่มี
        double levelPercentage = (double) level / maxLevel;
        levelBar.setWidth(200 * levelPercentage);  // ปรับความยาวของ Level Bar
    }
}
