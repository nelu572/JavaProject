package com.example.mygame.MainScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.mygame.EveryScene.UIManager;

public class MainUI extends UIManager {
    private final Image background;
    private final Image title;
    private final ImageButton StartButton;
    private final ImageButton ExitButton;

    public MainUI(Viewport viewport) {
        Texture bg = MainResources.get("sprite/main/img/BG.png", Texture.class);
        Texture titleTex = MainResources.get("sprite/main/img/Title.png", Texture.class);
        Texture startUp = MainResources.get("sprite/main/button/Start_bnt.png", Texture.class);
        Texture startOver = MainResources.get("sprite/main/button/Start_bnt_hover.png", Texture.class);
        Texture exitUp = MainResources.get("sprite/main/button/Exit_bnt.png", Texture.class);
        Texture exitOver = MainResources.get("sprite/main/button/Exit_bnt_hover.png", Texture.class);

        background = new Image(bg);
        background.setFillParent(true); // 스테이지 전체에 맞게 자동 확장
        background.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(-1280, -720);

        title = new Image(titleTex);
        title.setPosition(-853 / 2f, 30);

        StartButton = createButton(startUp, startOver);
        StartButton.setSize(450f, 130f);
        StartButton.setPosition(-450/2f, -300);

        ExitButton = createButton(exitUp, exitOver);
        ExitButton.setSize(450f, 130f);
        ExitButton.setPosition(-450/2f, -500);
    }

    public ImageButton getStartButton() {
        return StartButton;
    }
    public ImageButton getExitButton() {
        return ExitButton;
    }
    public Image getBackGround() {
        return background;
    }
    public Image getTitle() {
        return title;
    }
}
