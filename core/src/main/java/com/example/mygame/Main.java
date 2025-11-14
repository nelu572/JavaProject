package com.example.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.example.mygame.EveryScene.CursorManager;
import com.example.mygame.GameScene.GameScreen;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;
import com.example.mygame.GameScene.Resorces.GameUIResources;
import com.example.mygame.MainScene.MainResources;
import com.example.mygame.MainScene.MainScreen;

public class Main extends Game {
    private String Scene;

    @Override
    public void create() {
        MainResources.init();
        GameMonsterResources.init();
        GameUIResources.init();
        GameSpriteResources.init();

        setScreen(new MainScreen(this)); // this 전달
        Gdx.input.setCursorCatched(true);

        Scene = "Main";
    }

    public void ChangeScene(String Scene) {
        this.Scene = Scene;
        switch (Scene) {
            case "Main":
                setScreen(new MainScreen(this));
                break;
            case "Game":
                setScreen(new GameScreen(this));
                break;
        }
    }
    public String GetScene() {
        return Scene;
    }
    public void dispose(){
        System.out.println("dispose");
        CursorManager.getCursor().getTexture().dispose();
        MainResources.dispose();
        GameUIResources.dispose();
        GameSpriteResources.dispose();
        GameMonsterResources.dispose();
    }
}
