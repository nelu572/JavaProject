package com.example.mygame.GameScene.Resorces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameMonsterResources {
    private static AssetManager GameAssetManager;

    public static void init() {
        GameAssetManager = new AssetManager();
    }

    public static void loadAssets() {
        GameAssetManager.load("sprite/game/monster/slime/jump/start/1.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/2.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/3.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/4.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/5.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/6.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/7.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/8.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/jump/start/9.png", Texture.class);
    }

    public static void unloadAssets() {
        unloadAsset("sprite/game/monster/slime/jump/start/1.png");
        unloadAsset("sprite/game/monster/slime/jump/start/2.png");
        unloadAsset("sprite/game/monster/slime/jump/start/3.png");
        unloadAsset("sprite/game/monster/slime/jump/start/4.png");
        unloadAsset("sprite/game/monster/slime/jump/start/5.png");
        unloadAsset("sprite/game/monster/slime/jump/start/6.png");
        unloadAsset("sprite/game/monster/slime/jump/start/7.png");
        unloadAsset("sprite/game/monster/slime/jump/start/8.png");
        unloadAsset("sprite/game/monster/slime/jump/start/9.png");
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
