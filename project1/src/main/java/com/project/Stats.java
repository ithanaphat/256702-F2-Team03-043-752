package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.time.Duration;

public class Stats extends Component {

    private IntegerProperty health;
    private IntegerProperty score;
    private IntegerProperty attack;
    private IntegerProperty level;
    private IntegerProperty experience;
    private final int maxHealth;

    public Stats(int health, int score, int attack, int level) {
        this.health = new SimpleIntegerProperty(health);
        this.score = new SimpleIntegerProperty(score);
        this.attack = new SimpleIntegerProperty(attack);
        this.level = new SimpleIntegerProperty(level);
        this.experience = new SimpleIntegerProperty(0); // กำหนดค่าเริ่มต้นให้กับ experience
        maxHealth = health;
    }

    // Getter และ Setter สำหรับ health

    // Getter และ Setter สำหรับ score
    public IntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public int getLevel() {
        return level.get();
    }

    public void setLevel(int level) {
        this.level.set(level);
    }
    // Getter และ Setter สำหรับ attack (พลังโจมตี)
    public IntegerProperty attackProperty() {
        return attack;
    }

    public int getAttack() {
        return attack.get();
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }

    public void increaseAttackTemporarily(int value, Duration duration) {
        int originalAttack = getAttack();
        setAttack(originalAttack + value);
        System.out.println("Temporary Attack Boost: " + getAttack());
    
        FXGL.runOnce(() -> {
            setAttack(originalAttack);
            System.out.println("Attack Boost Ended: " + getAttack());
        }, javafx.util.Duration.millis(duration.toMillis()));
    }
    
    public int getHealth() {
        return health.get();
    }

    public void setHealth(int value) {
        health.set(value);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void heal(int amount) {
        health.set(Math.min(health.get() + amount, maxHealth));
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    public void damage(int amount) {
        health.set(Math.max(health.get() - amount, 0));
    }

    public int getExperience() {
        return experience.get();
    }

    public void setExperience(int experience) {
        System.out.println("Before setExperience: " + this.experience.get());
        this.experience.set(experience);
        System.out.println("After setExperience: " + this.experience.get());
    }

    public IntegerProperty experienceProperty() {
        return experience;
    }

    public void addExperience(int amount) {
        experience.set(experience.get() + amount);
    System.out.println("Exp Updated in Stats: " + getExperience());

    while (experience.get() >= getExperienceForNextLevel()) {
        experience.set(experience.get() - getExperienceForNextLevel());
        level.set(level.get() + 1);
    }

    
    }

    int getExperienceForNextLevel() {
        int currentLevel = level.get();
        if (currentLevel < 10) {
            return 10;
        } else if (currentLevel < 20) {
            return 20;
        } else {
            return 30;
        }
    }
}
