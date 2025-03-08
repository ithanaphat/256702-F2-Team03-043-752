package com.project;

import com.almasb.fxgl.entity.component.Component;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Stats extends Component{

    private IntegerProperty health;
    private IntegerProperty score;
    private IntegerProperty attack;
    private IntegerProperty mana;
    private IntegerProperty level;
    private IntegerProperty experience;
    private final int maxHealth;
    private final int maxMana;

    

    public Stats(int health, int score, int attack, int level, int mana) {
        this.health = new SimpleIntegerProperty(health);
        this.score = new SimpleIntegerProperty(score);
        this.attack = new SimpleIntegerProperty(attack);
        this.level = new SimpleIntegerProperty(level);
        this.mana = new SimpleIntegerProperty(mana);
        this.experience = new SimpleIntegerProperty(0); // กำหนดค่าเริ่มต้นให้กับ experience
        maxHealth = health;
        this.maxMana = mana;
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

    public IntegerProperty manaProperty() {
        return mana;
    }

    public int getMana() {
        return mana.get();
    }

    public void setMana(int value) {
        mana.set(Math.max(0, Math.min(value, maxMana))); // จำกัดค่าให้อยู่ระหว่าง 0 - maxMana
    }

    public int getMaxMana() {
        return maxMana;
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
        this.experience.set(experience);
    }

    public void addExperience(int amount) {
        experience.set(experience.get() + amount);
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
