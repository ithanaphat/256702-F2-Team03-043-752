package com.project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
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
        registerControls();
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

    // ควบคุมตัวละคร
    private boolean up, down, left, right;

    private void registerControls() {
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                right = true;
                updateMovement();
            }

            @Override
            protected void onActionEnd() {
                right = false;
                updateMovement();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                left = true;
                updateMovement();
            }

            @Override
            protected void onActionEnd() {
                left = false;
                updateMovement();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                up = true;
                updateMovement();
            }

            @Override
            protected void onActionEnd() {
                up = false;
                updateMovement();
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                down = true;
                updateMovement();
            }

            @Override
            protected void onActionEnd() {
                down = false;
                updateMovement();
            }
        }, KeyCode.S);
    }

    private void updateMovement() {
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

}