package com.example.mygame.GameScene.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.FontManager;
import com.example.mygame.EveryScene.UIManager;
import com.example.mygame.GameScene.Manager.ValueManager;
import com.example.mygame.GameScene.Resorces.GameUIResources;

public class UpgradeCanvas extends UIManager {

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

    // -----------------------------
    // 텍스트 설정 구조체
    // -----------------------------
    private static class TextConfig {
        float x, y;
        float scale;

        TextConfig(float x, float y) {
            this(x, y, 1f);
        }

        TextConfig(float x, float y, float scale) {
            this.x = x;
            this.y = y;
            this.scale = scale;
        }
    }

    // -----------------------------
    // 메인 패널 구조체
    // -----------------------------
    private static class MainPanel {
        Image background;
        ImageButton towerButton;
        ImageButton playerButton;
        ImageButton nextWaveButton;
    }

    // -----------------------------
    // 타워 업그레이드 패널 구조체
    // -----------------------------
    private static class TowerUpgradePanel {
        Image background;
        Image panel;
        ImageButton UpgradeButton;
        ImageButton backButton;

        // 텍스트 위치
        TextConfig UpgradeCostPos;
        TextConfig UpgradeLevelPos;
        TextConfig HPIncreasePos;
    }

    // -----------------------------
    // 플레이어 업그레이드 패널 구조체
    // -----------------------------
    private static class PlayerUpgradePanel {
        Image background;
        Image panel;
        ImageButton upgradeButton;
        ImageButton toggleButton;
        ImageButton backButton;

        // 텍스트 위치
        TextConfig costPos;
        TextConfig valuePos;
        TextConfig levelPos;
    }

    // -----------------------------
    // 플레이어 업그레이드 타입 enum
    // -----------------------------
    public enum PlayerUpgradeType {
        ATTACK,
        RELOAD
    }

    // -----------------------------
    // 패널 상태 enum
    // -----------------------------
    public enum PanelState {
        MAIN,
        TOWER_UPGRADE,
        PLAYER_UPGRADE
    }

    // -----------------------------
    // 레이아웃 설정
    // -----------------------------
    private static final class Layout {
        static final UIElementConfig PanelBG = new UIElementConfig(200, -500, 1.3f);

        // 메인 패널
        static final UIElementConfig MainTowerBtn = new UIElementConfig(250f, -100f, 1f);
        static final UIElementConfig MainPlayerBtn = new UIElementConfig(250f, -250f, 1f);
        static final UIElementConfig MainNextBtn = new UIElementConfig(250f, -400f, 1f);

        // 타워 업그레이드 패널
        static final UIElementConfig TowerPanel = new UIElementConfig(250f, -350f, 1f);
        static final UIElementConfig TowerUpgradeBtn = new UIElementConfig(250f, -300f, 0.8f);
        static final UIElementConfig TowerBackBtn = new UIElementConfig(250f, -500f, 0.5f);

        // 타워 패널 텍스트 위치
        static final TextConfig TowerUpgradeCost = new TextConfig(380f, -100f, 0.8f);
        static final TextConfig TowerUpgradeLevel = new TextConfig(290f, 20f, 0.8f);
        static final TextConfig TowerHPIncrease = new TextConfig(550f, -100f, 0.8f);

        // 플레이어 업그레이드 패널
        static final UIElementConfig PlayerPanel = new UIElementConfig(250f, -350f, 1f);
        static final UIElementConfig PlayerUpgradeBtn = new UIElementConfig(250f, -300f, 0.8f);
        static final UIElementConfig PlayerToggleBtn = new UIElementConfig(270f, -465f, 0.8f);
        static final UIElementConfig PlayerBackBtn = new UIElementConfig(350f, -500f, 0.5f);

        // 플레이어 패널 텍스트 위치
        static final TextConfig PlayerCost = new TextConfig(380f, -100f, 0.8f);
        static final TextConfig PlayerValue = new TextConfig(550f, -100f, 0.8f);
        static final TextConfig PlayerLevel = new TextConfig(290f, 20f, 0.8f);
    }

    // -----------------------------
    // Fields
    // -----------------------------
    private final Batch batch;
    private final Stage stage;
    private final GlyphLayout glyphLayout;
    private BitmapFont font;

    private PanelState currentState = PanelState.MAIN;
    private PlayerUpgradeType playerUpgradeType = PlayerUpgradeType.ATTACK;

    private MainPanel mainPanel;
    private TowerUpgradePanel towerPanel;
    private PlayerUpgradePanel playerPanel;

    // -----------------------------
    // Getter for Stage (InputProcessor)
    // -----------------------------
    public Stage getStage() {
        return stage;
    }

    // -----------------------------
    // Constructor
    // -----------------------------
    public UpgradeCanvas(CoverViewport viewport, Batch batch) {
        this.batch = batch;
        this.stage = new Stage(viewport);
        this.stage.getCamera().position.set(0, 0, 0);
        this.stage.getCamera().update();
        this.glyphLayout = new GlyphLayout();

        // 패널들 초기화
        initMainPanel();
        initTowerUpgradePanel();
        initPlayerUpgradePanel();

        // 초기 상태로 시작
        showCurrentPanel();
    }

    // -----------------------------
    // 메인 패널 초기화
    // -----------------------------
    private void initMainPanel() {
        mainPanel = new MainPanel();
        mainPanel.background = createImage("sprite/game/ui/upgrade/main_panel.png", Layout.PanelBG);

        mainPanel.towerButton = createButton("sprite/game/ui/upgrade/button.png", Layout.MainTowerBtn);
        addHoverEffect(mainPanel.towerButton);
        mainPanel.towerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchToPanel(PanelState.TOWER_UPGRADE);
            }
        });

        mainPanel.playerButton = createButton("sprite/game/ui/upgrade/button.png", Layout.MainPlayerBtn);
        addHoverEffect(mainPanel.playerButton);
        mainPanel.playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchToPanel(PanelState.PLAYER_UPGRADE);
            }
        });

        mainPanel.nextWaveButton = createButton("sprite/game/ui/upgrade/button.png", Layout.MainNextBtn);
        addHoverEffect(mainPanel.nextWaveButton);
        mainPanel.nextWaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ValueManager.setisWave(true);
            }
        });
    }

    // -----------------------------
    // 타워 업그레이드 패널 초기화
    // -----------------------------
    private void initTowerUpgradePanel() {
        towerPanel = new TowerUpgradePanel();
        towerPanel.background = createImage("sprite/game/ui/upgrade/tower_panel.png", Layout.PanelBG);
        towerPanel.panel = createImage("sprite/game/ui/upgrade/panel.png", Layout.TowerPanel);

        towerPanel.UpgradeCostPos = Layout.TowerUpgradeCost;
        towerPanel.UpgradeLevelPos = Layout.TowerUpgradeLevel;
        towerPanel.HPIncreasePos = Layout.TowerHPIncrease;

        towerPanel.UpgradeButton = createButton("sprite/game/ui/upgrade/button.png", Layout.TowerUpgradeBtn);
        addHoverEffect(towerPanel.UpgradeButton);
        towerPanel.UpgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onTowerUpgrade();
            }
        });

        towerPanel.backButton = createButton("sprite/game/ui/upgrade/back_button.png", Layout.TowerBackBtn);
        addHoverEffect(towerPanel.backButton);
        towerPanel.backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchToPanel(PanelState.MAIN);
            }
        });
    }

    // -----------------------------
    // 플레이어 업그레이드 패널 초기화
    // -----------------------------
    private void initPlayerUpgradePanel() {
        playerPanel = new PlayerUpgradePanel();
        playerPanel.background = createImage("sprite/game/ui/upgrade/player_panel.png", Layout.PanelBG);
        playerPanel.panel = createImage("sprite/game/ui/upgrade/panel.png", Layout.PlayerPanel);

        playerPanel.costPos = Layout.PlayerCost;
        playerPanel.valuePos = Layout.PlayerValue;
        playerPanel.levelPos = Layout.PlayerLevel;

        // 업그레이드 버튼
        playerPanel.upgradeButton = createButton("sprite/game/ui/upgrade/button.png", Layout.PlayerUpgradeBtn);
        addHoverEffect(playerPanel.upgradeButton);
        playerPanel.upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onPlayerUpgrade();
            }
        });

        // 토글 버튼 (공격력 <-> 재장전 전환)
        playerPanel.toggleButton = createButton("sprite/game/ui/upgrade/toggle_button.png", Layout.PlayerToggleBtn);
        addHoverEffect(playerPanel.toggleButton);
        playerPanel.toggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePlayerUpgradeType();
            }
        });

        // 뒤로 가기 버튼
        playerPanel.backButton = createButton("sprite/game/ui/upgrade/back_button.png", Layout.PlayerBackBtn);
        addHoverEffect(playerPanel.backButton);
        playerPanel.backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchToPanel(PanelState.MAIN);
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
    // 패널 전환
    // -----------------------------
    public void switchToPanel(PanelState newState) {
        hideCurrentPanel();
        currentState = newState;
        showCurrentPanel();
    }

    // -----------------------------
    // 플레이어 업그레이드 타입 토글
    // -----------------------------
    private void togglePlayerUpgradeType() {
        if (playerUpgradeType == PlayerUpgradeType.ATTACK) {
            playerUpgradeType = PlayerUpgradeType.RELOAD;
        } else {
            playerUpgradeType = PlayerUpgradeType.ATTACK;
        }
    }

    // -----------------------------
    // 현재 패널 보이기
    // -----------------------------
    private void showCurrentPanel() {
        switch (currentState) {
            case MAIN:
                stage.addActor(mainPanel.background);
                stage.addActor(mainPanel.towerButton);
                stage.addActor(mainPanel.playerButton);
                stage.addActor(mainPanel.nextWaveButton);
                break;

            case TOWER_UPGRADE:
                stage.addActor(towerPanel.background);
                stage.addActor(towerPanel.panel);
                stage.addActor(towerPanel.UpgradeButton);
                stage.addActor(towerPanel.backButton);
                break;

            case PLAYER_UPGRADE:
                stage.addActor(playerPanel.background);
                stage.addActor(playerPanel.panel);
                stage.addActor(playerPanel.upgradeButton);
                stage.addActor(playerPanel.toggleButton);
                stage.addActor(playerPanel.backButton);
                break;
        }
    }

    // -----------------------------
    // 현재 패널 숨기기
    // -----------------------------
    private void hideCurrentPanel() {
        switch (currentState) {
            case MAIN:
                mainPanel.background.remove();
                mainPanel.towerButton.remove();
                mainPanel.playerButton.remove();
                mainPanel.nextWaveButton.remove();
                break;

            case TOWER_UPGRADE:
                towerPanel.background.remove();
                towerPanel.panel.remove();
                towerPanel.UpgradeButton.remove();
                towerPanel.backButton.remove();
                break;

            case PLAYER_UPGRADE:
                playerPanel.background.remove();
                playerPanel.panel.remove();
                playerPanel.upgradeButton.remove();
                playerPanel.toggleButton.remove();
                playerPanel.backButton.remove();
                break;
        }
    }

    // -----------------------------
    // 업그레이드 콜백들
    // -----------------------------
    private void onTowerUpgrade() {
        boolean success = ValueManager.upgradeTower();
        if (!success && ValueManager.isTowerMaxLevel()) {
            System.out.println("타워가 최대 레벨입니다!");
        } else if (!success) {
            System.out.println("코인이 부족합니다!");
        }
    }

    private void onPlayerUpgrade() {
        boolean success;
        if (playerUpgradeType == PlayerUpgradeType.ATTACK) {
            success = ValueManager.upgradePlayerAttack();
            if (!success && ValueManager.isPlayerAttackMaxLevel()) {
                System.out.println("공격력이 최대 레벨입니다!");
            } else if (!success) {
                System.out.println("코인이 부족합니다!");
            }
        } else {
            success = ValueManager.upgradePlayerReload();
            if (!success && ValueManager.isPlayerReloadMaxLevel()) {
                System.out.println("재장전이 최대 레벨입니다!");
            } else if (!success) {
                System.out.println("코인이 부족합니다!");
            }
        }
    }

    // -----------------------------
    // Render
    // -----------------------------
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.begin();
        if (font == null) {
            font = FontManager.getFont("Galmuri-9");
        }

        switch (currentState) {
            case MAIN:
                break;
            case TOWER_UPGRADE:
                renderTowerUpgradeText();
                break;
            case PLAYER_UPGRADE:
                renderPlayerUpgradeText();
                break;
        }

        batch.end();
    }

    // -----------------------------
    // 타워 업그레이드 텍스트 렌더링
    // -----------------------------
    private void renderTowerUpgradeText() {
        // 레벨 텍스트
        font.getData().setScale(towerPanel.UpgradeLevelPos.scale);
        String levelText = "Lv " + ValueManager.getTowerLevel() + "/" + ValueManager.getMaxTowerLevel();
        font.draw(batch, levelText, towerPanel.UpgradeLevelPos.x, towerPanel.UpgradeLevelPos.y);

        // 비용 텍스트 (최대 레벨이면 MAX 표시)
        font.getData().setScale(towerPanel.UpgradeCostPos.scale);
        if (ValueManager.isTowerMaxLevel()) {
            font.setColor(Color.GOLD);
            font.draw(batch, "MAX", towerPanel.UpgradeCostPos.x, towerPanel.UpgradeCostPos.y);
            font.setColor(Color.WHITE);
        } else {
            // 코인이 부족하면 빨간색으로 표시
            if (ValueManager.getCoin() < ValueManager.getTowerUpgradeCost()) {
                font.setColor(Color.RED);
            }
            font.draw(batch, "" + ValueManager.getTowerUpgradeCost(),
                towerPanel.UpgradeCostPos.x, towerPanel.UpgradeCostPos.y);
            font.setColor(Color.WHITE);
        }

        // 체력 증가량 표시 (최대 레벨이 아닐 때만)
        if (!ValueManager.isTowerMaxLevel()) {
            font.getData().setScale(towerPanel.HPIncreasePos.scale);
            font.setColor(Color.GREEN);
            font.draw(batch, "+HP " + ValueManager.getTowerHPIncrease(),
                towerPanel.HPIncreasePos.x, towerPanel.HPIncreasePos.y);
            font.setColor(Color.WHITE);
        }

        // 스케일 원래대로
        font.getData().setScale(1f);
    }

    // -----------------------------
    // 플레이어 업그레이드 텍스트 렌더링
    // -----------------------------
    private void renderPlayerUpgradeText() {
        if (playerUpgradeType == PlayerUpgradeType.ATTACK) {
            // 공격력 레벨
            font.getData().setScale(playerPanel.levelPos.scale);
            String levelText = "Lv " + ValueManager.getPlayerAttackLevel() + "/" + ValueManager.getMaxPlayerAttackLevel();
            font.draw(batch, levelText, playerPanel.levelPos.x, playerPanel.levelPos.y);

            // 공격력 업그레이드 비용
            font.getData().setScale(playerPanel.costPos.scale);
            if (ValueManager.isPlayerAttackMaxLevel()) {
                font.setColor(Color.GOLD);
                font.draw(batch, "MAX", playerPanel.costPos.x, playerPanel.costPos.y);
                font.setColor(Color.WHITE);
            } else {
                if (ValueManager.getCoin() < ValueManager.getPlayerAttackUpgradeCost()) {
                    font.setColor(Color.RED);
                }
                font.draw(batch, "" + ValueManager.getPlayerAttackUpgradeCost(),
                    playerPanel.costPos.x, playerPanel.costPos.y);
                font.setColor(Color.WHITE);
            }

            // 증가량
            font.getData().setScale(playerPanel.valuePos.scale);
            if (!ValueManager.isPlayerAttackMaxLevel()) {
                font.draw(batch, "+" + ValueManager.getPlayerAttackIncrease(),
                    playerPanel.valuePos.x, playerPanel.valuePos.y);
            }
        } else {
            // 재장전 레벨
            font.getData().setScale(playerPanel.levelPos.scale);
            String levelText = "Lv " + ValueManager.getPlayerReloadLevel() + "/" + ValueManager.getMaxPlayerReloadLevel();
            font.draw(batch, levelText, playerPanel.levelPos.x, playerPanel.levelPos.y);

            // 재장전 업그레이드 비용
            font.getData().setScale(playerPanel.costPos.scale);
            if (ValueManager.isPlayerReloadMaxLevel()) {
                font.setColor(Color.GOLD);
                font.draw(batch, "MAX", playerPanel.costPos.x, playerPanel.costPos.y);
                font.setColor(Color.WHITE);
            } else {
                if (ValueManager.getCoin() < ValueManager.getPlayerReloadUpgradeCost()) {
                    font.setColor(Color.RED);
                }
                font.draw(batch, "" + ValueManager.getPlayerReloadUpgradeCost(),
                    playerPanel.costPos.x, playerPanel.costPos.y);
                font.setColor(Color.WHITE);
            }

            // 감소량
            font.getData().setScale(playerPanel.valuePos.scale);
            if (!ValueManager.isPlayerReloadMaxLevel()) {
                font.draw(batch, "-" + ValueManager.getPlayerReloadDecrease() + "초",
                    playerPanel.valuePos.x, playerPanel.valuePos.y);
            }
        }

        // 스케일 원래대로
        font.getData().setScale(1f);
    }

    public void dispose() {
        stage.dispose();
    }
}
