package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Texture;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.GameSpriteResources;

import java.util.ArrayList;

public class Tower extends GameObject {
    private int level;
    private static final int MAX_HP = 100;
    private int hp;
    private final ArrayList<Texture> textures = new ArrayList<>();

    public Tower(int level)
    {
        super(GameSpriteResources.get("sprite/game/tower/LV1.png", Texture.class));
        super.setSize(32*6,24*6);
        super.setPosition(-900,-483);
        textures.add(GameSpriteResources.get("sprite/game/tower/LV1.png", Texture.class));
        textures.add(GameSpriteResources.get("sprite/game/tower/LV2.png", Texture.class));
        this.level = level;
        hp = MAX_HP;
    }
    public void takeDamage(int damage) {
        if (isDead()){
            return;
        }
        hp -= damage;
        if(isDead()){
            hp = 0;

        }
    }
    private boolean isDead() {
        return hp <= 0;
    }
    public int getHp() {
        return hp;
    }

    public void setTowerLevel(int level)
    {
        this.level = level;
        super.setTexture(textures.get(level));
    }
    public int getTowerLevel()
    {
        return level;
    }
}
