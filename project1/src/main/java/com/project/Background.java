package com.project;

import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.dsl.FXGL;

public class Background {

    // เมธอดสร้างพื้นหลัง
    public static Entity createBackground() {
        return FXGL.entityBuilder()
                .view(FXGL.texture("map.png"))  // ใช้ texture ของภาพ
                .at(0, 0)  // ตำแหน่งที่ภาพจะปรากฏในฉาก
                .build();
    }

}

