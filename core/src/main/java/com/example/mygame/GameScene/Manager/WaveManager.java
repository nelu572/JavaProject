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
    private ArrayList<Slime> slimes = new ArrayList<>();

    // 웨이브 스폰 관리
    private float spawnTimer = 0f;
    private int spawnCount = 0;
    private int maxSpawnCount = 0;
    private float spawnInterval = 0f;

    public WaveManager(World world) {
        this.world = world;
    }

    public void WaveStart(int wave) {
        wave_time = 0;
        now_wave = wave;
        spawnTimer = 0f;
        spawnCount = 0;

        // 웨이브별 설정
        if (wave == 1) {
            maxSpawnCount = 1;  // 5마리 스폰
            spawnInterval = 2f;  // 2초마다
        }
    }

    public void update(float delta) {
        wave_time += delta;
        spawnTimer += delta;

        if (now_wave == 1) {
            wave1(delta);
        }
    }

    private void wave1(float delta) {
        // 스폰 타이머 체크
        if (spawnCount < maxSpawnCount && spawnTimer >= spawnInterval) {
            slimes.add(new Slime(this.world));
            spawnCount++;
            spawnTimer = 0f;
        }

        for (Slime slime : slimes) {
            slime.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (Slime slime : slimes) {
            slime.render(batch);
        }
    }

    // 원할 때 슬라임 소환
    public void spawnSlime() {
        slimes.add(new Slime(this.world));
    }
}
