package com.example.mygame.GameScene.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.FontManager;
import com.example.mygame.EveryScene.UIManager;
import com.example.mygame.GameScene.Manager.ValueManager;
import com.example.mygame.GameScene.Resorces.GameUIResources;

public class Canvas extends UIManager {

    // -----------------------------
    // UI 설정 구조체들
    // -----------------------------
    private static class UIElementConfig {
        float x, y;
        float scaleX, scaleY;
        float width, height;
        Float r, g, b, a; // null이면 색상 적용 안함

        // 기본 생성자 (스케일만)
        UIElementConfig(float x, float y, float scale) {
            this(x, y, scale, scale, 0, 0);
        }

        // 가로세로 스케일 따로
        UIElementConfig(float x, float y, float scaleX, float scaleY) {
            this(x, y, scaleX, scaleY, 0, 0);
        }

        // 크기 지정
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

        // 색상 설정 메서드
        UIElementConfig withColor(float r, float g, float b, float a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }
    }

    private static class TextConfig {
        float x, y;
        boolean rightAlign;

        TextConfig(float x, float y) {
            this(x, y, false);
        }

        TextConfig(float x, float y, boolean rightAlign) {
            this.x = x;
            this.y = y;
            this.rightAlign = rightAlign;
        }
    }

    private static class HealthBarConfig {
        float x, y, width, height;
        float textLeftPadding, textRightPadding;

        HealthBarConfig(float x, float y, float width, float height,
                        float leftPad, float rightPad) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.textLeftPadding = leftPad;
            this.textRightPadding = rightPad;
        }
    }

    // -----------------------------
    // UI 레이아웃 설정
    // -----------------------------
    private static final class Layout {
        static final UIElementConfig COIN = new UIElementConfig(-1000f, 500f, 7f);
        static final UIElementConfig COIN_PANEL = new UIElementConfig(-1025f, 480f, 500f, 150f).withColor(0.7f, 0.7f, 0.7f, 0.5f);
        static final UIElementConfig WAVE_PANEL = new UIElementConfig(350f, 350f, 1f);

        static final TextConfig COIN_TEXT = new TextConfig(-550f, 585f, true); // 오른쪽 정렬
        static final TextConfig WAVE_TEXT = new TextConfig(400f, 450f);

        static final HealthBarConfig HEALTH_BAR = new HealthBarConfig(
            -50f, 550f,           // x, y
            388f * 3f, 32f * 3f, // width, height
            75f, 100f            // text padding
        );
    }

    // -----------------------------
    // Fields
    // -----------------------------
    private final Slider healthSlider;
    private final Image coin;
    private final Image wavePanel;
    private final Image coinPanel;
    private final Batch batch;
    private final Stage stage;
    private final GlyphLayout glyphLayout;
    private BitmapFont font;

    // -----------------------------
    // Constructor
    // -----------------------------
    public Canvas(CoverViewport viewport, Batch batch) {
        this.batch = batch;
        this.stage = new Stage(viewport);
        this.stage.getCamera().position.set(0, 0, 0);
        this.stage.getCamera().update();
        this.glyphLayout = new GlyphLayout();

        // UI 요소들 초기화
        coin = createImage("sprite/game/ui/icon/coin.png", Layout.COIN);
        coinPanel = createImage("sprite/game/ui/panel/1.png", Layout.COIN_PANEL);
        wavePanel = createImage("sprite/game/ui/panel/1.png", Layout.WAVE_PANEL);
        healthSlider = createHealthSlider();

        // Stage에 추가
        stage.addActor(coinPanel);
        stage.addActor(wavePanel);
        stage.addActor(coin);
        stage.addActor(healthSlider);
    }

    // -----------------------------
    // Image 생성 헬퍼
    // -----------------------------
    private Image createImage(String path, UIElementConfig config) {
        Texture texture = GameUIResources.get(path, Texture.class);
        Image image = new Image(texture);

        // 스케일 적용
        image.setScale(config.scaleX, config.scaleY);

        // 크기가 지정되어 있으면 적용
        if (config.width > 0 && config.height > 0) {
            image.setSize(config.width, config.height);
        }

        // 색상 적용
        if (config.r != null) {
            image.setColor(config.r, config.g, config.b, config.a);
        }

        image.setPosition(config.x, config.y);
        return image;
    }

    // -----------------------------
    // Health Slider 생성
    // -----------------------------
    private Slider createHealthSlider() {
        Texture sliderBg = GameUIResources.get("sprite/game/ui/tower_hp/background.png", Texture.class);
        Texture sliderFill = GameUIResources.get("sprite/game/ui/tower_hp/filled.png", Texture.class);

        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(new TextureRegion(sliderBg));
        TextureRegionDrawable fillDrawable = new TextureRegionDrawable(new TextureRegion(sliderFill));

        bgDrawable.setMinHeight(Layout.HEALTH_BAR.height);
        fillDrawable.setMinHeight(Layout.HEALTH_BAR.height);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = bgDrawable;
        sliderStyle.knob = null;
        sliderStyle.knobBefore = fillDrawable;

        Slider slider = new Slider(0, ValueManager.getMaxTowerHp(), 1, false, sliderStyle);
        slider.setValue(ValueManager.getMaxTowerHp());
        slider.setDisabled(true);
        slider.setPosition(Layout.HEALTH_BAR.x, Layout.HEALTH_BAR.y);
        slider.setSize(Layout.HEALTH_BAR.width, Layout.HEALTH_BAR.height);

        return slider;
    }

    // -----------------------------
    // Render
    // -----------------------------
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.begin();
        if (font == null) {
            font = FontManager.getFont("Galmuri-Bold");
        }

        drawWaveText();
        drawCoinText();
        drawHealthText();

        batch.end();
    }

    // -----------------------------
    // Draw Wave Text
    // -----------------------------
    private void drawWaveText() {
        font.draw(batch, "Wave: " + ValueManager.getWave(),
            Layout.WAVE_TEXT.x, Layout.WAVE_TEXT.y);
    }

    // -----------------------------
    // Draw Coin Text
    // -----------------------------
    private void drawCoinText() {
        String text = "" + ValueManager.getCoin();
        TextConfig config = Layout.COIN_TEXT;

        if (config.rightAlign) {
            glyphLayout.setText(font, text);
            float textWidth = glyphLayout.width;
            font.draw(batch, text, config.x - textWidth, config.y);
        } else {
            font.draw(batch, text, config.x, config.y);
        }
    }

    // -----------------------------
    // Draw Health Text
    // -----------------------------
    private void drawHealthText() {
        String leftText = "HP";
        String rightText = (int) healthSlider.getValue() + " / " + ValueManager.getMaxTowerHp();

        glyphLayout.setText(font, leftText);
        float leftTextHeight = glyphLayout.height;

        glyphLayout.setText(font, rightText);
        float rightTextWidth = glyphLayout.width;

        HealthBarConfig bar = Layout.HEALTH_BAR;
        float barX = healthSlider.getX();
        float barY = healthSlider.getY() + 4;

        float leftTextX = barX + bar.textLeftPadding;
        float leftTextY = barY + bar.height / 2 + leftTextHeight / 2;

        float rightTextX = barX + bar.width - rightTextWidth - bar.textRightPadding;
        float rightTextY = barY + bar.height / 2 + leftTextHeight / 2;

        font.draw(batch, leftText, leftTextX, leftTextY);
        font.draw(batch, rightText, rightTextX, rightTextY);
    }

    // -----------------------------
    // Set Health
    // -----------------------------
    public void setHealth() {
        healthSlider.setValue(ValueManager.getTower_hp());
    }

    // -----------------------------
    // Dispose
    // -----------------------------
    public void dispose() {
        stage.dispose();
    }
}
