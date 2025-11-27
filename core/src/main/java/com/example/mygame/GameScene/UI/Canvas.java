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
    // Constants
    private static final float COIN_SCALE = 6f;
    private static final float COIN_X = -500f;
    private static final float COIN_Y = 500f;
    private static final float COIN_TEXT_X = -450f;
    private static final float COIN_TEXT_Y = 550f;

    private static final float HEALTH_BAR_X = 50f;
    private static final float HEALTH_BAR_Y = 550f;
    private static final float TEXTURE_SIZE = 32f;
    private static final float SCALE_FACTOR = 3f;
    private static final float SLIDER_WIDTH = 388f * SCALE_FACTOR;
    private static final float SLIDER_HEIGHT = TEXTURE_SIZE * SCALE_FACTOR;

    private static final float WAVE_TEXT_X = 500f;
    private static final float WAVE_TEXT_Y = 450f;

    private static final float HP_TEXT_PADDING = 75f;
    private static final float HP_TEXT_RIGHT_PADDING = 100f;

    // Fields
    private final Slider healthSlider;
    private final Image coin;
    private final Batch batch;
    private final Stage stage;
    private final GlyphLayout glyphLayout;
    private BitmapFont font;

    public Canvas(CoverViewport viewport, Batch batch) {
        this.batch = batch;
        this.stage = new Stage(viewport);
        this.stage.getCamera().position.set(0, 0, 0);
        this.stage.getCamera().update();
        this.glyphLayout = new GlyphLayout();

        // 코인 아이콘 초기화
        Texture coinTexture = GameUIResources.get("sprite/game/ui/icon/coin.png", Texture.class);
        coin = new Image(coinTexture);
        coin.setScale(COIN_SCALE);
        coin.setPosition(COIN_X, COIN_Y);
        stage.addActor(coin);

        // 체력바 초기화
        healthSlider = createHealthSlider();
        stage.addActor(healthSlider);
    }

    private Slider createHealthSlider() {
        // 이미지 로드
        Texture sliderBg = GameUIResources.get("sprite/game/ui/tower_hp/background.png", Texture.class);
        Texture sliderFill = GameUIResources.get("sprite/game/ui/tower_hp/filled.png", Texture.class);

        // Drawable 생성
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(new TextureRegion(sliderBg));
        TextureRegionDrawable fillDrawable = new TextureRegionDrawable(new TextureRegion(sliderFill));

        bgDrawable.setMinHeight(SLIDER_HEIGHT);
        fillDrawable.setMinHeight(SLIDER_HEIGHT);

        // SliderStyle 생성
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = bgDrawable;
        sliderStyle.knob = null;
        sliderStyle.knobBefore = fillDrawable;

        // Slider 생성
        Slider slider = new Slider(0, ValueManager.getMaxTowerHp(), 1, false, sliderStyle);
        slider.setValue(ValueManager.getMaxTowerHp());
        slider.setDisabled(true);
        slider.setPosition(HEALTH_BAR_X, HEALTH_BAR_Y);
        slider.setSize(SLIDER_WIDTH, SLIDER_HEIGHT);

        return slider;
    }

    /**
     * 모든 UI 요소를 렌더링합니다.
     * 이 메서드를 게임 루프에서 호출하세요.
     */
    public void render() {
        // Stage 업데이트 및 렌더링 (coin과 healthSlider)
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // 텍스트 렌더링
        batch.begin();
        if (font == null) {
            font = FontManager.getFont("Galmuri-Bold");
        }

        drawWaveText();
        drawCoinText();
        drawHealthText();
        batch.end();
    }

    private void drawWaveText() {
        font.draw(batch, "Wave: " + ValueManager.getWave(), WAVE_TEXT_X, WAVE_TEXT_Y);
    }

    private void drawCoinText() {
        font.draw(batch, ": " + ValueManager.getCoin(), COIN_TEXT_X, COIN_TEXT_Y);
    }

    private void drawHealthText() {
        String leftText = "HP";
        String rightText = (int) healthSlider.getValue() + " / " + ValueManager.getMaxTowerHp();

        glyphLayout.setText(font, leftText);
        float leftTextHeight = glyphLayout.height;

        glyphLayout.setText(font, rightText);
        float rightTextWidth = glyphLayout.width;

        float barX = healthSlider.getX();
        float barY = healthSlider.getY() + 4;
        float barWidth = healthSlider.getWidth();
        float barHeight = healthSlider.getHeight();

        float leftTextX = barX + HP_TEXT_PADDING;
        float leftTextY = barY + barHeight / 2 + leftTextHeight / 2;

        float rightTextX = barX + barWidth - rightTextWidth - HP_TEXT_RIGHT_PADDING;
        float rightTextY = barY + barHeight / 2 + leftTextHeight / 2;

        font.draw(batch, leftText, leftTextX, leftTextY);
        font.draw(batch, rightText, rightTextX, rightTextY);
    }

    /**
     * 체력바 값을 설정합니다.
     * @param health 설정할 체력 값 (0 ~ maxTowerHp)
     */
    public void setHealth(int health) {
        int maxHp = ValueManager.getMaxTowerHp();
        healthSlider.setValue(Math.max(0, Math.min(health, maxHp)));
    }

    public void dispose() {
        stage.dispose();
    }
}
