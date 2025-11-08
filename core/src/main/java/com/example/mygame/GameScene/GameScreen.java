package com.example.mygame.GameScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.CursorManager;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;
import com.example.mygame.Main;

public class GameScreen implements Screen {
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Stage backstage;
    private GameUI gameUI;
    private final Main main;
    private CoverViewport viewport;

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

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 2560, 1440); // 기준 월드 크기 설정
        camera.update();

        // CoverViewport 사용 (비율 유지 + 한쪽 꽉 차면 멈춤 + 여백 검정)
        viewport = new CoverViewport(2560, 1440, camera);
        viewport.initialize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        backstage = new Stage(viewport);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(backstage);

        backstage.getCamera().position.set(0, 0, 0);
        backstage.getCamera().update();

        gameUI = new GameUI(viewport);

        gameUI.drawUI(backstage, gameUI.getBackground());
        gameUI.drawUI(backstage, gameUI.getGround());

        tower = new Tower(1);
        player = new Player(viewport);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(backstage.getCamera().combined);

        // 여백을 검정색으로 채움
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ESC 키로 메인 화면으로 돌아가기
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            main.ChangeScene("Main");
        }

        // 플레이어 업데이트
        player.update(delta);

        // UI 스테이지 업데이트 및 렌더링
        backstage.act(delta);
        backstage.draw();

        // 게임 오브젝트 렌더링
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        tower.render(batch);
        player.render(batch);
        CursorManager.draw(batch, viewport);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // CoverViewport 업데이트 (비율 유지 + 여백 검정)
        backstage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {
        // Alt+Tab 후에도 비율 유지
        backstage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        backstage.dispose();
    }
}
