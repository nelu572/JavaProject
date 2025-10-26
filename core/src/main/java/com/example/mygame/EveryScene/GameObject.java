package com.example.mygame.EveryScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

abstract public class GameObject {
    protected float x, y;
    protected Texture texture;

    public GameObject(float x, float y, String texturePath) {
        this.x = x;
        this.y = y;
        this.texture = new Texture(texturePath);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public abstract void update(float delta);

    public void dispose() {
        texture.dispose();
    }
}
