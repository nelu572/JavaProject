package com.example.mygame.EveryScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CursorManager {
    private static final Sprite cursor = new Sprite(new Texture("sprite/cursor/2.png"));

    // 커서 크기를 원본 그대로 유지
    static {
        cursor.setSize(cursor.getTexture().getWidth()*0.65f, cursor.getTexture().getHeight()*0.65f);
        cursor.setColor(0.85f, 0.0f, 0.15f, 1.0f);
    }

    // Viewport를 받아서 올바른 좌표 변환
    public static void draw(SpriteBatch batch, Viewport viewport) {
        // Viewport의 unproject로 화면 좌표를 월드 좌표로 변환
        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // 커서를 월드 좌표에 배치 (중앙 정렬)
        float x = mousePos.x - cursor.getWidth() / 2f;
        float y = mousePos.y - cursor.getHeight() / 2f;

        cursor.setPosition(x, y);
        cursor.draw(batch);
    }

    public static Sprite getCursor() {
        return cursor;
    }
}
