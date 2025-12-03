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

    // 재장전 시간 관련 (초 단위로 직접 관리)
    private static float playerReloadTime;  // 재장전 시간 (초)
    private static int playerReloadLevel;
    private static int playerReloadUpgradeCost;
    private static final int MAX_PLAYER_RELOAD_LEVEL = 4;  // 5 → 3으로 변경
    private static final int PLAYER_RELOAD_BASE_COST = 150;
    private static final float BASE_RELOAD_TIME = 1.0f;  // 기본 재장전 시간

    // 증가량
    private static final int PLAYER_ATTACK_BASE_INCREASE = 3;
    private static final float RELOAD_TIME_BASE_DECREASE = 0.15f;  // 0.3초 → 0.15초로 변경

    // 공통 증가 비율
    private static final double UPGRADE_MULTIPLIER = 1.5;

    static {
        MAX_COIN = 9999999;
        coin = 9999999;
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

        // 재장전 시간 초기값
        playerReloadTime = BASE_RELOAD_TIME;
        playerReloadLevel = 1;
        playerReloadUpgradeCost = PLAYER_RELOAD_BASE_COST;
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
        // 기본값 10에서 1.5배씩 증가
        return (int)(TOWER_HP_BASE_INCREASE * Math.pow(UPGRADE_MULTIPLIER, towerLevel - 1));
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
                towerUpgradeCost = (int)(TOWER_BASE_COST * Math.pow(UPGRADE_MULTIPLIER, towerLevel - 1));
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
        // 기본값 3에서 1.5배씩 증가
        return (int)(PLAYER_ATTACK_BASE_INCREASE * Math.pow(UPGRADE_MULTIPLIER, playerAttackLevel - 1));
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
                playerAttackUpgradeCost = (int)(PLAYER_ATTACK_BASE_COST * Math.pow(UPGRADE_MULTIPLIER, playerAttackLevel - 1));
            }
            return true;
        }
        return false;
    }

    // -----------------------------
    // 플레이어 재장전 시간 관련 getter/setter
    // -----------------------------

    /**
     * 현재 재장전 시간 반환 (초)
     */
    public static float getPlayerReloadTime() {
        return playerReloadTime;
    }

    /**
     * 발사 속도 배율 반환 (기본 시간 / 현재 시간)
     * 게임 로직에서 사용
     */
    public static float getPlayerFireRate() {
        return BASE_RELOAD_TIME / playerReloadTime;
    }

    public static int getPlayerReloadLevel() {
        return playerReloadLevel;
    }

    public static int getMaxPlayerReloadLevel() {
        return MAX_PLAYER_RELOAD_LEVEL;
    }

    public static int getPlayerReloadUpgradeCost() {
        return playerReloadUpgradeCost;
    }

    /**
     * 다음 레벨의 재장전 시간 감소량 반환
     * 레벨이 올라갈수록 감소량도 증가 (0.3초 * 1.5^(level-1))
     */
    public static float getPlayerReloadDecrease() {
        if(isPlayerReloadMaxLevel()) {
            return 0f;
        }
        return (float)(RELOAD_TIME_BASE_DECREASE * Math.pow(UPGRADE_MULTIPLIER, playerReloadLevel - 1));
    }

    public static boolean isPlayerReloadMaxLevel() {
        return playerReloadLevel >= MAX_PLAYER_RELOAD_LEVEL;
    }

    /**
     * 재장전 시간 업그레이드
     * 레벨이 올라갈수록 감소량도 증가
     */
    public static boolean upgradePlayerReload() {
        if(isPlayerReloadMaxLevel()) {
            return false;
        }

        if(spendCoin(playerReloadUpgradeCost)) {
            float decrease = getPlayerReloadDecrease();
            playerReloadTime -= decrease;

            // 최소 재장전 시간은 0.1초로 제한
            if(playerReloadTime < 0.1f) {
                playerReloadTime = 0.1f;
            }

            playerReloadLevel++;

            if(!isPlayerReloadMaxLevel()) {
                playerReloadUpgradeCost = (int)(PLAYER_RELOAD_BASE_COST * Math.pow(UPGRADE_MULTIPLIER, playerReloadLevel - 1));
            }
            return true;
        }
        return false;
    }

    // 기존 메서드 호환성을 위한 별칭
    public static float getPlayerFireRateIncrease() {
        return getPlayerReloadDecrease();
    }

    public static int getPlayerFireRateLevel() {
        return playerReloadLevel;
    }

    public static int getMaxPlayerFireRateLevel() {
        return MAX_PLAYER_RELOAD_LEVEL;
    }

    public static int getPlayerFireRateUpgradeCost() {
        return playerReloadUpgradeCost;
    }

    public static String getFireRateDisplay() {
        return String.format("%.2f초", playerReloadTime);
    }

    public static boolean isPlayerFireRateMaxLevel() {
        return isPlayerReloadMaxLevel();
    }

    public static boolean upgradePlayerFireRate() {
        return upgradePlayerReload();
    }
}
