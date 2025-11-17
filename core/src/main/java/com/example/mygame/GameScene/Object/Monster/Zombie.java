package com.example.mygame.GameScene.Object.Monster;

import com.badlogic.gdx.graphics.Texture;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Manager.BulletManager;

import java.util.ArrayList;

public class Zombie extends GameObject {
    ArrayList<Texture> textures;

    public Zombie(Texture texture) {
        super(texture);
    }

    @Override
    public void onHit() {

    }
}
