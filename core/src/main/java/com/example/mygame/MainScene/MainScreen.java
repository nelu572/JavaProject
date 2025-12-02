package com.example.mygame.MainScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.CursorManager;
import com.example.mygame.Main;

public class MainScreen implements Screen {
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Stage stage;
    private MainUI mainUI;
    private final Main main;
    private CoverViewport viewport;

    public MainScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        MainResources.loadAssets();
        MainResources.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 2560, 1440); // 기준 월드 크기 설정
        camera.update();

        // FitViewport → CoverViewport로 변경
        viewport = new CoverViewport(2560, 1440, camera);
        viewport.initialize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        stage.getCamera().position.set(0, 0, 0);
        stage.getCamera().update();

        mainUI = new MainUI(viewport);

        mainUI.drawUI(stage, mainUI.getBackGround());
        mainUI.drawUI(stage, mainUI.getTitle());
        mainUI.drawUI(stage, mainUI.getExitButton());
        mainUI.drawUI(stage, mainUI.getStartButton());

        mainUI.getStartButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.ChangeScene("Game");
            }
        });
        mainUI.getExitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        // 여백을 검정색으로 채움
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        CursorManager.draw(batch, viewport);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // CoverViewport 업데이트
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
