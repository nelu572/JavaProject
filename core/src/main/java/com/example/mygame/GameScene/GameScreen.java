package com.example.mygame.GameScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.mygame.Main;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private Viewport viewport;

    private final Main main;

    public GameScreen(Main main) {
        this.main = main;
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        batch = new SpriteBatch();


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 몬스터 이동

        // 그리기
        batch.begin();
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
