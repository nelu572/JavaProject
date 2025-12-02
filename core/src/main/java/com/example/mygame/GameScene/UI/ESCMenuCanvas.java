package com.example.mygame.GameScene.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.UIManager;
import com.example.mygame.GameScene.Resorces.GameUIResources;
import com.example.mygame.Main;

public class ESCMenuCanvas extends UIManager {

    // -----------------------------
    // UI 설정 구조체
    // -----------------------------
    private static class UIElementConfig {
        float x, y;
        float scaleX, scaleY;
        float width, height;
        Float r, g, b, a;

        UIElementConfig(float x, float y, float scale) {
            this(x, y, scale, scale, 0, 0);
        }

        UIElementConfig(float x, float y, float scaleX, float scaleY) {
            this(x, y, scaleX, scaleY, 0, 0);
        }

        UIElementConfig(float x, float y, float scaleX, float scaleY,
                        float width, float height) {
            this.x = x;
            this.y = y;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.width = width;
            this.height = height;
            this.r = null;
            this.g = null;
            this.b = null;
            this.a = null;
        }

        UIElementConfig withColor(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }
    }

    // -----------------------------
    // ESC 메뉴 구조체
    // -----------------------------
    private static class ESCMenu {
        Image backgroundPanel;
        Image menuPanel;
        ImageButton resumeButton;
        ImageButton mainMenuButton;
    }

    // -----------------------------
    // 레이아웃 설정
    // -----------------------------
    private static final class Layout {
        static final UIElementConfig Background = new UIElementConfig(-1180f, -720f, 2360f, 1440f).withColor(0f, 0f, 0f, 0.7f);
        static final UIElementConfig ResumeBtn = new UIElementConfig(-399f, -100f, 0.75f);
        static final UIElementConfig MainMenuBtn = new UIElementConfig(-399f, -350f, 0.75f);
    }

    // -----------------------------
    // Fields
    // -----------------------------
    private final Stage stage;
    private final Batch batch;
    private ESCMenu escMenu;
    private boolean isVisible;
    private InputProcessor previousInputProcessor; // 이전 InputProcessor 저장
    private Main main;


    // -----------------------------
    // Constructor - Main 인스턴스를 받도록 수정
    // -----------------------------
    public ESCMenuCanvas(CoverViewport viewport, Batch batch, Main main) {
        this.batch = batch;
        this.stage = new Stage(viewport);
        this.stage.getCamera().position.set(0, 0, 0);
        this.stage.getCamera().update();
        this.isVisible = false;
        this.main = main;  // 전달받은 Main 인스턴스 사용
        initESCMenu();
    }

    // -----------------------------
    // ESC 메뉴 초기화
    // -----------------------------
    private void initESCMenu() {
        escMenu = new ESCMenu();

        // 반투명 배경
        escMenu.backgroundPanel = createImage("sprite/game/ui/panel/1.png", Layout.Background);

        // 재개 버튼
        escMenu.resumeButton = createButton("sprite/game/ui/set/resume.png", Layout.ResumeBtn);
        addHoverEffect(escMenu.resumeButton);
        final ESCMenuCanvas self = this;
        escMenu.resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                self.hide();
            }
        });

        // 메인 메뉴 버튼
        escMenu.mainMenuButton = createButton("sprite/game/ui/set/main_menu.png", Layout.MainMenuBtn);
        addHoverEffect(escMenu.mainMenuButton);
        escMenu.mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.ChangeScene("Main");  // 이제 올바른 Main 인스턴스로 장면 전환
            }
        });
    }

    // -----------------------------
    // Hover 효과 추가
    // -----------------------------
    private void addHoverEffect(final ImageButton button) {
        final float originalScaleX = button.getScaleX();
        final float originalScaleY = button.getScaleY();

        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                if (pointer == -1) {
                    button.setScale(originalScaleX * 1.05f, originalScaleY * 1.05f);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                if (pointer == -1) {
                    button.setScale(originalScaleX, originalScaleY);
                }
            }
        });
    }

    // -----------------------------
    // Image 생성 헬퍼
    // -----------------------------
    private Image createImage(String path, UIElementConfig config) {
        Texture texture = GameUIResources.get(path, Texture.class);
        Image image = new Image(texture);

        image.setScale(config.scaleX, config.scaleY);

        if (config.width > 0 && config.height > 0) {
            image.setSize(config.width, config.height);
        }

        if (config.r != null) {
            image.setColor(config.r, config.g, config.b, config.a);
        }

        image.setPosition(config.x, config.y);
        return image;
    }

    // -----------------------------
    // Button 생성 헬퍼
    // -----------------------------
    private ImageButton createButton(String path, UIElementConfig config) {
        Texture texture = GameUIResources.get(path, Texture.class);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(drawable);

        button.setSize(texture.getWidth(), texture.getHeight());
        button.setTransform(true);
        button.setOrigin(texture.getWidth() / 2f, texture.getHeight() / 2f);
        button.setScale(config.scaleX, config.scaleY);
        button.setPosition(config.x, config.y);

        return button;
    }

    // -----------------------------
    // Show
    // -----------------------------
    public void show() {
        if (!isVisible) {
            isVisible = true;

            // 현재 InputProcessor 저장
            previousInputProcessor = Gdx.input.getInputProcessor();

            if (escMenu.backgroundPanel != null) stage.addActor(escMenu.backgroundPanel);
            if (escMenu.menuPanel != null) stage.addActor(escMenu.menuPanel);
            if (escMenu.resumeButton != null) stage.addActor(escMenu.resumeButton);
            if (escMenu.mainMenuButton != null) stage.addActor(escMenu.mainMenuButton);

            // InputProcessor를 이 Stage로 변경
            Gdx.input.setInputProcessor(stage);
        }
    }

    // -----------------------------
    // Hide
    // -----------------------------
    public void hide() {
        if (isVisible) {
            isVisible = false;
            if (escMenu.backgroundPanel != null) escMenu.backgroundPanel.remove();
            if (escMenu.menuPanel != null) escMenu.menuPanel.remove();
            if (escMenu.resumeButton != null) escMenu.resumeButton.remove();
            if (escMenu.mainMenuButton != null) escMenu.mainMenuButton.remove();

            // 이전 InputProcessor 복원
            if (previousInputProcessor != null) {
                Gdx.input.setInputProcessor(previousInputProcessor);
                previousInputProcessor = null;
            }
        }
    }

    // -----------------------------
    // Toggle
    // -----------------------------
    public void toggle() {
        if (isVisible) {
            hide();
        } else {
            show();
        }
    }

    // -----------------------------
    // Getter
    // -----------------------------
    public Stage getStage() {
        return stage;
    }

    public boolean isVisible() {
        return isVisible;
    }

    // -----------------------------
    // Render
    // -----------------------------
    public void render() {
        if (isVisible) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    // -----------------------------
    // Dispose
    // -----------------------------
    public void dispose() {
        stage.dispose();
    }
}
