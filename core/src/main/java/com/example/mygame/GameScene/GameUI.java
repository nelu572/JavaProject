package com.example.mygame.GameScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.mygame.EveryScene.UIManager;

public class GameUI extends UIManager {
    private final Image ground;
    private final Image background;

    public GameUI(Viewport viewport) {
        Texture Ground = GameUIResources.get("sprite/game/ui/img/ground.png", Texture.class);

        ground = new Image(Ground);
        ground.setPosition(-viewport.getWorldWidth()/2f, -viewport.getWorldHeight()/2f);

        Texture Background = GameUIResources.get("sprite/game/ui/img/BG.png", Texture.class);
        background = new Image(Background);
        background.setColor(1f, 1f, 1f, 0.75f);
        background.setScale(1.7f);
        background.setPosition(-viewport.getWorldWidth()/2f, -viewport.getWorldHeight()/2f);
    }
    public Image getGround() {
        return ground;
    }
    public Image getBackground() {
        return background;
    }
}
