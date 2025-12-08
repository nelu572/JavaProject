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
    private static final int MAX_TOWER_LEVEL = 15;
    private static final int TOWER_BASE_COST = 325;
    private static final int TOWER_HP_BASE_INCREASE = 10;

    // 플레이어 업그레이드 관련
    private static int playerAttack;
    private static int playerAttackLevel;
    private static int playerAttackUpgradeCost;
    private static final int MAX_PLAYER_ATTACK_LEVEL = 15;
    private static final int PLAYER_ATTACK_BASE_COST = 380;

    // 재장전 시간 관련 (초 단위로 직접 관리)
    private static float playerReloadTime;  // 재장전 시간 (초)
    private static int playerReloadLevel;
    private static int playerReloadUpgradeCost;
    private static final int MAX_PLAYER_RELOAD_LEVEL = 6;
    private static final int PLAYER_RELOAD_BASE_COST = 550;
    private static final float BASE_RELOAD_TIME = 0.8f;  // 기본 재장전 시간

    // 증가량
    private static final int PLAYER_ATTACK_BASE_INCREASE = 5;
    private static final float RELOAD_TIME_DECREASE = 0.15f;  // 재장전 시간 감소량 (고정)

    // 공통 증가 비율
    private static final double UPGRADE_MULTIPLIER = 1.1;
    // 재장전 전용 증가 비율 (더 완만하게)
    private static final double RELOAD_UPGRADE_MULTIPLIER = 1.25;
    // 공격력 코인 비용 증가 비율
    private static final double ATTACK_COST_MULTIPLIER = 1.25;
    // 공격력 증가량 비율
    private static final double ATTACK_INCREASE_MULTIPLIER = 1.1;

    static {
        MAX_COIN = 9999999;
        coin = 999999;
        wave = 5;

        // 타워 초기값
        towerLevel = 0;
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

    // -----------------------------
    // 게임 오버 시 초기화
    // -----------------------------
    public static void resetGame() {
        // 코인 초기화
        coin = 0;
        wave = 1;
        isWave = false; // 업그레이드 화면으로 전환

        // 타워 초기화
        towerLevel = 1;
        towerUpgradeCost = TOWER_BASE_COST;
        MAX_TOWER_HP = 10;
        tower_hp = MAX_TOWER_HP;

        // 플레이어 공격력 초기화
        playerAttack = 15;
        playerAttackLevel = 1;
        playerAttackUpgradeCost = PLAYER_ATTACK_BASE_COST;

        // 재장전 시간 초기화
        playerReloadTime = BASE_RELOAD_TIME;
        playerReloadLevel = 1;
        playerReloadUpgradeCost = PLAYER_RELOAD_BASE_COST;

        // UI 업데이트
        if(canvas != null) {
            canvas.setHealth();
        }
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

    public static void damageTower(int amount) {
        tower_hp -= amount;

        if(tower_hp < 0) {
            tower_hp = 0;
        }

        if(canvas != null) {
            canvas.setHealth();
        }

        // HP가 0이 되면 게임 오버
        if(tower_hp <= 0) {
            onGameOver();
        }
    }

    // 게임 오버 콜백
    private static Runnable gameOverCallback = null;

    public static void setGameOverCallback(Runnable callback) {
        gameOverCallback = callback;
    }

    private static void onGameOver() {
        // GameScreen에 게임 오버 알림
        if(gameOverCallback != null) {
            gameOverCallback.run();
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
        return (int)(PLAYER_ATTACK_BASE_INCREASE * Math.pow(ATTACK_INCREASE_MULTIPLIER, playerAttackLevel - 1));
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
                playerAttackUpgradeCost = (int)(PLAYER_ATTACK_BASE_COST * Math.pow(ATTACK_COST_MULTIPLIER, playerAttackLevel - 1));
            }
            return true;
        }
        return false;
    }

    // -----------------------------
    // 플레이어 재장전 시간 관련 getter/setter
    // -----------------------------
    public static float getPlayerReloadTime() {
        return playerReloadTime;
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

    public static float getPlayerReloadDecrease() {
        if(isPlayerReloadMaxLevel()) {
            return 0f;
        }
        return RELOAD_TIME_DECREASE;  // 고정된 감소량 반환
    }

    public static boolean isPlayerReloadMaxLevel() {
        return playerReloadLevel >= MAX_PLAYER_RELOAD_LEVEL;
    }

    public static boolean upgradePlayerReload() {
        if(isPlayerReloadMaxLevel()) {
            return false;
        }

        if(spendCoin(playerReloadUpgradeCost)) {
            float decrease = getPlayerReloadDecrease();
            playerReloadTime -= decrease;

            if(playerReloadTime < 0.1f) {
                playerReloadTime = 0.1f;
            }

            playerReloadLevel++;

            if(!isPlayerReloadMaxLevel()) {
                playerReloadUpgradeCost = (int)(PLAYER_RELOAD_BASE_COST * Math.pow(RELOAD_UPGRADE_MULTIPLIER, playerReloadLevel - 1));
            }
            return true;
        }
        return false;
    }
}
