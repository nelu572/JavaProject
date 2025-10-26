package com.example.mygame.EveryScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UI {
    protected float x, y;
    protected Texture texture;

    public UI(float x, float y, String texturePath) {
        this.x = x;
        this.y = y;
        this.texture = new Texture(texturePath);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }
}
