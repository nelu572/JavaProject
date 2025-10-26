package com.example.mygame.GameScene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Player player;

    @Override
    public void show() {
        batch = new SpriteBatch();
        player = new Player(100, 100);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update(delta);

        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }

    // 나머지 Screen 인터페이스 메서드
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
