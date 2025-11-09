package com.example.mygame.GameScene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameSpriteResources {
    private static AssetManager GameAssetManager;

    public static void init() {
        GameAssetManager = new AssetManager();
    }

    public static void loadAssets() {
        GameAssetManager.load("sprite/game/img/ground.png", Texture.class);
        GameAssetManager.load("sprite/game/tower/LV1.png", Texture.class);
        GameAssetManager.load("sprite/game/tower/LV2.png", Texture.class);
        GameAssetManager.load("sprite/game/player/idle1.png", Texture.class);
        GameAssetManager.load("sprite/game/player/idle2.png", Texture.class);
        GameAssetManager.load("sprite/game/gun/M92.png", Texture.class);
    }

    public static void unloadAssets() {
        unloadAsset("sprite/game/img/ground.png");
        unloadAsset("sprite/game/tower/LV1.png");
        unloadAsset("sprite/game/tower/LV2.png");
        unloadAsset("sprite/game/player/idle1.png");
        unloadAsset("sprite/game/player/idle2.png");
        unloadAsset("sprite/game/gun/M92.png");
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
