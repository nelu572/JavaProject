package com.example.mygame.GameScene.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.UIManager;
import com.example.mygame.GameScene.Resorces.GameUIResources;

public class GameUI extends UIManager {
    private final Image background;

    public GameUI(CoverViewport viewport) {
        Texture Background = GameUIResources.get("sprite/game/ui/img/BG.png", Texture.class);
        background = new Image(Background);
        background.setColor(1.2f, 1.2f, 1.2f, 1f);
        background.setScale(1.7f);
        background.setPosition(-viewport.getWorldWidth()/2f, -viewport.getWorldHeight()/2f);
    }
    public void drawBackground(Stage backStage) {
        super.drawUI(backStage, background);
    }
}
