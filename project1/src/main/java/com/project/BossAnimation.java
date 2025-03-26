package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class BossAnimation extends Component {

    private PhysicsComponent physics;
    private int speedX = 0;
    private int speedY = 0;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkRight, animWalkLeft, animWalkUp, animWalkDown;
    private AnimationChannel animWalkUpRight, animWalkUpLeft, animWalkDownRight, animWalkDownLeft;
    private AnimationChannel animAttack;

    public BossAnimation(String image) {
        animIdle = new AnimationChannel(FXGL.image(image), 10, 96, 96, Duration.seconds(1), 0, 9);
        animWalkDown = new AnimationChannel(FXGL.image(image), 16, 96, 96, Duration.seconds(1), 17, 31);
        animWalkLeft = new AnimationChannel(FXGL.image(image), 16, 96, 96, Duration.seconds(1), 17, 31);
        animWalkRight = new AnimationChannel(FXGL.image(image), 16, 96, 96, Duration.seconds(1), 17, 31);
        animWalkUp = new AnimationChannel(FXGL.image(image), 16, 96, 96, Duration.seconds(1), 17, 31);

        animWalkUpRight = animWalkUp;
        animWalkUpLeft = animWalkUp;
        animWalkDownRight = animWalkDown;
        animWalkDownLeft = animWalkDown;

        animAttack = new AnimationChannel(FXGL.image(image), 6, 94, 94, Duration.seconds(1), 10, 16); // Example attack animation

        texture = new AnimatedTexture(animIdle);
        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(64, 64));
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

    public void attack() {
        if (texture.getAnimationChannel() != animAttack) {
            texture.playAnimationChannel(animAttack);
        }
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
}