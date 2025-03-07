package com.project;

import com.almasb.fxgl.entity.components.IntegerComponent;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;

public class SkillSystem {
    private final Player player;

    public SkillSystem(Player player2) {
        this.player = player2;
    }

    public void activateSkill(KeyCode keyCode) {
        switch (keyCode) {
            case Q -> {
                skillOne();
                showSkillIcon("icon.png");
            }
            case E -> { 
                skillTwo();
                showSkillIcon("icon1.png");
            }
            case R -> {
                skillThree(); 
                showSkillIcon("icon2.png");
            }
            default -> System.out.println("No skill assigned to this key.");
        }
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

    private void showSkillIcon(String iconName) {
        runOnce(() -> {
            ImageView skillIcon = new ImageView(texture(iconName).getImage()); // โหลดภาพจาก assets
            skillIcon.setFitWidth(50);
            skillIcon.setFitHeight(50);

            StackPane iconContainer = new StackPane(skillIcon);
            iconContainer.setTranslateX(getAppWidth() - 80); // ชิดขวา
            iconContainer.setTranslateY(20); // ชิดขอบบน

            addUINode(iconContainer);

            // ลบไอคอนออกหลังจาก 3 วินาที
            runOnce(() -> removeUINode(iconContainer), Duration.seconds(3));
        }, Duration.seconds(0.1));
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
