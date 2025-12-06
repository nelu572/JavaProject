package com.example.mygame.GameScene.Manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.example.mygame.GameScene.Object.Monster.Slime;
import com.example.mygame.GameScene.Object.Monster.Zombie;
import com.example.mygame.GameScene.Object.Monster.Cyclops;

import java.util.ArrayList;
import java.util.Iterator;

public class WaveManager {
    private float wave_time = 0;
    private int now_wave = 0;
    private World world;
    private ArrayList<Slime> slimes = new ArrayList<>();
    private ArrayList<Zombie> zombies = new ArrayList<>();
    private ArrayList<Cyclops> cyclopses = new ArrayList<>();

    // 스폰 데이터: {시간, 슬라임 수, 좀비 수, 사일롭스 수}

    // Wave 1: 좀비만 등장 (입문)
    private float[][] wave1_spawns = {
        {1f, 0, 1, 0},      // 1초: 좀비 1
        {3f, 0, 1, 0},      // 3초: 좀비 1
        {5f, 0, 1, 0},      // 5초: 좀비 1
    };

    // Wave 2: 좀비 + 슬라임 첫 등장 (초급)
    private float[][] wave2_spawns = {
        {1f, 0, 1, 0},      // 1초: 좀비 1
        {2.5f, 1, 0, 0},    // 2.5초: 슬라임 1
        {5f, 0, 1, 0},      // 5초: 좀비 1
        {6f, 1, 0, 0},      // 6초: 슬라임 1
        {7f, 0, 1, 0},      // 7초: 좀비 1
    };

    // Wave 3: 좀비와 슬라임 혼합 (중급)
    private float[][] wave3_spawns = {
        {1f, 1, 1, 0},      // 1초: 슬라임 1, 좀비 1
        {2f, 0, 1, 0},      // 2초: 좀비 1
        {3.5f, 1, 1, 0},    // 3.5초: 슬라임 1, 좀비 1
        {5f, 0, 1, 0},      // 5초: 좀비 1
        {5.5f, 0, 1, 0},    // 5.5초: 좀비 1
        {7f, 1, 0, 0},      // 7초: 슬라임 1
        {8f, 0, 1, 0},      // 8초: 좀비 1
        {8.5f, 0, 1, 0},    // 8.5초: 좀비 1
        {9f, 1, 0, 0},      // 9초: 슬라임 1
        {10f, 1, 1, 0},     // 10초: 슬라임 1, 좀비 1
    };

    // Wave 4: 대량 공격 (고급)
    private float[][] wave4_spawns = {
        {1f, 1, 0, 0},      // 1초: 슬라임 1
        {1.5f, 1, 0, 0},    // 1.5초: 슬라임 1
        {2f, 1, 1, 0},      // 2초: 슬라임 1, 좀비 1
        {3f, 0, 1, 0},      // 3초: 좀비 1
        {3.5f, 1, 0, 0},    // 3.5초: 슬라임 1
        {4f, 0, 1, 0},      // 4초: 좀비 1
        {4.5f, 0, 1, 0},    // 4.5초: 좀비 1
        {5f, 1, 0, 0},      // 5초: 슬라임 1
        {6f, 1, 1, 0},      // 6초: 슬라임 1, 좀비 1
        {7f, 0, 1, 0},      // 7초: 좀비 1
        {7.5f, 1, 0, 0},    // 7.5초: 슬라임 1
        {8f, 0, 1, 0},      // 8초: 좀비 1
        {8.5f, 0, 1, 0},    // 8.5초: 좀비 1
        {9f, 1, 0, 0},      // 9초: 슬라임 1
        {10f, 0, 1, 0},     // 10초: 좀비 1
        {10.5f, 1, 0, 0},   // 10.5초: 슬라임 1
        {11f, 1, 1, 0},     // 11초: 슬라임 1, 좀비 1
        {12f, 0, 1, 0},     // 12초: 좀비 1
        {12.5f, 0, 1, 0},   // 12.5초: 좀비 1
        {13f, 1, 0, 0},     // 13초: 슬라임 1
        {14f, 0, 1, 0},     // 14초: 좀비 1
    };

    // Wave 5: 보스 웨이브 - 사일롭스 등장! (최고난이도)
    private float[][] wave5_spawns = {
        {1f, 1, 0, 0},      // 1초: 슬라임 1
        {1.5f, 0, 1, 0},    // 1.5초: 좀비 1
        {2f, 1, 0, 0},      // 2초: 슬라임 1
        {2.5f, 0, 1, 0},    // 2.5초: 좀비 1
        {3f, 1, 0, 0},      // 3초: 슬라임 1
        {4f, 0, 0, 1},      // 4초: 사일롭스 1 (첫 등장!)
        {5f, 0, 1, 0},      // 5초: 좀비 1
        {5.5f, 1, 0, 0},    // 5.5초: 슬라임 1
        {6f, 0, 1, 0},      // 6초: 좀비 1
        {6.5f, 1, 0, 0},    // 6.5초: 슬라임 1
        {7f, 0, 1, 0},      // 7초: 좀비 1
        {8f, 1, 0, 0},      // 8초: 슬라임 1
        {8.5f, 0, 1, 0},    // 8.5초: 좀비 1
        {9f, 0, 0, 1},      // 9초: 사일롭스 1
        {10f, 1, 0, 0},     // 10초: 슬라임 1
        {10.5f, 0, 1, 0},   // 10.5초: 좀비 1
        {11f, 1, 0, 0},     // 11초: 슬라임 1
        {11.5f, 0, 1, 0},   // 11.5초: 좀비 1
        {12f, 1, 0, 0},     // 12초: 슬라임 1
        {13f, 0, 1, 0},     // 13초: 좀비 1
        {13.5f, 0, 1, 0},   // 13.5초: 좀비 1
        {14f, 1, 0, 0},     // 14초: 슬라임 1
        {15f, 0, 0, 1},     // 15초: 사일롭스 1
        {16f, 0, 1, 0},     // 16초: 좀비 1
        {16.5f, 1, 0, 0},   // 16.5초: 슬라임 1
        {17f, 0, 1, 0},     // 17초: 좀비 1
        {18f, 0, 0, 1},     // 18초: 사일롭스 1
        {19f, 1, 0, 0},     // 19초: 슬라임 1
        {19.5f, 0, 1, 0},   // 19.5초: 좀비 1
        {20f, 1, 0, 0},     // 20초: 슬라임 1
        {21f, 0, 1, 0},     // 21초: 좀비 1
    };

    private int current_spawn_index = 0;
    private boolean all_spawned = false;

    public WaveManager(World world) {
        this.world = world;
    }

    public void WaveStart(int wave) {
        wave_time = 0;
        now_wave = wave;
        current_spawn_index = 0;
        all_spawned = false;

        // 이전 웨이브 몬스터 정리
        slimes.clear();
        zombies.clear();
        cyclopses.clear();
    }

    public void update(float delta) {
        if (now_wave == 0) return; // 웨이브가 시작되지 않음

        wave_time += delta;

        // 웨이브별 처리
        switch (now_wave) {
            case 1:
                processWave(delta, wave1_spawns);
                break;
            case 2:
                processWave(delta, wave2_spawns);
                break;
            case 3:
                processWave(delta, wave3_spawns);
                break;
            case 4:
                processWave(delta, wave4_spawns);
                break;
            case 5:
                processWave(delta, wave5_spawns);
                break;
            default:
                // 웨이브 5 이후는 계속 웨이브 5 패턴 반복
                processWave(delta, wave5_spawns);
                break;
        }

        // 몬스터 업데이트
        updateMonsters(delta);

        // 죽은 몬스터 제거
        removeDeadMonsters();

        // 웨이브 클리어 체크
        checkWaveClear();
    }

    // 통합 웨이브 처리 메서드 (같은 시간에 여러 종류 가능, 같은 종류는 0.5초 간격)
    private void processWave(float delta, float[][] spawns) {
        // 스폰 체크
        if (current_spawn_index < spawns.length) {
            float spawn_time = spawns[current_spawn_index][0];

            if (wave_time >= spawn_time) {
                int slime_count = (int)spawns[current_spawn_index][1];
                int zombie_count = (int)spawns[current_spawn_index][2];
                int cyclops_count = (int)spawns[current_spawn_index][3];

                // 슬라임 소환 (같은 종류는 0.5초 간격으로)
                for (int i = 0; i < slime_count; i++) {
                    Slime slime = new Slime(this.world);
                    slime.setPosition(1180 + (i * 100), 100);
                    slimes.add(slime);
                }

                // 좀비 소환 (같은 종류는 0.5초 간격으로)
                for (int i = 0; i < zombie_count; i++) {
                    Zombie zombie = new Zombie(this.world);
                    zombie.setPosition(1180 + (i * 150), 100);
                    zombies.add(zombie);
                }

                // 사일롭스 소환 (같은 종류는 0.5초 간격으로)
                for (int i = 0; i < cyclops_count; i++) {
                    Cyclops cyclops = new Cyclops(this.world);
                    cyclops.setPosition(1180 + (i * 200), 100);
                    cyclopses.add(cyclops);
                }

                current_spawn_index++;
            }
        } else {
            all_spawned = true;
        }
    }

    // 몬스터 업데이트
    private void updateMonsters(float delta) {
        for (int i = 0; i < slimes.size(); i++) {
            slimes.get(i).update(delta);
        }

        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).update(delta);
        }

        for (int i = 0; i < cyclopses.size(); i++) {
            cyclopses.get(i).update(delta);
        }
    }

    private void removeDeadMonsters() {
        // Iterator를 사용하여 안전하게 제거
        Iterator<Slime> slimeIterator = slimes.iterator();
        while (slimeIterator.hasNext()) {
            Slime slime = slimeIterator.next();
            if (slime.isDead()) {
                slimeIterator.remove();
            }
        }

        Iterator<Zombie> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            if (zombie.isDead()) {
                zombieIterator.remove();
            }
        }

        Iterator<Cyclops> cyclopsIterator = cyclopses.iterator();
        while (cyclopsIterator.hasNext()) {
            Cyclops cyclops = cyclopsIterator.next();
            if (cyclops.isDead()) {
                cyclopsIterator.remove();
            }
        }
    }

    private void checkWaveClear() {
        // 모든 몬스터가 소환되었고, 남은 몬스터가 없으면 웨이브 클리어
        if (all_spawned && slimes.isEmpty() && zombies.isEmpty() && cyclopses.isEmpty()) {
            onWaveClear();
        }
    }

    private void onWaveClear() {
        // 웨이브 클리어 처리
        ValueManager.setWave(ValueManager.getWave() + 1); // 다음 웨이브 번호로 증가
        ValueManager.setTower_hp(ValueManager.getMaxTowerHp()); // 타워 체력 회복
        ValueManager.setisWave(false); // 업그레이드 모드로 전환
        now_wave = 0; // 웨이브 초기화
    }

    public void render(SpriteBatch batch) {
        // 렌더링도 일반 for문으로 변경
        for (int i = slimes.size()-1; i >= 0; i--) {
            slimes.get(i).render(batch);
        }

        for (int i = zombies.size()-1; i >= 0; i--) {
            zombies.get(i).render(batch);
        }

        for (int i = cyclopses.size()-1; i >= 0; i--) {
            cyclopses.get(i).render(batch);
        }
    }

    public int getCurrentWave() {
        return now_wave;
    }

    public int getAliveMonsterCount() {
        return slimes.size() + zombies.size() + cyclopses.size();
    }

    // 게임 오버 시 웨이브 초기화
    public void reset() {
        wave_time = 0;
        now_wave = 0;
        current_spawn_index = 0;
        all_spawned = false;

        // 모든 몬스터의 Body 제거
        for (int i = 0; i < slimes.size(); i++) {
            Slime slime = slimes.get(i);
            if (slime.getBody() != null && !world.isLocked()) {
                world.destroyBody(slime.getBody());
            }
        }

        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if (zombie.getBody() != null && !world.isLocked()) {
                world.destroyBody(zombie.getBody());
            }
        }

        for (int i = 0; i < cyclopses.size(); i++) {
            Cyclops cyclops = cyclopses.get(i);
            // 사일롭스의 모든 돌 먼저 제거
            cyclops.disposeAllRocks(world);
            // 사일롭스 Body 제거
            if (cyclops.getBody() != null && !world.isLocked()) {
                world.destroyBody(cyclops.getBody());
            }
        }

        // 리스트 비우기
        slimes.clear();
        zombies.clear();
        cyclopses.clear();
    }
}
