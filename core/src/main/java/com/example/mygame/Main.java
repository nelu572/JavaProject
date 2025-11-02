package com.example.mygame;

import com.badlogic.gdx.Game;
import com.example.mygame.GameScene.GameScreen;
import com.example.mygame.MainScene.MainResourceManager;
import com.example.mygame.MainScene.MainScreen;

public class Main extends Game {
    private String Scene;

    @Override
    public void create() {
        setScreen(new MainScreen(this)); // 🔹 this 전달
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

    public void SetScene(String Scene) {
        this.Scene = Scene;
    }

    public String GetScene() {
        return Scene;
    }
    public void dispose(){
        MainResourceManager.dispose();
    }
}
