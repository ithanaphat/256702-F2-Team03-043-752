package com.project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Stats {

    private IntegerProperty health = new SimpleIntegerProperty(100);
    private IntegerProperty score;
    private IntegerProperty attack;
    private IntegerProperty level;
    private final int maxHealth = 100;

    

    public Stats(int health, int score, int attack,int level) {
        this.health = new SimpleIntegerProperty(health);
        this.score = new SimpleIntegerProperty(score);
        this.attack = new SimpleIntegerProperty(attack);
        this.level = new SimpleIntegerProperty(level);
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

    public Stats(int health) {
        this.health.set(health);
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

   

    


}
