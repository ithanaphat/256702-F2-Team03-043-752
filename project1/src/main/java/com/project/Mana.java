package com.project;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Mana extends Component {
    private IntegerProperty mana;

    public Mana(int initialMana) {
        this.mana = new SimpleIntegerProperty(initialMana);
    }

    public int getMana() {
        return mana.get();
    }

    public void setMana(int mana) {
        this.mana.set(mana);
    }

    public IntegerProperty manaProperty() {
        return mana;
    }
}
