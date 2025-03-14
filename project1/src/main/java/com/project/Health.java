package com.project;

import com.almasb.fxgl.entity.component.Component;

public class Health extends Component {
    private int health;

    public Health(int initialHealth) {
        this.health = initialHealth;
    }

    public int getHealth() {
        return health;
    }

    public void damage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0; // ป้องกันค่า HP ติดลบ
        }
    }

    public void heal(int amount) {
        health += amount;
        System.out.println("Current Health: " + health);
    }
}
