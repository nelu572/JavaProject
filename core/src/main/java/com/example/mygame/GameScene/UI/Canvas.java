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
    // 코인 UI 구조체
    // -----------------------------
    private static class CoinUI {
        Image icon;
        Image panel;
        TextConfig textPos;
    }

    // -----------------------------
    // 웨이브 UI 구조체
    // -----------------------------
    private static class WaveUI {
        Image panel;
        TextConfig textPos;
    }

    // -----------------------------
    // 체력바 UI 구조체
    // -----------------------------
    private static class HealthBarUI {
        Slider slider;
        HealthBarConfig config;
    }

    // -----------------------------
    // UI 레이아웃 설정
    // -----------------------------
    private static final class Layout {
        // 코인
        static final UIElementConfig CoinIcon = new UIElementConfig(-1000f, 500f, 7f);
        static final UIElementConfig CoinPanel = new UIElementConfig(-1025f, 480f, 500f, 150f).withColor(0.7f, 0.7f, 0.7f, 0.5f);
        static final TextConfig CoinText = new TextConfig(-550f, 585f, true);

        // 웨이브
        static final UIElementConfig WavePanel = new UIElementConfig(350f, 350f, 1f);
        static final TextConfig WaveText = new TextConfig(400f, 450f);

        // 체력바
        static final HealthBarConfig HealthBar = new HealthBarConfig(
            -50f, 550f,
            388f * 3f, 32f * 3f,
            75f, 100f
        );
    }

    // -----------------------------
    // Fields
    // -----------------------------
    private final Batch batch;
    private final Stage stage;
    private final GlyphLayout glyphLayout;
    private BitmapFont font;

    private CoinUI coinUI;
    private WaveUI waveUI;
    private HealthBarUI healthBarUI;

    // -----------------------------
    // Constructor
    // -----------------------------
    public Canvas(CoverViewport viewport, Batch batch) {
        this.batch = batch;
        this.stage = new Stage(viewport);
        this.stage.getCamera().position.set(0, 0, 0);
        this.stage.getCamera().update();
        this.glyphLayout = new GlyphLayout();

        // UI 초기화
        initCoinUI();
        initWaveUI();
        initHealthBarUI();

        // Stage에 추가
        stage.addActor(coinUI.panel);
        stage.addActor(coinUI.icon);
        stage.addActor(waveUI.panel);
        stage.addActor(healthBarUI.slider);
    }

    // -----------------------------
    // 코인 UI 초기화
    // -----------------------------
    private void initCoinUI() {
        coinUI = new CoinUI();
        coinUI.icon = createImage("sprite/game/ui/icon/coin.png", Layout.CoinIcon);
        coinUI.panel = createImage("sprite/game/ui/panel/1.png", Layout.CoinPanel);
        coinUI.textPos = Layout.CoinText;
    }

    // -----------------------------
    // 웨이브 UI 초기화
    // -----------------------------
    private void initWaveUI() {
        waveUI = new WaveUI();
        waveUI.panel = createImage("sprite/game/ui/panel/1.png", Layout.WavePanel);
        waveUI.textPos = Layout.WaveText;
    }

    // -----------------------------
    // 체력바 UI 초기화
    // -----------------------------
    private void initHealthBarUI() {
        healthBarUI = new HealthBarUI();
        healthBarUI.config = Layout.HealthBar;

        Texture sliderBg = GameUIResources.get("sprite/game/ui/tower_hp/background.png", Texture.class);
        Texture sliderFill = GameUIResources.get("sprite/game/ui/tower_hp/filled.png", Texture.class);

        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(new TextureRegion(sliderBg));
        TextureRegionDrawable fillDrawable = new TextureRegionDrawable(new TextureRegion(sliderFill));

        bgDrawable.setMinHeight(healthBarUI.config.height);
        fillDrawable.setMinHeight(healthBarUI.config.height);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = bgDrawable;
        sliderStyle.knob = null;
        sliderStyle.knobBefore = fillDrawable;

        healthBarUI.slider = new Slider(0, ValueManager.getMaxTowerHp(), 1, false, sliderStyle);
        healthBarUI.slider.setValue(ValueManager.getTower_hp());
        healthBarUI.slider.setDisabled(true);
        healthBarUI.slider.setPosition(healthBarUI.config.x, healthBarUI.config.y);
        healthBarUI.slider.setSize(healthBarUI.config.width, healthBarUI.config.height);
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
            waveUI.textPos.x, waveUI.textPos.y);
    }

    // -----------------------------
    // Draw Coin Text
    // -----------------------------
    private void drawCoinText() {
        String text = "" + ValueManager.getCoin();
        TextConfig config = coinUI.textPos;

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
        String rightText = ValueManager.getTower_hp() + " / " + ValueManager.getMaxTowerHp();

        glyphLayout.setText(font, leftText);
        float leftTextHeight = glyphLayout.height;

        glyphLayout.setText(font, rightText);
        float rightTextWidth = glyphLayout.width;

        HealthBarConfig bar = healthBarUI.config;
        float barX = healthBarUI.slider.getX();
        float barY = healthBarUI.slider.getY() + 4;

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
        // 최대값 업데이트 (업그레이드로 최대 HP 증가 시 슬라이더 범위도 확장)
        healthBarUI.slider.setRange(0, ValueManager.getMaxTowerHp());
        // 현재값 업데이트
        healthBarUI.slider.setValue(ValueManager.getTower_hp());
    }

    // -----------------------------
    // Dispose
    // -----------------------------
    public void dispose() {
        stage.dispose();
    }
}
