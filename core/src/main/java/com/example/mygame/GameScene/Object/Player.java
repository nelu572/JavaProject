package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.GameSpriteResources;

public class Player extends GameObject {
    private Gun gun;
    private CoverViewport viewport;

    public Player(CoverViewport viewport) {
        super(GameSpriteResources.get("sprite/game/player/idle1.png", Texture.class));
        this.viewport = viewport;
        // 위치 및 크기 설정
        super.setPosition(-830, -340);
        Texture playerTexture = GameSpriteResources.get("sprite/game/player/idle1.png", Texture.class);
        super.setSize(playerTexture.getWidth() * 6f, playerTexture.getHeight() * 6f);

        // Gun 생성
        gun = new Gun(this, viewport);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        gun.update(delta, viewport);
    }

    @Override
    public void render(SpriteBatch batch) {

        float drawX = getX();
        float drawY = getY();
        float width = getWidth();
        float height = getHeight();

        // 🔹 플레이어 렌더
        batch.draw(
            getTexture(),
            drawX, drawY,
            width, height
        );

        // 🔹 총 렌더 (플레이어 위)
        gun.render(batch);
    }

    public Gun getGun() {
        return gun;
    }
}
