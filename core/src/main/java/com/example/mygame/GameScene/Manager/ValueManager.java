package com.example.mygame.GameScene.Manager;

import com.example.mygame.GameScene.UI.Canvas;

public class ValueManager {
    private static int coin;
    private static int tower_hp;
    private static final int MAX_COIN;
    private static int MAX_TOWER_HP;
    private static int wave;
    private static boolean isWave;
    private static Canvas canvas;

    // 타워 업그레이드 관련
    private static int towerLevel;
    private static int towerUpgradeCost;

    // 플레이어 업그레이드 관련
    private static int playerAttack;
    private static int playerAttackUpgradeCost;
    private static float playerReloadTime;
    private static int playerReloadUpgradeCost;

    // 증가량
    private static final int PLAYER_ATTACK_INCREASE = 3;
    private static final float PLAYER_RELOAD_DECREASE = 0.2f;

    static {
        MAX_TOWER_HP = 100;
        MAX_COIN = 1000000;
        coin = 0;
        tower_hp = MAX_TOWER_HP;
        wave = 1;

        // 타워 초기값
        towerLevel = 1;
        towerUpgradeCost = 100;

        // 플레이어 초기값
        playerAttack = 15;
        playerAttackUpgradeCost = 180;
        playerReloadTime = 2.0f;
        playerReloadUpgradeCost = 150;
    }

    public static void setCoin(int coin) {
        if(coin > MAX_COIN) {
            ValueManager.coin = MAX_COIN;
        } else {
            ValueManager.coin = coin;
        }
    }

    public static void addCoin(int amount) {
        setCoin(coin + amount);
    }

    public static boolean spendCoin(int amount) {
        if(coin >= amount) {
            coin -= amount;
            return true;
        }
        return false;
    }

    public static void setMaxTowerHp(int MAX_TOWER_HP) {
        ValueManager.MAX_TOWER_HP = MAX_TOWER_HP;
    }

    public static void setTower_hp(int tower_hp) {
        ValueManager.tower_hp = tower_hp;
        canvas.setHealth();
    }

    public static void setWave(int wave) {
        ValueManager.wave = wave;
    }

    public static void setisWave(boolean isWave) {
        ValueManager.isWave = isWave;
    }

    public static boolean getisWave() {
        return isWave;
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

    // -----------------------------
    // 타워 업그레이드 관련 getter/setter
    // -----------------------------
    public static int getTowerLevel() {
        return towerLevel;
    }

    public static int getTowerUpgradeCost() {
        return towerUpgradeCost;
    }

    public static void upgradeTower() {
        if(spendCoin(towerUpgradeCost)) {
            towerLevel++;
            towerUpgradeCost = (int)(towerUpgradeCost * 1.5f); // 비용 50% 증가
        }
    }

    // -----------------------------
    // 플레이어 공격력 관련 getter/setter
    // -----------------------------
    public static int getPlayerAttack() {
        return playerAttack;
    }

    public static int getPlayerAttackUpgradeCost() {
        return playerAttackUpgradeCost;
    }

    public static int getPlayerAttackIncrease() {
        return PLAYER_ATTACK_INCREASE;
    }

    public static void upgradePlayerAttack() {
        if(spendCoin(playerAttackUpgradeCost)) {
            playerAttack += PLAYER_ATTACK_INCREASE;
            playerAttackUpgradeCost = (int)(playerAttackUpgradeCost * 1.5f);
        }
    }

    // -----------------------------
    // 플레이어 재장전 관련 getter/setter
    // -----------------------------
    public static float getPlayerReloadTime() {
        return playerReloadTime;
    }

    public static int getPlayerReloadUpgradeCost() {
        return playerReloadUpgradeCost;
    }

    public static float getPlayerReloadDecrease() {
        return PLAYER_RELOAD_DECREASE;
    }

    public static void upgradePlayerReload() {
        if(spendCoin(playerReloadUpgradeCost)) {
            playerReloadTime = Math.max(0.5f, playerReloadTime - PLAYER_RELOAD_DECREASE); // 최소 0.5초
            playerReloadUpgradeCost = (int)(playerReloadUpgradeCost * 1.5f);
        }
    }
}
