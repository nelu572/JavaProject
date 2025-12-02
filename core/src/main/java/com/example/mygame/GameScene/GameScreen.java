package com.example.mygame.GameScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.CursorManager;
import com.example.mygame.EveryScene.FontManager;
import com.example.mygame.GameScene.Manager.BulletManager;
import com.example.mygame.GameScene.Manager.MyContactListener;
import com.example.mygame.GameScene.Manager.ValueManager;
import com.example.mygame.GameScene.Manager.WaveManager;
import com.example.mygame.GameScene.Object.Ground;
import com.example.mygame.GameScene.Object.Monster.Slime;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;
import com.example.mygame.GameScene.Resorces.GameUIResources;
import com.example.mygame.GameScene.UI.Canvas;
import com.example.mygame.GameScene.UI.ESCMenuCanvas;
import com.example.mygame.GameScene.UI.GameUI;
import com.example.mygame.GameScene.UI.UpgradeCanvas;
import com.example.mygame.Main;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private ESCMenuCanvas escMenuCanvas;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage backstage;
    private GameUI gameUI;
    private final Main main;
    private CoverViewport viewport;

    // Box2D
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private static final float PPM = 100f;

    private Ground ground;
    private Tower tower;
    private Player player;

    private Canvas canvas;
    private UpgradeCanvas upgradeCanvas;

    private boolean wasWaveActive = false; // 이전 프레임의 웨이브 상태

    public GameScreen(Main main) {
        this.main = main;
    }

    private WaveManager waveManager;

    @Override
    public void show() {
        GameSpriteResources.loadAssets();
        GameSpriteResources.finishLoading();

        GameUIResources.loadAssets();
        GameUIResources.finishLoading();

        GameMonsterResources.loadAssets();
        GameMonsterResources.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 2360, 1440);
        camera.update();

        viewport = new CoverViewport(2360, 1440, camera);
        viewport.initialize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        backstage = new Stage(viewport);
        batch = new SpriteBatch();

        backstage.getCamera().position.set(0, 0, 0);
        backstage.getCamera().update();

        // Box2D World 생성 (중력 -9.8)
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new MyContactListener());

        debugRenderer = new Box2DDebugRenderer();

        gameUI = new GameUI(viewport);
        gameUI.drawBackground(backstage);

        canvas = new Canvas(viewport, batch);
        upgradeCanvas = new UpgradeCanvas(viewport, batch);
        escMenuCanvas = new ESCMenuCanvas(viewport, batch, main);

        // 초기 상태는 업그레이드 모드
        Gdx.input.setInputProcessor(upgradeCanvas.getStage());
        wasWaveActive = false;

        waveManager = new WaveManager(world);
        // GameObject들을 Box2D Body와 함께 생성
        ground = new Ground(world);
        tower = new Tower(world, 1);
        player = new Player(world, viewport);
    }

    // -----------------------------
    // 메인 메뉴로 이동
    // -----------------------------
    private void goToMainMenu() {
        // 리소스 정리
        dispose();

        // 메인 메뉴로 이동 (Main 클래스에 메인 메뉴 Screen이 있다고 가정)
        // main.setScreen(main.getMainMenuScreen());

        // 또는 게임 종료
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(backstage.getCamera().combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1/60f, 6, 2);

        // ESC 메뉴가 보이지 않을 때만 ESC 키 처리
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !escMenuCanvas.isVisible()) {
            escMenuCanvas.toggle();
        }

        update(delta);

        rendering();
    }

    private void update(float delta){
        BulletManager.processDestroyQueue();

        boolean isWaveActive = ValueManager.getisWave();

        // ESC 메뉴가 열려있지 않을 때만 게임 로직 업데이트
        if (!escMenuCanvas.isVisible()) {
            // 웨이브 상태가 변경되었을 때만 InputProcessor 전환
            if (isWaveActive != wasWaveActive) {
                if (isWaveActive) {
                    // 웨이브 시작: 플레이어 입력으로 전환
                    Gdx.input.setInputProcessor(backstage);
                } else {
                    // 웨이브 종료: 업그레이드 UI 입력으로 전환
                    Gdx.input.setInputProcessor(upgradeCanvas.getStage());
                }
                wasWaveActive = isWaveActive;
            }

            if(isWaveActive) {
                player.update(delta);
                waveManager.update(delta);
            }
        }

        // backstage는 항상 렌더링 (gameUI 배경 포함)
        backstage.act(delta);
        backstage.draw();
    }

    private void rendering(){
        // 그 다음 게임 오브젝트들 그리기
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        ground.render(batch);
        tower.render(batch);
        player.render(batch);
        if(ValueManager.getisWave()) {
            waveManager.render(batch);
        }
        batch.end();

        // HP바 먼저 그리기 (Stage가 자체 batch 관리)
        canvas.render();

        if(!ValueManager.getisWave()) {
            upgradeCanvas.render();
        }

        // ESC 메뉴는 맨 위에 렌더링
        if (escMenuCanvas.isVisible()) {
            escMenuCanvas.render();
        }

        batch.begin();
        CursorManager.draw(batch, viewport);
        batch.end();

        // 디버그 렌더링
        // debugRenderer.render(world, viewport.getCamera().combined.cpy().scl(PPM));
    }

    @Override
    public void resize(int width, int height) {
        backstage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {
        backstage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        backstage.dispose();
        world.dispose();
        debugRenderer.dispose();
        upgradeCanvas.dispose();
        escMenuCanvas.dispose();
        canvas.dispose();
    }
}
