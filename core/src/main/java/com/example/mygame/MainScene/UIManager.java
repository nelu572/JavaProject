package com.example.mygame.MainScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UIManager {
    private final Image background;
    private final Image title;
    private final ImageButton StartButton;
    private final ImageButton ExitButton;

    public UIManager(Viewport viewport) {
        background = new Image(new Texture("sprite/ui/img/BG.png"));
        background.setFillParent(true); // 스테이지 전체에 맞게 자동 확장
        background.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(-1280, -720);

        title = new Image(new Texture("sprite/ui/img/Title.png"));
        title.setPosition(-853 / 2f, 30);

        Texture startUp = new Texture("sprite/ui/main button/Start_bnt.png");
        Texture startOver = new Texture("sprite/ui/main button/Start_bnt_hover.png");
        StartButton = createButton(startUp, startOver);
        StartButton.setSize(450f, 130f);
        StartButton.setPosition(-450/2f, -300);

        Texture exitUp = new Texture("sprite/ui/main button/Exit_bnt.png");
        Texture exitOver = new Texture("sprite/ui/main button/Exit_bnt_hover.png");
        ExitButton = createButton(exitUp, exitOver);
        ExitButton.setSize(450f, 130f);
        ExitButton.setPosition(-450/2f, -500);
    }
    private static ImageButton createButton(Texture up, Texture over) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(up));
        style.over = new TextureRegionDrawable(new TextureRegion(over));

        ImageButton button = new ImageButton(style);
        return button;
    }

    public void drawBackground(Stage stage) {
        stage.addActor(background);
    }

    public void drawStartButton(Stage stage) {
        stage.addActor(StartButton);
    }
    public void drawTitle(Stage stage) {
        stage.addActor(title);
    }
    public void drawExitButton(Stage stage) {
        stage.addActor(ExitButton);
    }

    // 🔹 MainScreen에서 버튼을 감지할 수 있도록 getter 제공
    public ImageButton getStartButton() {
        return StartButton;
    }
    public ImageButton getExitButton() {
        return ExitButton;
    }
}
