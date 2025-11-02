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

    private ArrayList<Monster> monsters;
    private ArrayList<Tower> towers;
    private ArrayList<Bullet> bullets;
    private final Main main;

    public GameScreen(Main main) {
        this.main = main;
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        batch = new SpriteBatch();

        monsters = new ArrayList<>();
        towers = new ArrayList<>();
        bullets = new ArrayList<>();

        // 예시: 몬스터와 타워 초기화
        monsters.add(new Monster(0, 100, 100, 50));
        towers.add(new Tower(400, 300));

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 몬스터 이동
        for (Monster m : monsters) m.update(delta);

        // 타워 공격
        for (Tower t : towers) t.update(delta, monsters, bullets);

        // 총알 업데이트
        ArrayList<Bullet> removeBullets = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update(delta);
            if (b.hasHitTarget()) removeBullets.add(b);
        }
        bullets.removeAll(removeBullets);

        // 그리기
        batch.begin();
        for (Monster m : monsters) m.render(batch);
        for (Tower t : towers) t.render(batch);
        for (Bullet b : bullets) b.render(batch);
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
