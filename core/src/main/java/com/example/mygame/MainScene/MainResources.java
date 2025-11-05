package com.example.mygame.MainScene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainResources {
    private static AssetManager MainAssetManager;

    public static void init() {
        MainAssetManager = new AssetManager();
    }

    // 🔹 Main 씬 전용 로딩
    public static void loadAssets() {
        MainAssetManager.load("sprite/main/img/BG.png", Texture.class);
        MainAssetManager.load("sprite/main/img/Title.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Start_bnt.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Start_bnt_hover.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Exit_bnt.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Exit_bnt_hover.png", Texture.class);
    }

    // 🔹 Main 씬 리소스만 언로드
    public static void unloadAssets() { // 추가
        unloadAsset("sprite/main/img/BG.png");
        unloadAsset("sprite/main/img/Title.png");
        unloadAsset("sprite/main/button/Start_bnt.png");
        unloadAsset("sprite/main/button/Start_bnt_hover.png");
        unloadAsset("sprite/main/button/Exit_bnt.png");
        unloadAsset("sprite/main/button/Exit_bnt_hover.png");
    }
    private static void unloadAsset(String name) {
        if (MainAssetManager.isLoaded(name)) MainAssetManager.unload(name);
    }
    public static void finishLoading() {
        MainAssetManager.finishLoading();
    }

    public static <T> T get(String path, Class<T> type) {
        return MainAssetManager.get(path, type);
    }

    // 🔹 게임 종료 시 전체 dispose
    public static void dispose() {
        if (MainAssetManager != null) {
            MainAssetManager.dispose();
        }
    }
}
