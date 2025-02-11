package com.project;

import com.almasb.fxgl.entity.Entity;
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

