package com.example.mygame.GameScene.Manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.example.mygame.GameScene.Object.Monster.Slime;

import java.util.ArrayList;

public class WaveManager {
    private float wave_time = 0;
    private float now_wave;
    private World world;
    private ArrayList<Slime> slimes =  new ArrayList<>();
    public WaveManager(World world) {
        this.world = world;
    }
    public void WaveStart(int wave){
        wave_time = 0;
        now_wave = wave;
    }
    public void update(float delta){
        wave_time += delta;
        if(now_wave==1){
            wave1(delta);
        }
    }
    private void wave1(float delta){
        if(wave_time==0){
            slimes.add(new Slime(this.world));
        }

        for (Slime slime : slimes) {
            slime.update(delta);
        }
    }
    public void render(SpriteBatch batch){
        for(Slime slime: slimes){
            slime.render(batch);
        }
    }
}
