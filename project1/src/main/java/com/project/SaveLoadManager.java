package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadManager {
    public static final String SAVE_FILE = "savegame.txt";

    public static void saveGame(Stats stats, double posX, double posY, List<Point2D> enemyPositions, Entity boss) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(stats.getHealth() + "\n");
            writer.write(stats.getMaxHealth() + "\n");
            writer.write(stats.getAttack() + "\n");
            writer.write(stats.getLevel() + "\n");
            writer.write(stats.getExperience() + "\n");
            writer.write(posX + "\n");
            writer.write(posY + "\n");
            for (Point2D pos : enemyPositions) {
                writer.write(pos.getX() + "," + pos.getY() + "\n");
            }

            if (boss != null) {
                writer.write(boss.getComponent(Health.class).getHealth() + "\n");
                writer.write(boss.getX() + "\n");
                writer.write(boss.getY() + "\n");
            } else {
                writer.write("-1\n");
            }

            FXGL.getNotificationService().pushNotification("บันทึกเกมสำเร็จ!");
        } catch (IOException e) {
            FXGL.getNotificationService().pushNotification("บันทึกเกมล้มเหลว!");
            e.printStackTrace();
        }
    }

    public static SaveData loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            int health = Integer.parseInt(reader.readLine());
            int maxHealth = Integer.parseInt(reader.readLine());
            int attack = Integer.parseInt(reader.readLine());
            int level = Integer.parseInt(reader.readLine());
            int experience = Integer.parseInt(reader.readLine());
            double posX = Double.parseDouble(reader.readLine());
            double posY = Double.parseDouble(reader.readLine());
            List<Point2D> enemyPositions = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null && line.contains(",")) {
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                enemyPositions.add(new Point2D(x, y));
            }

            int bossHealth = Integer.parseInt(line);
            double bossPosX = bossHealth == -1 ? -1 : Double.parseDouble(reader.readLine());
            double bossPosY = bossHealth == -1 ? -1 : Double.parseDouble(reader.readLine());

            return new SaveData(health, maxHealth, level, experience, attack, posX, posY, enemyPositions, bossHealth,
                    bossPosX, bossPosY);
        } catch (FileNotFoundException e) {
            FXGL.getNotificationService().pushNotification("ไม่พบไฟล์เซฟ!");
            e.printStackTrace();
            return null;
        } catch (IOException | NumberFormatException e) {
            FXGL.getNotificationService().pushNotification("เกิดข้อผิดพลาดในการอ่านไฟล์เซฟ!");
            e.printStackTrace();
            return null;
        }
    }
}