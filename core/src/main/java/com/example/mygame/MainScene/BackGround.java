package com.example.mygame.MainScene;

import com.badlogic.gdx.Gdx;
import com.example.mygame.EveryScene.UI;

public class BackGround extends UI {
    private static final float X = (float) Gdx.graphics.getWidth() /2;
    private static final float Y = (float) Gdx.graphics.getHeight() /2;

    public BackGround() {
        super(X, Y, "Img/BG.png");
    }
}
