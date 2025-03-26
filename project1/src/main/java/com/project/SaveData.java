package com.project;

import java.io.Serializable;
import java.util.List;

import javafx.geometry.Point2D;

public class SaveData implements Serializable {
    private int health;
    private int maxHealth; // ✅ เพิ่มค่า Max HP
    private int level;
    private int experience;
    private int attack;
    private double posX;
    private double posY;
    private int bossHealth;
    private double bossPosX;
    private double bossPosY;
    private List<Point2D> enemyPositions; // Add enemy positions

    public SaveData(int health, int maxHealth, int level, int experience, int attack, double posX, double posY,
            List<Point2D> enemyPositions, int bossHealth, double bossPosX, double bossPosY) {
        this.health = health;
        this.level = level;
        this.maxHealth = maxHealth;
        this.experience = experience;
        this.attack = attack;
        this.posX = posX;
        this.posY = posY;
        this.enemyPositions = enemyPositions;
        this.bossHealth = bossHealth;
        this.bossPosX = bossPosX;
        this.bossPosY = bossPosY;
    }

    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getAttack() {
        return attack;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public List<Point2D> getEnemyPositions() {
        return enemyPositions;
    }

    public int getBossHealth() {
        return bossHealth;
    }

    public double getBossPosX() {
        return bossPosX;
    }

    public double getBossPosY() {
        return bossPosY;
    }
}
