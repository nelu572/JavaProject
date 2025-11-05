package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.GameSpriteResources;

public class Player extends GameObject {
    private Gun gun;

    public Player(Camera camera) {
        super(GameSpriteResources.get("sprite/game/player/idle1.png", Texture.class));

        // 위치 및 크기 설정
        super.setPosition(-830, -340);
        Texture playerTexture = GameSpriteResources.get("sprite/game/player/idle1.png", Texture.class);
        super.setSize(playerTexture.getWidth() * 6f, playerTexture.getHeight() * 6f);

        // Gun 생성
        gun = new Gun(this, camera);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        gun.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        boolean flipped = gun.isFilp();  // 🔹 총이 플립 상태인지 확인

        float drawX = getX();
        float drawY = getY();
        float width = getWidth();
        float height = getHeight();

        // 🔹 X축 기준으로 뒤집히면 기준점을 오른쪽으로 이동
        if (flipped) {
            drawX += width;   // 오른쪽 기준으로 반전
            width = -width;   // X스케일 반전
        }

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
