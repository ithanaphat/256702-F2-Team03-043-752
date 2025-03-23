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
    private IntegerProperty points; // Add points property
    private int maxHealth;

    public Stats(int health, int score, int attack, int level) {
        this.health = new SimpleIntegerProperty(health);
        this.score = new SimpleIntegerProperty(score);
        this.attack = new SimpleIntegerProperty(attack);
        this.level = new SimpleIntegerProperty(level);
        this.experience = new SimpleIntegerProperty(0); // กำหนดค่าเริ่มต้นให้กับ experience
        this.points = new SimpleIntegerProperty(0); // Initialize points
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

        FXGL.runOnce(() -> {
            setAttack(originalAttack);

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

    public void setMaxHealth(int value) {
        maxHealth = value;
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

        this.experience.set(experience);

    }

    public IntegerProperty experienceProperty() {
        return experience;
    }

    public int getPoints() {
        return points.get();
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public void addExperience(int amount) {
        experience.set(experience.get() + amount);

        while (experience.get() >= getExperienceForNextLevel()) {
            experience.set(experience.get() - getExperienceForNextLevel());
            level.set(level.get() + 1);
            points.set(points.get() + 5); // Grant 5 points per level
        }
    }

    int getExperienceForNextLevel() {
        int currentLevel = level.get();
        return (int) Math.pow(1.3, currentLevel) * 10; // Exponential progression
    }
}
