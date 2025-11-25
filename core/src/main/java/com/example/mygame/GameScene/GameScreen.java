package com.example.mygame.GameScene;

import com.badlogic.gdx.Gdx;
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
import com.example.mygame.GameScene.Object.Ground;
import com.example.mygame.GameScene.Object.Monster.Slime;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;
import com.example.mygame.GameScene.Resorces.GameUIResources;
import com.example.mygame.GameScene.UI.Canvas;
import com.example.mygame.GameScene.UI.GameUI;
import com.example.mygame.Main;

import java.util.ArrayList;

public class GameScreen implements Screen {
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
    private ArrayList<Slime> slimes =  new ArrayList<>();

    private Canvas canvas;
    public GameScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        GameSpriteResources.loadAssets();
        GameSpriteResources.finishLoading();

        GameUIResources.loadAssets();
        GameUIResources.finishLoading();

        GameMonsterResources.loadAssets();
        GameMonsterResources.finishLoading();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 2560, 1440);
        camera.update();

        viewport = new CoverViewport(2560, 1440, camera);
        viewport.initialize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        backstage = new Stage(viewport);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(backstage);

        backstage.getCamera().position.set(0, 0, 0);
        backstage.getCamera().update();

        // Box2D World 생성 (중력 -9.8)
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new MyContactListener());

        debugRenderer = new Box2DDebugRenderer();

        gameUI = new GameUI(viewport);
        gameUI.drawBackground(backstage);

        canvas = new Canvas(viewport,batch);
        // GameObject들을 Box2D Body와 함께 생성
        ground = new Ground(world);
        tower = new Tower(world,1);
        player = new Player(world, viewport);
        slimes.add(new Slime(world));
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(backstage.getCamera().combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            main.ChangeScene("Main");
        }

        world.step(1/60f, 6, 2);

        update(delta);

        rendering();
    }
    private void update(float delta){
        BulletManager.processDestroyQueue();
        player.update(delta);
        for(Slime slime: slimes){
            slime.update(delta);
        }

        backstage.act(delta);
        backstage.draw();
    }
    private void rendering(){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        ground.render(batch);
        tower.render(batch);
        player.render(batch);
        for(Slime slime: slimes){
            slime.render(batch);
        }
        CursorManager.draw(batch, viewport);
        canvas.drawHP();
        batch.end();
        // 디버그 렌더링 (충돌 박스 보기 - 개발 중에만 사용)
        debugRenderer.render(world, viewport.getCamera().combined.cpy().scl(PPM));
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
    }
}
