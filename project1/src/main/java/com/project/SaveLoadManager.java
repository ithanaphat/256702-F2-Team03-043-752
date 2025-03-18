package com.project;

import com.almasb.fxgl.dsl.FXGL;
import java.io.*;

public class SaveLoadManager {
    private static final String SAVE_FILE = "c:/Users/t1669/OneDrive/เดสก์ท็อป/java practise/Game Project Java/256702-F2-Team03-043-752/savegame.dat";

    public static void saveGame(Stats stats, double posX, double posY) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            SaveData data = new SaveData(
                stats.getHealth(),
                stats.getAttack(),
                stats.getLevel(),
                stats.getExperience(),
                posX,
                posY
            );
            out.writeObject(data);
            FXGL.getNotificationService().pushNotification("บันทึกเกมสำเร็จ!");
        } catch (IOException e) {
            FXGL.getNotificationService().pushNotification("บันทึกเกมล้มเหลว!");
            e.printStackTrace();
        }
    }

    public static SaveData loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (SaveData) in.readObject();
        } catch (FileNotFoundException e) {
            FXGL.getNotificationService().pushNotification("ไม่พบไฟล์เซฟ!");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            FXGL.getNotificationService().pushNotification("เกิดข้อผิดพลาดในการอ่านไฟล์เซฟ!");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            FXGL.getNotificationService().pushNotification("ไม่พบคลาส SaveData!");
            e.printStackTrace();
            return null;
        }
    }
}
