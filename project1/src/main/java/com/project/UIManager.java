package com.project;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIManager {
    private Stats stats;
    private Rectangle healthBar; // ✅ ตัวแปรเก็บ Health Bar
    private Rectangle levelBar; // ✅ ตัวแปรเก็บ Level Bar
    private Text healthText; // ✅ ข้อความแสดงค่า HP
    private Text levelText; // ✅ ข้อความแสดงค่า Level
    private Text attackText; // ✅ ข้อความแสดงค่าพลังโจมตี
    private Text pointsText; // ✅ ข้อความแสดงค่า Points
    private Button btnUpgradeHP; // ✅ ปุ่มอัพเกรด HP
    private Button btnUpgradeAttack; // ✅ ปุ่มอัพเกรด Attack
    private SkillSystem skillSystem; // ✅ เพิ่มตัวแปร SkillSystem
    private Rectangle bossHealthBar; // ✅ แถบเลือดบอส
    private Rectangle bossHealthBarBackground; // ✅ พื้นหลังของแถบเลือดบอส
    private Text bossHealthText; // ✅ ตัวเลขเลือดบอส
    private ImageView bossFrame; // ✅ เพิ่มตัวแปรสำหรับ bossFrame

    public UIManager() {
        this.stats = FXGL.geto("playerStats");
        this.skillSystem = FXGL.geto("skillSystem");

        // ฟังค่าจาก playerStats ถ้าเปลี่ยนจะอัปเดต UI อัตโนมัติ
        FXGL.getWorldProperties().<Stats>addListener("playerStats", (oldStats, newStats) -> {
            this.stats = newStats;
            updateHealthDisplay();
        });

        stats.experienceProperty().addListener((obs, oldVal, newVal) -> updateHealthDisplay());
    }

    public void initUI() {

        // สร้างปุ่มใหม่ที่เมื่อเอาเมาส์ไปชี้จะแสดงข้อความ "wasd"
        Button btnWASD = new Button("i");
        btnWASD.setTranslateX(30);
        btnWASD.setTranslateY(250);
        btnWASD.setPrefSize(50, 50);
        btnWASD.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // ใช้คลาส CSS
        btnWASD.getStyleClass().add("button-default");

        // เพิ่มเอฟเฟกต์ hover
        btnWASD.setOnMouseEntered(e -> {
            btnWASD.getStyleClass().remove("button-default");
            btnWASD.getStyleClass().add("button-hover");
        });

        btnWASD.setOnMouseExited(e -> {
            btnWASD.getStyleClass().remove("button-hover");
            btnWASD.getStyleClass().add("button-default");
        });

        // สร้าง Tooltip ให้ปุ่มนี้
        Tooltip tooltip = new Tooltip("Left Click = Attack\n" +
                "W = ⬆\n" +
                "A = ⬅\n" +
                "S = ⬇\n" +
                "D = ⮕\n" +
                "Q = Heal\n" +
                "E = BuffAtt\n" +
                "R = Dash");
        Tooltip.install(btnWASD, tooltip);

        FXGL.getGameScene().addUINode(btnWASD);

        // ✅ สร้างข้อความ "HP:"
        Text hpLabel = new Text("HP: ");
        hpLabel.setTranslateX(30);
        hpLabel.setTranslateY(30);
        hpLabel.setFill(Color.WHITE);
        hpLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Health Bar
        Rectangle healthBarBackground = new Rectangle(321, 17);
        healthBarBackground.setTranslateX(34);
        healthBarBackground.setTranslateY(24);
        healthBarBackground.setFill(Color.GRAY);

        // ✅ สร้าง Health Bar สีแดง
        healthBar = new Rectangle(321, 17);
        healthBar.setTranslateX(34);
        healthBar.setTranslateY(24);
        healthBar.setFill(Color.RED);

        // ✅ แสดงค่า HP เป็นตัวเลข
        healthText = new Text();
        healthText.setTranslateX(175);
        healthText.setTranslateY(38);
        healthText.setFill(Color.WHITE);
        healthText.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GREEN),
                new Stop(1, Color.YELLOW));

        // ✅ สร้างข้อความ "Level:"
        levelText = new Text();
        levelText.setTranslateX(400);
        levelText.setTranslateY(34);
        levelText.setFill(Color.WHITE);
        levelText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ✅ สร้างพื้นหลังของ Level Bar
        Rectangle levelBarBackground = new Rectangle(600, 8);
        levelBarBackground.setTranslateX(400);
        levelBarBackground.setTranslateY(5);
        levelBarBackground.setFill(Color.GRAY);
        levelBarBackground.setStroke(Color.BLACK); // Set the border color
        levelBarBackground.setStrokeWidth(3); // Set the border width

        // ✅ สร้าง Level Bar สีน้ำเงิน
        levelBar = new Rectangle(600, 8);
        levelBar.setTranslateX(400);
        levelBar.setTranslateY(5);
        levelBar.setFill(gradient);

        // ✅ สร้างข้อความ "Attack:"
        attackText = new Text();
        attackText.setTranslateX(30);
        attackText.setTranslateY(90);
        attackText.setFill(Color.WHITE);
        attackText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ✅ สร้างข้อความ "Points:"
        pointsText = new Text();
        pointsText.setTranslateX(1100);
        pointsText.setTranslateY(28);
        pointsText.setFill(Color.WHITE);
        pointsText.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        // ✅ สร้างปุ่มอัพเกรด HP

        btnUpgradeHP = new Button("⬆HP");
        btnUpgradeHP.setTranslateX(30);
        btnUpgradeHP.setTranslateY(100);
        btnUpgradeHP.setPrefSize(90, 60);
        btnUpgradeHP.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // ใช้คลาส CSS
        btnUpgradeHP.getStyleClass().add("button-default");

        // เพิ่มเอฟเฟกต์ hover
        btnUpgradeHP.setOnMouseEntered(e -> {
            btnUpgradeHP.getStyleClass().remove("button-default");
            btnUpgradeHP.getStyleClass().add("button-hover");
        });

        btnUpgradeHP.setOnMouseExited(e -> {
            btnUpgradeHP.getStyleClass().remove("button-hover");
            btnUpgradeHP.getStyleClass().add("button-default");
        });

        btnUpgradeHP.setOnAction(e -> upgradeHP());

        // เพิ่ม Tooltip ให้กับ btnUpgradeHP
        Tooltip hpTooltip = new Tooltip("เพิ่มค่า HP 10 หน่วย");
        Tooltip.install(btnUpgradeHP, hpTooltip);

        // ✅ สร้างปุ่มอัพเกรด Attack

        btnUpgradeAttack = new Button("⬆ATK");
        btnUpgradeAttack.setTranslateX(30);
        btnUpgradeAttack.setTranslateY(170);
        btnUpgradeAttack.setPrefSize(90, 60);
        btnUpgradeAttack.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // ใช้คลาส CSS
        btnUpgradeAttack.getStyleClass().add("button-default");

        // เพิ่มเอฟเฟกต์ hover
        btnUpgradeAttack.setOnMouseEntered(e -> {
            btnUpgradeAttack.getStyleClass().remove("button-default");
            btnUpgradeAttack.getStyleClass().add("button-hover");
        });

        btnUpgradeAttack.setOnMouseExited(e -> {
            btnUpgradeAttack.getStyleClass().remove("button-hover");
            btnUpgradeAttack.getStyleClass().add("button-default");
        });

        btnUpgradeAttack.setOnAction(e -> upgradeAttack());

        // เพิ่ม Tooltip ให้กับ btnUpgradeAttack
        Tooltip attackTooltip = new Tooltip("เพิ่มค่าพลังโจมตี 1");
        Tooltip.install(btnUpgradeAttack, attackTooltip);

        ImageView hpFrame = FXGL.texture("test.png");
        hpFrame.setTranslateX(10); // กำหนดตำแหน่ง X ของรูปภาพ
        hpFrame.setTranslateY(5); // กำหนดตำแหน่ง Y ของรูปภาพ

        ImageView point = FXGL.texture("point.png");
        point.setTranslateX(1050);
        point.setTranslateY(5);
        Tooltip pointTooltip = new Tooltip("Points: ใช้สำหรับอัพเกรดค่าต่าง ๆ เช่น HP และ Attack");
        Tooltip.install(point, pointTooltip);

        bossFrame = FXGL.texture("bossgauge.png");
        bossFrame.setTranslateX(450); // กำหนดตำแหน่ง X
        bossFrame.setTranslateY(30); // กำหนดตำแหน่ง Y
        bossFrame.setVisible(false); // ซ่อนภาพเริ่มต้น

        // ✅ สร้างพื้นหลังของแถบเลือดบอส
        bossHealthBarBackground = new Rectangle(400, 20);
        bossHealthBarBackground.setTranslateX(470);
        bossHealthBarBackground.setTranslateY(50);
        bossHealthBarBackground.setFill(Color.GRAY);

        // ✅ สร้างแถบเลือดบอส
        bossHealthBar = new Rectangle(400, 20);
        bossHealthBar.setTranslateX(470);
        bossHealthBar.setTranslateY(50);
        bossHealthBar.setFill(Color.RED);

        // ✅ สร้างตัวเลขเลือดบอส
        bossHealthText = new Text();
        bossHealthText.setTranslateX(630);
        bossHealthText.setTranslateY(67);
        bossHealthText.setFill(Color.WHITE);
        bossHealthText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        bossHealthText.setVisible(false); // ซ่อนตัวเลขเลือดบอสเริ่มต้น

        // เพิ่ม UI ลงในหน้าจอ
        FXGL.getGameScene().addUINode(bossHealthBarBackground);
        FXGL.getGameScene().addUINode(bossHealthBar);
        FXGL.getGameScene().addUINode(bossHealthText);
        FXGL.getGameScene().addUINode(bossFrame);

        // ซ่อนแถบเลือดบอสเริ่มต้น
        bossHealthBar.setVisible(false);
        bossHealthBarBackground.setVisible(false); // ซ่อนพื้นหลังของแถบเลือดบอสเริ่มต้น

        // ✅ เพิ่ม UI เข้าไปในเกม
        // FXGL.getGameScene().addUINode(hpLabel);
        FXGL.getGameScene().addUINode(healthBarBackground);
        FXGL.getGameScene().addUINode(healthBar);
        FXGL.getGameScene().addUINode(hpFrame);
        FXGL.getGameScene().addUINode(healthText);

        FXGL.getGameScene().addUINode(point);

        FXGL.getGameScene().addUINode(levelText);
        FXGL.getGameScene().addUINode(levelBarBackground);
        FXGL.getGameScene().addUINode(levelBar);

        FXGL.getGameScene().addUINode(attackText);
        FXGL.getGameScene().addUINode(pointsText);
        FXGL.getGameScene().addUINode(btnUpgradeHP);
        FXGL.getGameScene().addUINode(btnUpgradeAttack);

        // ✅ อัปเดตค่าเริ่มต้น
        updateHealthDisplay();
    }

    // ✅ ฟังก์ชันอัปเดต Health Bar
    public void updateHealthDisplay() {
        Stats stats = FXGL.geto("playerStats");

        levelText.setText("Level: " + stats.getLevel());

        int health = stats.getHealth();
        int maxHealth = stats.getMaxHealth();

        // คำนวณความกว้างของ Health Bar
        double healthPercentage = (double) health / maxHealth;
        healthBar.setWidth(321 * healthPercentage);

        // อัปเดตค่า HP ที่แสดงผล
        healthText.setText(health + " / " + maxHealth);

        // อัปเดตค่า Level ที่แสดงผล
        levelText.setText("Level: " + stats.getLevel());

        // อัปเดตค่าพลังโจมตีที่แสดงผล
        attackText.setText("Attack: " + stats.getAttack());

        // อัปเดตค่า Points ที่แสดงผล
        pointsText.setText(": " + stats.getPoints());

        // คำนวณความกว้างของ Level Bar
        double levelPercentage = (double) stats.getExperience() / stats.getExperienceForNextLevel();
        levelBar.setWidth(600 * levelPercentage);
    }

    private void upgradeHP() {
        if (stats.getPoints() >= 1) {
            stats.setMaxHealth(stats.getMaxHealth() + 10); // Increase max health
            stats.setPoints(stats.getPoints() - 1);
            updateHealthDisplay();
        }
    }

    private void upgradeAttack() {
        if (skillSystem.isSkillEActive()) { // ✅ เช็คว่าสกิล E กำลังทำงานอยู่หรือไม่
            FXGL.getNotificationService().pushNotification("Cannot upgrade attack while Skill E is active!");
            return;
        }

        if (stats.getPoints() >= 1) {
            stats.setAttack(stats.getAttack() + 1);
            stats.setPoints(stats.getPoints() - 1);
            updateHealthDisplay();
        }
    }

    public void reloadStats() {
        this.stats = FXGL.geto("playerStats");
        updateHealthDisplay();
    }

    public void updateBossUI(int currentHealth, int maxHealth) {
        if (currentHealth <= 0) {
            bossHealthBar.setVisible(false);
            bossHealthBarBackground.setVisible(false); // ซ่อนพื้นหลังของแถบเลือดบอส
            bossHealthText.setVisible(false);
            bossFrame.setVisible(false); // ซ่อน bossFrame
            return;
        }

        bossHealthBar.setVisible(true);
        bossHealthBarBackground.setVisible(true); // แสดงพื้นหลังของแถบเลือดบอส
        bossHealthText.setVisible(true);
        bossFrame.setVisible(true); // แสดง bossFrame

        // อัปเดตความกว้างของแถบเลือด
        double healthPercentage = (double) currentHealth / maxHealth;
        bossHealthBar.setWidth(400 * healthPercentage);

        // อัปเดตตัวเลขเลือด
        bossHealthText.setText(currentHealth + " / " + maxHealth);
    }

    public void hideBossUI() {
        bossHealthBar.setVisible(false);
        bossHealthBarBackground.setVisible(false); // ซ่อนพื้นหลังของแถบเลือดบอส
        bossHealthText.setVisible(false);
        bossFrame.setVisible(false); // ซ่อน bossFrame
    }
}
