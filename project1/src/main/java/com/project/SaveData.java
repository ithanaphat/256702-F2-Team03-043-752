package com.project;

import java.io.Serializable;

public class SaveData implements Serializable {
    private int health;
    private int level;
    private int experience;
    private int attack;
    private double posX;
    private double posY;

    public SaveData(int health, int level, int experience, int attack, double posX, double posY) {
        this.health = health;
        this.level = level;
        this.experience = experience;
        this.attack = attack;
        this.posX = posX;
        this.posY = posY;
    }

    public int getHealth() { return health; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getAttack() { return attack; }
    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
}
