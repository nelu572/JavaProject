package com.example.mygame.MainScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.UIManager;

public class MainUI extends UIManager {
    private final Image background;
    private final Image title;
    private final ImageButton StartButton;
    private final ImageButton ExitButton;

    public MainUI(CoverViewport viewport) {
        Texture bg = MainResources.get("sprite/main/img/BG.png", Texture.class);
        Texture titleTex = MainResources.get("sprite/main/img/Title.png", Texture.class);
        Texture startUp = MainResources.get("sprite/main/button/Start_bnt.png", Texture.class);
        Texture startOver = MainResources.get("sprite/main/button/Start_bnt_hover.png", Texture.class);
        Texture exitUp = MainResources.get("sprite/main/button/Exit_bnt.png", Texture.class);
        Texture exitOver = MainResources.get("sprite/main/button/Exit_bnt_hover.png", Texture.class);

        background = new Image(bg);

        float viewportWidth = viewport.getWorldWidth();
        float viewportHeight = viewport.getWorldHeight();
        float bgWidth = bg.getWidth();
        float bgHeight = bg.getHeight();

        // 배경의 비율
        float bgRatio = bgWidth / bgHeight;
        // 뷰포트의 비율
        float viewportRatio = viewportWidth / viewportHeight;

        float scaleX, scaleY;

        if (viewportRatio > bgRatio) {
            // 뷰포트가 더 넓음 → 가로에 맞춤
            scaleX = viewportWidth / bgWidth;
            scaleY = scaleX;
        } else {
            // 뷰포트가 더 좁음 → 세로에 맞춤
            scaleY = viewportHeight / bgHeight;
            scaleX = scaleY;
        }

        background.setScale(scaleX, scaleY);
        background.setPosition(-viewportWidth/2, -viewportHeight/2);

        title = new Image(titleTex);
        title.setPosition(-894 * 1.05f / 2f, 30);

        StartButton = createButton(startUp, startOver);
        StartButton.setSize(450f, 130f);
        StartButton.setPosition(-450/2f*1.05f, -300);

        ExitButton = createButton(exitUp, exitOver);
        ExitButton.setSize(450f, 130f);
        ExitButton.setPosition(-450/2f*1.05f, -500);
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
