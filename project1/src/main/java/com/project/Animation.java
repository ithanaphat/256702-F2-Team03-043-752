package com.project;

import com.almasb.fxgl.dsl.FXGL;

import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;

import javafx.scene.effect.BlendMode;

import javafx.scene.paint.Color;

import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Animation extends Component {

    private PhysicsComponent physics;
    private int speedX = 0;
    private int speedY = 0;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkRight, animWalkLeft, animWalkUp, animWalkDown;
    private AnimationChannel animWalkUpRight, animWalkUpLeft, animWalkDownRight, animWalkDownLeft;
 
    private boolean isAttacking = false; // เพิ่มตัวแปรสถานะการโจมตี


    public Animation(String image) {
        animIdle = new AnimationChannel(FXGL.image(image), 4, 64, 64, Duration.seconds(1), 0, 0);
        animWalkDown = new AnimationChannel(FXGL.image(image), 4, 64, 64, Duration.seconds(0.5), 0, 3);
        animWalkLeft = new AnimationChannel(FXGL.image(image), 4, 64, 64, Duration.seconds(0.5), 4, 7);
        animWalkRight = new AnimationChannel(FXGL.image(image), 4, 64, 64, Duration.seconds(0.5), 8, 11);
        animWalkUp = new AnimationChannel(FXGL.image(image), 4, 64, 64, Duration.seconds(0.5), 12, 15);

        animWalkUpRight = animWalkUp;
        animWalkUpLeft = animWalkUp;
        animWalkDownRight = animWalkDown;
        animWalkDownLeft = animWalkDown;

     
        texture = new AnimatedTexture(animIdle);
        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

    }

    @Override
    public void onUpdate(double tpf) {
        physics.setVelocityX(speedX);
        physics.setVelocityY(speedY);

        if (speedX > 0 && speedY < 0) { // เดินขึ้น-ขวา ↗
            if (texture.getAnimationChannel() != animWalkUpRight) {
                texture.loopAnimationChannel(animWalkUpRight);
            }
        } else if (speedX < 0 && speedY < 0) { // เดินขึ้น-ซ้าย ↖
            if (texture.getAnimationChannel() != animWalkUpLeft) {
                texture.loopAnimationChannel(animWalkUpLeft);
            }
        } else if (speedX > 0 && speedY > 0) { // เดินลง-ขวา ↘
            if (texture.getAnimationChannel() != animWalkDownRight) {
                texture.loopAnimationChannel(animWalkDownRight);
            }
        } else if (speedX < 0 && speedY > 0) { // เดินลง-ซ้าย ↙
            if (texture.getAnimationChannel() != animWalkDownLeft) {
                texture.loopAnimationChannel(animWalkDownLeft);
            }
        } else if (speedX > 0) { // เดินขวา →
            if (texture.getAnimationChannel() != animWalkRight) {
                texture.loopAnimationChannel(animWalkRight);
            }
        } else if (speedX < 0) { // เดินซ้าย ←
            if (texture.getAnimationChannel() != animWalkLeft) {
                texture.loopAnimationChannel(animWalkLeft);
            }
        } else if (speedY > 0) { // เดินลง ↓
            if (texture.getAnimationChannel() != animWalkDown) {
                texture.loopAnimationChannel(animWalkDown);
            }
        } else if (speedY < 0) { // เดินขึ้น ↑
            if (texture.getAnimationChannel() != animWalkUp) {
                texture.loopAnimationChannel(animWalkUp);
            }
        } else { // หยุดเดิน
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }

        // หยุดเมื่อความเร็วต่ำกว่า 1
        if (Math.abs(speedX) < 1 && Math.abs(speedY) < 1) {
            speedX = 0;
            speedY = 0;
            texture.loopAnimationChannel(animIdle);
        }

        if (isDashing) {
            physics.setVelocityX(dashSpeedX);
            physics.setVelocityY(dashSpeedY);
            return; // ข้าม update ปกติ
        }
    }

    public void moveRight() {
        speedX = 150;
        entity.setScaleX(1);
    }

    public void moveLeft() {
        speedX = -150;
        entity.setScaleX(1);
    }

    public void moveUp() {
        speedY = -150;
    }

    public void moveDown() {
        speedY = 150;
    }

    // attack animation////////////////////////////////////////////////////////
    public static void playEffect(Point2D position, Color startColor, Color endColor) {
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(100);
        emitter.setStartColor(startColor);
        emitter.setEndColor(endColor);
        emitter.setSize(5, 10);
        emitter.setBlendMode(BlendMode.SRC_OVER);

        Entity effect = FXGL.entityBuilder()
                .at(position)
                .with(new ParticleComponent(emitter))
                .buildAndAttach();

        FXGL.getGameTimer().runOnceAfter(effect::removeFromWorld, Duration.seconds(0.5));
    }

    public static void playAttackEffect(Point2D position, Color startColor, Color endColor) {
        // ตรวจสอบว่า skill E ทำงานอยู่หรือไม่
        if (FXGL.geto("skillSystem") instanceof SkillSystem skillSystem && skillSystem.isSkillEActive()) {
            // หากสกิล E ทำงานอยู่ ให้ใช้สีที่แตกต่าง
            startColor = Color.RED;  // หรือเลือกสีที่คุณต้องการ
            endColor = Color.ORANGE; // หรือเลือกสีที่คุณต้องการ
        }
    
        ParticleEmitter emitter = ParticleEmitters.newSparkEmitter();
        emitter.setStartColor(startColor);
        emitter.setEndColor(endColor);
        emitter.setSize(5, 10);
        emitter.setBlendMode(BlendMode.ADD);
        emitter.setNumParticles(30);
        emitter.setEmissionRate(0.05);
        emitter.setVelocityFunction(
                i -> new Point2D(Math.random() * 2 - 1, Math.random() * 2 - 1).normalize().multiply(200));
        emitter.setExpireFunction(i -> Duration.seconds(0.3));
    
        Entity effect = FXGL.entityBuilder()
                .at(position.add(40, 0)) // ปรับตำแหน่งให้เอฟเฟกต์ออกมาข้างหน้า
                .with(new ParticleComponent(emitter))
                .buildAndAttach();
    
        FXGL.getGameTimer().runOnceAfter(effect::removeFromWorld, Duration.seconds(0.3));
    }
    
    ////////////////////////////////////////////////////////////////////
    ///

    // ควบคุมตัวละคร
    public boolean up, down, left, right;
    public static boolean controlsRegistered = false; // ✅ ใช้ Flag ป้องกันซ้ำ

    public void updateMovement() {
        int moveSpeed = 150;

        speedX = 0;
        speedY = 0;

        if (up)
            speedY = -moveSpeed;
        if (down)
            speedY = moveSpeed;
        if (left)
            speedX = -moveSpeed;
        if (right)
            speedX = moveSpeed;

        // Normalize movement
        if (speedX != 0 && speedY != 0) {
            speedX *= 0.707; // 🔥 ลดความเร็วเวลาวิ่งเฉียง
            speedY *= 0.707;
        }

        physics.setVelocityX(speedX);
        physics.setVelocityY(speedY);
    }

    private boolean isDashing = false;
    private double dashSpeedX = 0;
    private double dashSpeedY = 0;

    public void dash(double dx, double dy) {
        isDashing = true;
        dashSpeedX = dx;
        dashSpeedY = dy;
        FXGL.getGameTimer().runOnceAfter(() -> isDashing = false, Duration.seconds(0.3));
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            playAttackEffect(entity.getPosition(), Color.DARKBLUE, Color.DARKGREY);
            FXGL.getGameTimer().runOnceAfter(() -> isAttacking = false, Duration.seconds(0.5));
        }
    }

}