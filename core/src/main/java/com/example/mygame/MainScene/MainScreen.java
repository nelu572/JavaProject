package com.example.mygame.MainScene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainScreen implements Screen {
    private SpriteBatch batch; // 2D 이미지를 화면에 그리는 도구
    private Stage stage;

    @Override
    public void show() {

        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Stage가 입력 감지

        //배경 이미지
        Image background = new Image(new Texture("sprite/ui/img/BG.png"));
        background.setFillParent(true);
        stage.addActor(background);

        //버튼
         ImageButton button = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(new Texture("sprite/ui/main button/btn.png"))),
            new TextureRegionDrawable(new TextureRegion(new Texture("sprite/ui/img/BG.png")))
        );
         button.setScale(100f);
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        batch.begin();

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    // 나머지 Screen 인터페이스 메서드
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
