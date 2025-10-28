package com.example.mygame;

import com.badlogic.gdx.Game;
import com.example.mygame.MainScene.MainScreen;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new MainScreen());
    }
    public void ChangeScene(String Scene) {
        if(Scene.equals("Main")) {
            setScreen(new MainScreen());
        }
    }
}
