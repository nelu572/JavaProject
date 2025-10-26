package com.example.mygame.GameScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.example.mygame.EveryScene.GameObject;

public class Player extends GameObject {
    private final float MAXSPEED = 200f;

    public Player(float x, float y) {
        super(x, y, "player.png");
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  x -= MAXSPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += MAXSPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    y += MAXSPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  y -= MAXSPEED * delta;
    }
}
