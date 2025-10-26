package com.example.mygame.MainScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainScreen implements Screen {
    private SpriteBatch batch; // 2D 이미지를 화면에 그리는 도구

    private BackGround backGround;

    @Override
    public void show() {
        batch = new SpriteBatch();

        backGround = new BackGround();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backGround.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backGround.dispose();
    }

    // 나머지 Screen 인터페이스 메서드
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
