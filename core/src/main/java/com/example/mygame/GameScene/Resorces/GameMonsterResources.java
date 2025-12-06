package com.example.mygame.GameScene.Resorces;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameMonsterResources {
    private static AssetManager GameAssetManager;

    public static void init() {
        GameAssetManager = new AssetManager();
    }

    public static void loadAssets() {
        GameAssetManager.load("sprite/game/monster/slime/idle/1.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/1.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/2.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/3.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/4.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/5.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/6.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/7.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/slime/hurt/8.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/1.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/2.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/3.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/4.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/5.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/6.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/7.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/zombie/8.png", Texture.class);
        for (int i = 1; i <= 12; i++) {
            GameAssetManager.load("sprite/game/monster/cyclops/walk/" + i + ".png", Texture.class);
        }
        for (int i = 1; i <= 9; i++) {
            GameAssetManager.load("sprite/game/monster/cyclops/attack/" + i + ".png", Texture.class);
        }
        GameAssetManager.load("sprite/game/monster/cyclops/idle/1.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/cyclops/hurt/1.png", Texture.class);
        GameAssetManager.load("sprite/game/monster/cyclops/rock/1.png", Texture.class);
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
