package com.project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Stats {

    private IntegerProperty health;
    private IntegerProperty score;
    private IntegerProperty attack;
    private IntegerProperty level;

    

    public Stats(int health, int score, int attack,int level) {
        this.health = new SimpleIntegerProperty(health);
        this.score = new SimpleIntegerProperty(score);
        this.attack = new SimpleIntegerProperty(attack);
        this.level = new SimpleIntegerProperty(level);
    }

    // Getter และ Setter สำหรับ health
    public IntegerProperty healthProperty() {
        return health;
    }

    public int getHealth() {
        return health.get();
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

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

   

    


}
