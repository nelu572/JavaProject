package com.example.mygame.MainScene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainResources {
    private static AssetManager MainAssetManager;

    public static void init() {
        MainAssetManager = new AssetManager();
    }

    public static void loadAssets() {
        MainAssetManager.load("sprite/main/img/BG.png", Texture.class);
        MainAssetManager.load("sprite/main/img/Title.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Start_bnt.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Start_bnt_hover.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Exit_bnt.png", Texture.class);
        MainAssetManager.load("sprite/main/button/Exit_bnt_hover.png", Texture.class);
    }
    public static void finishLoading() {
        MainAssetManager.finishLoading();
    }

    public static <T> T get(String path, Class<T> type) {
        return MainAssetManager.get(path, type);
    }

    public static void dispose() {
        if (MainAssetManager != null) {
            MainAssetManager.dispose();
        }
    }
}
