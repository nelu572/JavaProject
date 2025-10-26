package com.example.mygame;

import com.badlogic.gdx.Game;
import com.example.mygame.GameScene.GameScreen;
import com.example.mygame.MainScene.MainScreen;

public class Main extends Game {
    private String Scene = "Main";
    @Override
    public void create() {
        if(Scene.equals("Main")) {
            setScreen(new MainScreen());
        }
        else if(Scene.equals("Game")) {
            setScreen(new GameScreen());
        }
    }
}
