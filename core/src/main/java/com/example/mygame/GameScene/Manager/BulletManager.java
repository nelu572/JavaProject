package com.example.mygame.GameScene.Manager;

import com.example.mygame.GameScene.Object.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {
    private static final List<Bullet> activeBullets = new ArrayList<>();
    private static final List<Bullet> destroyQueue = new ArrayList<>();

    public static void registerBullet(Bullet bullet) {
        if (!activeBullets.contains(bullet)) {
            activeBullets.add(bullet);
        }
    }

    public static void requestDestroy(Bullet bullet) {
        if (!destroyQueue.contains(bullet)) {
            destroyQueue.add(bullet);
        }
    }

    public static void processDestroyQueue() {
        for (Bullet b : destroyQueue) {
            b.destroy(); // Step이 끝난 후 안전하게 삭제
            activeBullets.remove(b);
        }
        destroyQueue.clear();
    }

    // 모든 총알 제거 (웨이브 종료, 게임 오버 시 사용)
    public static void destroyAllBullets() {
        destroyQueue.addAll(activeBullets);
    }

    // 리셋 (게임 완전 초기화 시 사용)
    public static void reset() {
        activeBullets.clear();
        destroyQueue.clear();
    }
}
