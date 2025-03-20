package com.project;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.HashMap;
import java.util.Map;

public class SkillSystem {
    private final Player player;
    private boolean isSkillActive = false; // ✅ เช็คว่าสกิลกำลังทำงานอยู่หรือไม่
    private boolean isSkillEActive = false; // ✅ เช็คว่าสกิล E กำลังทำงานอยู่หรือไม่
    private final Map<KeyCode, ImageView> skillIcons = new HashMap<>();
    private boolean shieldActive = false; // ✅ ตัวแปรเช็คสถานะโล่

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
        System.out.println("healเต็มเลือดซะะะ!!!");
        player.getStats().heal(20); // เพิ่มการฟื้นฟูเลือด
        play("skill1_sound.mp3");
    }

    private void skillTwo() {
        System.out.println("หมัดอันทรงพลังงงงง!!!");
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

        play("skill2_sound.mp3");
    }

    private void skillThree() {
        System.out.println("The Flash!!!");
        Animation animation = player.getPlayerEntity().getComponent(Animation.class);
        if (animation == null) {
            System.out.println("Animation component not found!");
            return;
        }
    
        // ดึงทิศจาก input ปัจจุบัน
        double dirX = 0;
        double dirY = 0;
    
        if (animation.isUp()) dirY -= 1;
        if (animation.isDown()) dirY += 1;
        if (animation.isLeft()) dirX -= 1;
        if (animation.isRight()) dirX += 1;
    
        // ❌ ถ้าไม่ได้กดทิศทางใดเลย → ไม่ Dash
        if (dirX == 0 && dirY == 0) {
            System.out.println("ข่าไม่ขยับ จะskillไม่ได้นะจ๊ะ");
            isSkillActive = false; // คืน status skill ทันที เพราะไม่ Dash
            return;
        }
    
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length;
        dirY /= length;
    
        double dashPower = 450;
        animation.dash(dirX * dashPower, dirY * dashPower);
    
        play("skill3_sound.mp3");
    }

    public boolean isSkillEActive() {
        return isSkillEActive; // ✅ เช็คสถานะสกิล E
    }

    private void setSkillCooldown(KeyCode keyCode, Duration duration) {
        ImageView skillIcon = skillIcons.get(keyCode);
        if (skillIcon != null) {
            skillIcon.setOpacity(0.5); // ✅ เปลี่ยนเป็นสีเทา
        }


        runOnce(() -> {
            isSkillActive = false; // ✅ ปลดล็อกให้ใช้สกิลใหม่ได้
            if (skillIcon != null) {
                skillIcon.setOpacity(1.0); // ✅ คืนค่าไอคอนเป็นปกติ
            }
        }, duration);
    }
}
