package com.example.mygame.GameScene.Manager;

public class ValueManager {
    private static int coin;
    private static int tower_hp;
    private static int MAX_TOWER_HP;
    private static int wave;

    static {
        MAX_TOWER_HP = 100;
        coin = 0;
        tower_hp = MAX_TOWER_HP;
        wave = 1;
    }
    public static void setCoin(int coin) {
        ValueManager.coin = coin;
    }
    public static void setMaxTowerHp(int MAX_TOWER_HP) {
        ValueManager.MAX_TOWER_HP = MAX_TOWER_HP;
    }
    public static void setTower_hp(int tower_hp) {
        ValueManager.tower_hp = tower_hp;
    }
    public static void setWave(int wave) {
        ValueManager.wave = wave;
    }

    public static int getCoin() {
        return coin;
    }
    public static int getMaxTowerHp() {
        return MAX_TOWER_HP;
    }
    public static int getTower_hp() {
        return tower_hp;
    }
    public static int getWave() {
        return wave;
    }
}
