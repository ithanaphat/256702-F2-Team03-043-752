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
    private boolean isStunned = false;
    private double stunTimer = 0;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkRight, animWalkLeft, animWalkUp, animWalkDown;
    private AnimationChannel animWalkUpRight, animWalkUpLeft, animWalkDownRight, animWalkDownLeft;
    private AnimationChannel animAttack;

    public BossAnimation(String image) {
        animIdle = new AnimationChannel(FXGL.image(image), 5, 64, 34, Duration.seconds(1), 7, 22);
        animWalkDown = new AnimationChannel(FXGL.image(image), 5, 64, 34, Duration.seconds(1), 7, 22);
        animWalkLeft = new AnimationChannel(FXGL.image(image), 5, 64, 34, Duration.seconds(1), 7, 22);
        animWalkRight = new AnimationChannel(FXGL.image(image), 5, 64, 34, Duration.seconds(1), 7, 22);
        animWalkUp = new AnimationChannel(FXGL.image(image),5 , 64, 34, Duration.seconds(1), 7, 22);

        animWalkUpRight = animWalkUp;
        animWalkUpLeft = animWalkUp;
        animWalkDownRight = animWalkDown;
        animWalkDownLeft = animWalkDown;

        animAttack = new AnimationChannel(FXGL.image(image), 5, 64, 34, Duration.seconds(0.5), 0, 6); // Example attack animation

        texture = new AnimatedTexture(animIdle);
        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(64, 64));
        entity.getViewComponent().addChild(texture);
        entity.setScaleX(2); // Scale the texture by 2.5
        entity.setScaleY(2); // Scale the texture by 2.5
    }

    @Override
    public void onUpdate(double tpf) {
        if (isStunned) {
            stunTimer -= tpf;
            if (stunTimer <= 0) {
                isStunned = false;
            }
            physics.setVelocityX(0);
            physics.setVelocityY(0);
            if (texture.getAnimationChannel() != animAttack) {
                texture.loopAnimationChannel(animAttack);
            }
            return;
        }

        physics.setVelocityX(speedX);
        physics.setVelocityY(speedY);

        if (speedX == 0 && speedY == 0) {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
            return;
        }

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
            texture.loopAnimationChannel(animAttack);
        }
    }

    public void stun(double duration) {
        isStunned = true;
        stunTimer = duration;
    }

    public boolean isStunned() {
        return isStunned;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
}