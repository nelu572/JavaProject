package com.example.mygame.MainScene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainResourceManager {
    private static AssetManager MainAssetManager;

    public static void init() {
        MainAssetManager = new AssetManager();
    }

    // 🔹 Main 씬 전용 로딩
    public static void loadMainUIAssets() {
        MainAssetManager.load("sprite/ui/img/BG.png", Texture.class);
        MainAssetManager.load("sprite/ui/img/Title.png", Texture.class);
        MainAssetManager.load("sprite/ui/main button/Start_bnt.png", Texture.class);
        MainAssetManager.load("sprite/ui/main button/Start_bnt_hover.png", Texture.class);
        MainAssetManager.load("sprite/ui/main button/Exit_bnt.png", Texture.class);
        MainAssetManager.load("sprite/ui/main button/Exit_bnt_hover.png", Texture.class);
    }

    // 🔹 Main 씬 리소스만 언로드
    public static void unloadMainUIAssets() { // 추가
        if (MainAssetManager.isLoaded("sprite/ui/img/BG.png")) MainAssetManager.unload("sprite/ui/img/BG.png");
        if (MainAssetManager.isLoaded("sprite/ui/img/Title.png")) MainAssetManager.unload("sprite/ui/img/Title.png");
        if (MainAssetManager.isLoaded("sprite/ui/main button/Start_bnt.png")) MainAssetManager.unload("sprite/ui/main button/Start_bnt.png");
        if (MainAssetManager.isLoaded("sprite/ui/main button/Start_bnt_hover.png")) MainAssetManager.unload("sprite/ui/main button/Start_bnt_hover.png");
        if (MainAssetManager.isLoaded("sprite/ui/main button/Exit_bnt.png")) MainAssetManager.unload("sprite/ui/main button/Exit_bnt.png");
        if (MainAssetManager.isLoaded("sprite/ui/main button/Exit_bnt_hover.png")) MainAssetManager.unload("sprite/ui/main button/Exit_bnt_hover.png");
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
