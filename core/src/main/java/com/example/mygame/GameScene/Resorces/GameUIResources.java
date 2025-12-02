package com.example.mygame.GameScene.Resorces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameUIResources {
    private static AssetManager GameAssetManager;

    public static void init() {
        GameAssetManager = new AssetManager();
    }

    public static void loadAssets() {
        GameAssetManager.load("sprite/game/ui/img/BG.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/tower_hp/background.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/tower_hp/filled.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/icon/coin.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/panel/1.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/main_panel.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/button.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/back_button.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/player_panel.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/tower_panel.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/panel.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/upgrade/toggle_button.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/set/main_menu.png", Texture.class);
        GameAssetManager.load("sprite/game/ui/set/resume.png", Texture.class);

    }

    public static void unloadAssets() { // 추가
        unloadAsset("sprite/game/ui/img/BG.png");
        unloadAsset("sprite/game/ui/tower_hp/background.png");
        unloadAsset("sprite/game/ui/tower_hp/filled.png");
        unloadAsset("sprite/game/ui/icon/coin.png");
        unloadAsset("sprite/game/ui/panel/1.png");
        unloadAsset("sprite/game/ui/upgrade/main_panel.png");
        unloadAsset("sprite/game/ui/upgrade/button.png");
        unloadAsset("sprite/game/ui/upgrade/back_button.png");
        unloadAsset("sprite/game/ui/upgrade/player_panel.png");
        unloadAsset("sprite/game/ui/upgrade/tower_panel.png");
        unloadAsset("sprite/game/ui/upgrade/panel.png");
        unloadAsset("sprite/game/ui/upgrade/toggle_button.png");
        unloadAsset("sprite/game/ui/set/main_menu.png");
        unloadAsset("sprite/game/ui/set/resume.png");
    }
    private static void unloadAsset(String name) {
        if (GameAssetManager.isLoaded(name)) GameAssetManager.unload(name);
    }

    public static void finishLoading() {
        GameAssetManager.finishLoading();
    }

    public static <T> T get(String path, Class<T> type) {
        return GameAssetManager.get(path, type);
    }

    // 🔹 게임 종료 시 전체 dispose
    public static void dispose() {
        if (GameAssetManager != null) {
            GameAssetManager.dispose();
        }
    }
}
