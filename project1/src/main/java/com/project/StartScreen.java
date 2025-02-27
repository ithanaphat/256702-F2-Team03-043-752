package com.project;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StartScreen extends FXGLMenu {

    public StartScreen() {
        super(MenuType.MAIN_MENU);

        Text title = FXGL.getUIFactoryService().newText("The Last Adventurer", 36);
        Button btnStart = FXGL.getUIFactoryService().newButton("Start Game");
        btnStart.setOnAction(e -> fireNewGame());

        Button btnExit = FXGL.getUIFactoryService().newButton("Exit");
        btnExit.setOnAction(e -> fireExit());

        VBox menuBox = new VBox(10, title, btnStart, btnExit);
        menuBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 100);

        getContentRoot().getChildren().add(menuBox);
    }
}