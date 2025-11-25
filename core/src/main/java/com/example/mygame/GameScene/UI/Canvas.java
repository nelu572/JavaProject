package com.example.mygame.GameScene.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.FontManager;
import com.example.mygame.EveryScene.UIManager;
import com.example.mygame.GameScene.Resorces.GameUIResources;

public class Canvas extends UIManager {
    private final Image background;
    private final Batch batch;

    public Canvas(CoverViewport viewport, Batch batch) {
        this.batch = batch;

        Texture Background = GameUIResources.get("sprite/game/ui/img/BG.png", Texture.class);
        background = new Image(Background);
        background.setColor(1.2f, 1.2f, 1.2f, 1f);
        background.setScale(1.7f);
        background.setPosition(-viewport.getWorldWidth()/2f, -viewport.getWorldHeight()/2f);

    }
    public void drawHP() {
        BitmapFont font = FontManager.getFont("Galmuri-Bold");
        font.draw(batch, "HP", 0, 0);
    }
}
