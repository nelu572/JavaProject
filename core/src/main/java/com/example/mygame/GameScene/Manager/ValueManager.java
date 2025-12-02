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
    private static final int MAX_TOWER_LEVEL = 10;
    private static final int TOWER_BASE_COST = 100;
    private static final int TOWER_HP_BASE_INCREASE = 10;

    // 플레이어 업그레이드 관련
    private static int playerAttack;
    private static int playerAttackLevel;
    private static int playerAttackUpgradeCost;
    private static final int MAX_PLAYER_ATTACK_LEVEL = 15;
    private static final int PLAYER_ATTACK_BASE_COST = 180;

    // 발사 속도 관련
    private static float playerFireRate;  // 발사 속도 (배율)
    private static int playerFireRateLevel;
    private static int playerFireRateUpgradeCost;
    private static final int MAX_PLAYER_FIRE_RATE_LEVEL = 5;
    private static final int PLAYER_FIRE_RATE_BASE_COST = 150;

    // 증가량
    private static final int PLAYER_ATTACK_BASE_INCREASE = 3;

    static {
        MAX_COIN = 1000000;
        coin = 9999;
        wave = 1;

        // 타워 초기값
        towerLevel = 1;
        towerUpgradeCost = TOWER_BASE_COST;
        MAX_TOWER_HP = 10;
        tower_hp = MAX_TOWER_HP;

        // 플레이어 초기값
        playerAttack = 15;
        playerAttackLevel = 1;
        playerAttackUpgradeCost = PLAYER_ATTACK_BASE_COST;

        // 발사 속도 초기값 (1.0배 = 기본 속도)
        playerFireRate = 1.0f;
        playerFireRateLevel = 1;
        playerFireRateUpgradeCost = PLAYER_FIRE_RATE_BASE_COST;
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
        if(canvas != null) {
            canvas.setHealth();
        }
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

    public static void setCanvas(Canvas canvas) {
        ValueManager.canvas = canvas;
    }

    // -----------------------------
    // 타워 업그레이드 관련 getter/setter
    // -----------------------------
    public static int getTowerLevel() {
        return towerLevel;
    }

    public static int getMaxTowerLevel() {
        return MAX_TOWER_LEVEL;
    }

    public static int getTowerUpgradeCost() {
        return towerUpgradeCost;
    }

    public static int getTowerHPIncrease() {
        return TOWER_HP_BASE_INCREASE + (towerLevel * 2);
    }

    public static boolean isTowerMaxLevel() {
        return towerLevel >= MAX_TOWER_LEVEL;
    }

    public static boolean upgradeTower() {
        if(isTowerMaxLevel()) {
            return false;
        }

        if(spendCoin(towerUpgradeCost)) {
            int hpIncrease = getTowerHPIncrease();
            towerLevel++;
            MAX_TOWER_HP += hpIncrease;
            tower_hp = MAX_TOWER_HP;

            if(canvas != null) {
                canvas.setHealth();
            }

            if(!isTowerMaxLevel()) {
                towerUpgradeCost = (int)(TOWER_BASE_COST * Math.pow(1.5, towerLevel - 1));
            }
            return true;
        }
        return false;
    }

    // -----------------------------
    // 플레이어 공격력 관련 getter/setter
    // -----------------------------
    public static int getPlayerAttack() {
        return playerAttack;
    }

    public static int getPlayerAttackLevel() {
        return playerAttackLevel;
    }

    public static int getMaxPlayerAttackLevel() {
        return MAX_PLAYER_ATTACK_LEVEL;
    }

    public static int getPlayerAttackUpgradeCost() {
        return playerAttackUpgradeCost;
    }

    public static int getPlayerAttackIncrease() {
        return PLAYER_ATTACK_BASE_INCREASE + playerAttackLevel;
    }

    public static boolean isPlayerAttackMaxLevel() {
        return playerAttackLevel >= MAX_PLAYER_ATTACK_LEVEL;
    }

    public static boolean upgradePlayerAttack() {
        if(isPlayerAttackMaxLevel()) {
            return false;
        }

        if(spendCoin(playerAttackUpgradeCost)) {
            playerAttack += getPlayerAttackIncrease();
            playerAttackLevel++;
            if(!isPlayerAttackMaxLevel()) {
                playerAttackUpgradeCost = (int)(PLAYER_ATTACK_BASE_COST * Math.pow(1.5, playerAttackLevel - 1));
            }
            return true;
        }
        return false;
    }

    // -----------------------------
    // 플레이어 발사 속도 관련 getter/setter
    // -----------------------------

    /**
     * 현재 발사 속도 배율 반환 (1.0 = 기본, 2.0 = 2배속)
     */
    public static float getPlayerFireRate() {
        return playerFireRate;
    }

    public static int getPlayerFireRateLevel() {
        return playerFireRateLevel;
    }

    public static int getMaxPlayerFireRateLevel() {
        return MAX_PLAYER_FIRE_RATE_LEVEL;
    }

    public static int getPlayerFireRateUpgradeCost() {
        return playerFireRateUpgradeCost;
    }

    /**
     * 다음 레벨의 발사 속도 증가량 반환
     * 레벨에 따라 증가량이 커짐
     */
    public static float getPlayerFireRateIncrease() {
        // Lv1->2: +0.5 (1.5배)
        // Lv2->3: +0.6 (2.1배)
        // Lv3->4: +0.7 (2.8배)
        // Lv4->5: +0.8 (3.6배)
        return 0.5f + (playerFireRateLevel * 0.1f);
    }

    /**
     * 현재 발사 속도 증가량 (UI 표시용)
     */
    public static String getFireRateDisplay() {
        return String.format("%.1fx", playerFireRate);
    }

    public static boolean isPlayerFireRateMaxLevel() {
        return playerFireRateLevel >= MAX_PLAYER_FIRE_RATE_LEVEL;
    }

    /**
     * 발사 속도 업그레이드
     * 레벨이 올라갈수록 발사 속도가 빨라짐
     */
    public static boolean upgradePlayerFireRate() {
        if(isPlayerFireRateMaxLevel()) {
            return false;
        }

        if(spendCoin(playerFireRateUpgradeCost)) {
            // 발사 속도 증가 (레벨에 따라 증가량이 커짐)
            playerFireRate += getPlayerFireRateIncrease();
            playerFireRateLevel++;

            if(!isPlayerFireRateMaxLevel()) {
                playerFireRateUpgradeCost = (int)(PLAYER_FIRE_RATE_BASE_COST * Math.pow(1.5, playerFireRateLevel - 1));
            }
            return true;
        }
        return false;
    }

    // 기존 메서드 호환성을 위한 메서드들 (UI 표시용)
    public static float getPlayerReloadTime() {
        // 기본 발사 간격(2.0초)을 발사 속도로 나눈 값
        return 2.0f / playerFireRate;
    }

    public static int getPlayerReloadLevel() {
        return playerFireRateLevel;
    }

    public static int getMaxPlayerReloadLevel() {
        return MAX_PLAYER_FIRE_RATE_LEVEL;
    }

    public static int getPlayerReloadUpgradeCost() {
        return playerFireRateUpgradeCost;
    }

    /**
     * UI에 표시될 재장전 시간 감소량
     * 현재 재장전 시간에서 다음 레벨 재장전 시간을 뺀 값
     */
    public static float getPlayerReloadDecrease() {
        if(isPlayerFireRateMaxLevel()) {
            return 0f;
        }
        float currentReloadTime = 2.0f / playerFireRate;
        float nextFireRate = playerFireRate + getPlayerFireRateIncrease();
        float nextReloadTime = 2.0f / nextFireRate;
        return currentReloadTime - nextReloadTime;
    }

    public static boolean isPlayerReloadMaxLevel() {
        return isPlayerFireRateMaxLevel();
    }

    public static boolean upgradePlayerReload() {
        return upgradePlayerFireRate();
    }
}
