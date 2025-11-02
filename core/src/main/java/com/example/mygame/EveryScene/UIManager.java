package com.example.mygame.EveryScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIManager {

    protected ImageButton createButton(Texture up, Texture over) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(up));
        style.over = new TextureRegionDrawable(new TextureRegion(over));

        return new ImageButton(style);
    }
    public void drawUI(Stage stage, Image image) {
        stage.addActor(image);
    }
    public void drawUI(Stage stage, ImageButton button) {
        stage.addActor(button);
    }
}
