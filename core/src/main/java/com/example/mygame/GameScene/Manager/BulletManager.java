package com.example.mygame.GameScene.Manager;

import com.example.mygame.GameScene.Object.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {
    private static final List<Bullet> destroyQueue = new ArrayList<>();

    public static void requestDestroy(Bullet bullet) {
        if (!destroyQueue.contains(bullet)) {
            destroyQueue.add(bullet);
        }
    }

    public static void processDestroyQueue() {
        for (Bullet b : destroyQueue) {
            b.destroy(); // Step이 끝난 후 안전하게 삭제
        }
        destroyQueue.clear();
    }
}
