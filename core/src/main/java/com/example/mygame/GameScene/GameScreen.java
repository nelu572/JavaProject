package com.example.mygame.GameScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;
import com.example.mygame.Main;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Stage backstage;
    private GameUI gameUI;
    private final Main main;

    private Tower tower;
    private Player player;

    public GameScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        GameSpriteResources.init();
        GameSpriteResources.loadAssets();
        GameSpriteResources.finishLoading();

        GameUIResources.init();
        GameUIResources.loadAssets();
        GameUIResources.finishLoading();

        Viewport viewport = new StretchViewport(2560, 1440);
        viewport.apply();
        backstage = new Stage(viewport);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(backstage);

        backstage.getCamera().position.set(0, 0, 0);
        backstage.getCamera().update();

        gameUI = new GameUI(viewport);

        gameUI.drawUI(backstage, gameUI.getBackground());
        gameUI.drawUI(backstage, gameUI.getGround());

        tower = new Tower(1);
        player = new Player(backstage.getCamera());
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(backstage.getCamera().combined);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            // 예: 메인 메뉴로 돌아가기
            main.ChangeScene("Main");
        }

        player.update(delta);

        backstage.act(delta);
        backstage.draw();

        batch.begin();
        tower.render(batch);
        player.render(batch);
        batch.end();
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
        backstage.dispose();
    }
}
