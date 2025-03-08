package com.project;

import com.almasb.fxgl.entity.components.IntegerComponent;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.HashMap;
import java.util.Map;

public class SkillSystem {
    private final Player player;
    private boolean isSkillActive = false; // ✅ เช็คว่าสกิลกำลังทำงานอยู่หรือไม่
    private final Map<KeyCode, ImageView> skillIcons = new HashMap<>();

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
            System.out.println("A skill is already active! Wait for cooldown.");
            return;
        }
        
        if (!player.hasComponent(Stats.class)) {
            System.out.println("Error: Player does not have Stats component!");
            return;
        }

        Stats stats = player.getComponent(Stats.class);
        int manaCost = getManaCostForSkill(keyCode);
        stats.setMana(stats.getMana() - manaCost);
        
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

    public void startManaRegeneration() {
        run(() -> {
            Stats stats = player.getComponent(Stats.class);
            if (stats.getMana() < stats.getMaxMana()) {
                stats.setMana(stats.getMana() + 1); // เพิ่มมานาทีละ 1
            }
        }, Duration.seconds(1));
    }

    // ฟังก์ชันกำหนดค่ามานาสำหรับแต่ละสกิล
    private int getManaCostForSkill(KeyCode keyCode) {
        return switch (keyCode) {
            case Q -> 10;
            case E -> 15;
            case R -> 20;
            default -> 0;
        };
    }

    private void skillOne() {
        System.out.println("healเต็มเลือดซะะะ!!!");
        modifyPlayerStat(10);
        play("skill1_sound.mp3");
    }

    private void skillTwo() {
        System.out.println("หมัดอันทรงพลังงงงง!!!");
        play("skill2_sound.mp3");
    }

    private void skillThree() {
        System.out.println("The Flash!!!");
        play("skill3_sound.mp3");
    }

    private void setSkillCooldown(KeyCode keyCode, Duration duration) {
        ImageView skillIcon = skillIcons.get(keyCode);
        if (skillIcon != null) {
            skillIcon.setOpacity(0.5); // ✅ เปลี่ยนเป็นสีเทา
        }

        System.out.println("Skill " + keyCode + " on cooldown for " + duration.toSeconds() + " seconds.");

        runOnce(() -> {
            isSkillActive = false; // ✅ ปลดล็อกให้ใช้สกิลใหม่ได้
            if (skillIcon != null) {
                skillIcon.setOpacity(1.0); // ✅ คืนค่าไอคอนเป็นปกติ
            }
            System.out.println("Skill " + keyCode + " is ready to use again!");
        }, duration);
    }

    private void modifyPlayerStat(int value) {
        if (player.hasComponent(IntegerComponent.class)) {
            IntegerComponent stat = player.getComponent(IntegerComponent.class);
            stat.setValue(stat.getValue() + value);
        } else {
            System.out.println("Player has no IntegerComponent!");
        }
    }    
}
