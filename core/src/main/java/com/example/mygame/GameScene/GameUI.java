package com.example.mygame.GameScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.UIManager;

public class GameUI extends UIManager {
    private final Image background;

    public GameUI(CoverViewport viewport) {

        Texture Background = GameUIResources.get("sprite/game/ui/img/BG.png", Texture.class);
        background = new Image(Background);
        background.setColor(1.2f, 1.2f, 1.2f, 1f);
        background.setScale(1.7f);
        background.setPosition(-viewport.getWorldWidth()/2f, -viewport.getWorldHeight()/2f);
    }
    public Image getBackground() {
        return background;
    }
}
