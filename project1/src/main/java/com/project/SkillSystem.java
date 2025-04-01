package com.project;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.HashMap;
import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;

public class SkillSystem {
    private Player player;
    private boolean isSkillActive = false; // ✅ เช็คว่าสกิลกำลังทำงานอยู่หรือไม่
    private boolean isSkillEActive = false; // ✅ เช็คว่าสกิล E กำลังทำงานอยู่หรือไม่
    private final Map<KeyCode, ImageView> skillIcons = new HashMap<>();
    private final Map<KeyCode, Text> cooldownTexts = new HashMap<>();

    public SkillSystem(Player player2) {
        this.player = player2;
        initSkillIcons(); // ✅ แสดงไอคอนตั้งแต่เริ่มเกม
    }

    private void initSkillIcons() {
        addSkillIcon(KeyCode.Q, "icon.png", 0);
        addSkillIcon(KeyCode.E, "icon1.png", 1);
        addSkillIcon(KeyCode.R, "icon2.png", 2);
    }

    private void addSkillIcon(KeyCode keyCode, String iconName, int index) {
        // ✅ เช็คก่อนว่าไอคอนมีอยู่แล้วหรือไม่
        if (skillIcons.containsKey(keyCode)) {
            return; // ถ้ามีอยู่แล้ว ไม่ต้องสร้างใหม่
        }

        ImageView skillIcon = new ImageView(texture(iconName).getImage());
        skillIcon.setFitWidth(50);
        skillIcon.setFitHeight(50);
        skillIcon.setTranslateX(getAppWidth() - 80);
        skillIcon.setTranslateY(20 + (index * 55));

        skillIcons.put(keyCode, skillIcon);
        addUINode(skillIcon); // ✅ เพิ่มไอคอนครั้งเดียวเท่านั้น

        // เพิ่ม Text สำหรับ cooldown
        Text cooldownText = new Text("");
        cooldownText.setFont(Font.font(20));
        cooldownText.setFill(Color.RED);
        cooldownText.setTranslateX(skillIcon.getTranslateX() + 15);
        cooldownText.setTranslateY(skillIcon.getTranslateY() + 30);
        cooldownText.setVisible(false);

        cooldownTexts.put(keyCode, cooldownText);
        addUINode(cooldownText);
    }

    public void activateSkill(KeyCode keyCode) {
        if (isSkillActive) { // ✅ ถ้ามีสกิลทำงานอยู่ ห้ามใช้สกิลใหม่
            return;
        }

        isSkillActive = true; // ✅ ตั้งค่ากำลังใช้สกิล
        switch (keyCode) {
            case Q -> {
                skillOne();
                setSkillCooldown(keyCode, Duration.seconds(5));
            }
            case E -> {
                skillTwo();
                setSkillCooldown(keyCode, Duration.seconds(6));
            }
            case R -> {
                skillThree();
                setSkillCooldown(keyCode, Duration.seconds(7));
            }
            default -> System.out.println("No skill assigned to this key.");
        }
    }

    private void skillOne() {
        showFloatingText("healเต็มเลือดซะะะ!!!",Color.RED);
        Stats stats = player.getStats();
        int maxHealth = stats.getMaxHealth();
        int healAmount = (int) (maxHealth * 0.1); // Heal 10% of max health

        // Heal every second for the duration of the cooldown
        Duration healDuration = Duration.seconds(5);
        int healTicks = (int) healDuration.toSeconds();

        for (int i = 0; i < healTicks; i++) {
            runOnce(() -> {
                stats.heal(healAmount);
                // Update the UI after healing
                UIManager uiManager = FXGL.geto("uiManager");
                uiManager.updateHealthDisplay();
            }, Duration.seconds(i));
        }

        FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("skill1.mp3"));

    }

    private void skillTwo() {
        showFloatingText("หมัดอันทรงพลังงงงง!!!",Color.ORANGE);
        Stats stats = player.getStats();
        int originalAttack = stats.getAttack();
        int increasedAttack = (int) (originalAttack * 1.5); // เพิ่มพลังโจมตี 50%
        stats.setAttack(increasedAttack);
        isSkillEActive = true; // ✅ ตั้งค่าสถานะสกิล E เป็นทำงาน

        // ตั้งค่าให้พลังโจมตีกลับมาเท่าเดิมหลังจาก 5 วินาที
        runOnce(() -> {
            stats.setAttack(originalAttack);
            isSkillEActive = false; // ✅ ตั้งค่าสถานะสกิล E เป็นไม่ทำงาน
        }, Duration.seconds(5));

        FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("skill2.mp3"));
    }

    private void skillThree() {
        Animation animation = player.getPlayerEntity().getComponent(Animation.class);

        // ดึงทิศจาก input ปัจจุบัน
        double dirX = 0;
        double dirY = 0;

        if (animation.isUp())
            dirY -= 1;
        if (animation.isDown())
            dirY += 1;
        if (animation.isLeft())
            dirX -= 1;
        if (animation.isRight())
            dirX += 1;

        // ❌ ถ้าไม่ได้กดทิศทางใดเลย → ไม่ Dash
        if (dirX == 0 && dirY == 0) {
            showFloatingText("ข่าไม่ขยับ จะskillไม่ได้นะจ๊ะ",Color.GREEN);
            isSkillActive = false; // คืน status skill ทันที เพราะไม่ Dash
            return;
        }

        showFloatingText("The Flash!!!",Color.GREEN);

        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length;
        dirY /= length;

        double dashPower = 800;
        animation.dash(dirX * dashPower, dirY * dashPower);

        FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("skill3.mp3"));
    }

    public boolean isSkillEActive() {
        return isSkillEActive; // ✅ เช็คสถานะสกิล E
    }

    private void setSkillCooldown(KeyCode keyCode, Duration duration) {
        ImageView skillIcon = skillIcons.get(keyCode);
        Text cooldownText = cooldownTexts.get(keyCode);

        if (skillIcon != null) {
            skillIcon.setOpacity(0.5); // ✅ เปลี่ยนเป็นสีเทา
        }
        if (cooldownText != null) {
            cooldownText.setVisible(true);
        }

        double totalSeconds = duration.toSeconds();

        // สร้างตัวแปร array เพื่อใช้ใน lambda (เพราะ int ใช้ไม่ได้ใน lambda)
        final int[] remainingSeconds = { (int) totalSeconds };

        // แสดงตัวเลข countdown ทุก 1 วิ
        run(() -> {
            if (cooldownText != null) {
                cooldownText.setText(String.valueOf(remainingSeconds[0]));
                remainingSeconds[0]--;
            }
            if (remainingSeconds[0] < 0) {
                if (cooldownText != null)
                    cooldownText.setVisible(false);
                if (skillIcon != null)
                    skillIcon.setOpacity(1.0);
                isSkillActive = false;
                getGameTimer().clear(); // หยุด run loop
            }
        }, Duration.seconds(1));
    }

   private void showFloatingText(String message , Color color) {
    // สร้างข้อความหลัก
    Text floatingText = new Text(message);
    floatingText.setFont(Font.font("TH Sarabun PSK", FontWeight.BOLD, 24));    
    floatingText.setFill(color);

    // สร้างเอฟเฟกต์ Stroke ให้อยู่นอกตัวอักษร
    DropShadow outline = new DropShadow();
    outline.setColor(Color.BLACK);
    outline.setRadius(3);
    outline.setSpread(1.0); // ทำให้ Stroke อยู่ด้านนอกทั้งหมด

    floatingText.setEffect(outline);

    // world position ของ player
    double playerX = player.getPlayerEntity().getX();
    double playerY = player.getPlayerEntity().getY();

    // viewport offset (กล้อง)
    double camX = getGameScene().getViewport().getX();
    double camY = getGameScene().getViewport().getY();

    // world → screen
    double screenX = playerX - camX;
    double screenY = playerY - camY;

    // วาง text ชั่วคราวเพื่อให้ CSS ทำงาน
    addUINode(floatingText);
    floatingText.applyCss();

    // ดึงขนาดจริงของข้อความหลัง applyCss
    double textWidth = floatingText.getLayoutBounds().getWidth();

    // Center text ให้ตรงหัว player พอดี
    floatingText.setTranslateX(screenX - textWidth / 2);
    floatingText.setTranslateY(screenY - 50); // ลอยขึ้นเหนือหัว

    // ทำข้อความลอยขึ้นเรื่อยๆ
    run(() -> floatingText.setTranslateY(floatingText.getTranslateY() - 1), Duration.seconds(0.016), 30);

    // ลบออกหลังจาก 1.5 วินาที
    runOnce(() -> removeUINode(floatingText), Duration.seconds(1.5));
}


    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

}
