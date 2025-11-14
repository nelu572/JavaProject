package com.example.mygame.GameScene.Object.Monster;

import com.badlogic.gdx.graphics.Texture;
import com.example.mygame.EveryScene.GameObject;

import java.util.ArrayList;

public class Zombie extends GameObject {
    ArrayList<Texture> textures;

    public Zombie(Texture texture) {
        super(texture);
    }
}
