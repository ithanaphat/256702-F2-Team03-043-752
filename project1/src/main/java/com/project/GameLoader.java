package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import com.almasb.fxgl.entity.SpawnData;

import static com.almasb.fxgl.dsl.FXGL.spawn;

public class GameLoader {

    public static void loadPlayerFromSave(SaveData data) {
        if (data == null) {
            FXGL.getNotificationService().pushNotification("No Save Data Found!");
            return;
        }

        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(Entity::removeFromWorld);
        FXGL.getGameWorld().getEntitiesByType(EntityType.MONSTER).forEach(Entity::removeFromWorld);
        FXGL.getGameWorld().getEntitiesByType(EntityType.BOSS).forEach(Entity::removeFromWorld);

        Player player = new Player("playerimage.png");
        Entity newPlayer = player.createPlayer(data.getHealth(), data.getAttack(), data.getLevel(),
                data.getExperience());

        newPlayer.setPosition(data.getPosX(), data.getPosY());

        Stats stats = newPlayer.getComponent(Stats.class);
        stats.setHealth(data.getHealth());
        stats.setMaxHealth(data.getMaxHealth());
        stats.setAttack(data.getAttack());
        stats.setLevel(data.getLevel());
        stats.setExperience(data.getExperience());

        FXGL.set("playerStats", stats);
        UIManager uiManager = FXGL.geto("uiManager");
        uiManager.reloadStats();
        SkillSystem skillSystem = FXGL.geto("skillSystem");
        skillSystem.setPlayer(player);

        for (Point2D pos : data.getEnemyPositions()) {
            spawn("monster", pos.getX(), pos.getY());
        }

        // ตรวจสอบสถานะของบอส
    if (data.getBossHealth() > 0) {
        Entity boss = spawn("boss", data.getBossPosX(), data.getBossPosY());
        Health bossHealth = boss.getComponent(Health.class);
        if (bossHealth != null) {
            bossHealth.setValue(data.getBossHealth());
        }
        ((App) FXGL.getApp()).setBossSpawned(true);

        // เปลี่ยนเพลงเป็นเพลงบอส
        FXGL.getAudioPlayer().stopAllMusic();
        FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("background_boss.mp3"));
    } else {
        ((App) FXGL.getApp()).setBossSpawned(false);

        // เปลี่ยนเพลงเป็นเพลงพื้นหลังปกติ
        FXGL.getAudioPlayer().stopAllMusic();
        FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("background.mp3"));
    }

        FXGL.getNotificationService().pushNotification("Game Loaded!");
        FXGL.<UIManager>geto("uiManager").updateHealthDisplay();
        ((App) FXGL.getApp()).reloadStatsAfterLoad();

        // Rebind the camera to the new player entity
        FXGL.getGameScene().getViewport().bindToEntity(newPlayer, FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
        FXGL.getGameScene().getViewport().setBounds(-200, -200, 1500, 900); // Adjust the bounds as needed
        FXGL.getGameScene().getViewport().setZoom(1); // ซูมเข้าหน้าจอ

        // เรียกใช้การ spawn มอนสเตอร์ใหม่
        ((App) FXGL.getApp()).startMonsterSpawnTask();
    }
}