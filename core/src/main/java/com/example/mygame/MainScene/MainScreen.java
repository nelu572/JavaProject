package com.example.mygame.MainScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.example.mygame.Main;

public class MainScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private MainUI mainUI;
    private final Main main;

    public MainScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        MainResourceManager.init();
        MainResourceManager.loadMainUIAssets();
        MainResourceManager.finishLoading();

        Viewport viewport = new FillViewport(2560, 1440);
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        stage.getCamera().position.set(0, 0, 0);
        stage.getCamera().update();

        mainUI = new MainUI(viewport);

        mainUI.drawUI(stage,mainUI.getBackGround());
        mainUI.drawUI(stage,mainUI.getTitle());
        mainUI.drawUI(stage,mainUI.getExitButton());
        mainUI.drawUI(stage,mainUI.getStartButton());

        mainUI.getStartButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("UI", "게임 화면으로 전환");
                main.ChangeScene("Game"); // 🔹 Main에 요청
            }
        });
        mainUI.getExitButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
