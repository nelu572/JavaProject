package com.example.mygame.EveryScene;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * CoverViewport
 * 기준 해상도(Base Resolution)와 실제 해상도(Real Resolution)를 비교하여
 * 비율을 유지하면서 화면을 꽉 채우되,
 * 한쪽이 화면에 닿는 순간 스케일을 멈추고,
 * 남는 영역은 검정색으로 처리하는 커스텀 Viewport
 */
public class CoverViewport extends Viewport {

    private final int baseWidth;     // 기준 해상도 가로
    private final int baseHeight;    // 기준 해상도 세로
    private float scale;             // 실제 스케일 비율
    private int offsetX;             // 화면 여백 (좌우)
    private int offsetY;             // 화면 여백 (상하)

    public CoverViewport(int baseWidth, int baseHeight) {
        this(baseWidth, baseHeight, new OrthographicCamera());
    }

    public CoverViewport(int baseWidth, int baseHeight, Camera camera) {
        this.baseWidth = baseWidth;
        this.baseHeight =  baseHeight;
        setCamera(camera);
        setWorldSize(baseWidth, baseHeight);
    }

    /**
     * 실제 모니터 해상도(screenWidth, screenHeight)를 기준으로
     * Viewport를 초기화함
     */
    public void initialize(int screenWidth, int screenHeight, boolean centerCamera) {
        int viewportWidth;
        int viewportHeight;

        // 비율 비교 후 스케일 결정
        if((float) baseWidth / screenWidth > (float) baseHeight / screenHeight){
            scale = (float) screenWidth / baseWidth;
            viewportWidth = screenWidth;
            viewportHeight = (int) (baseHeight * scale);

            // 상하 여백 계산
            offsetX = 0;
            offsetY = (screenHeight - viewportHeight) / 2;
        }
        else{
            scale = (float) screenHeight / baseHeight;
            viewportWidth = (int) (baseWidth * scale);
            viewportHeight = screenHeight;

            // 좌우 여백 계산
            offsetX = (screenWidth - viewportWidth) / 2;
            offsetY = 0;
        }

        // 계산된 값 적용 - 월드 크기는 항상 고정
        setScreenBounds(offsetX, offsetY, viewportWidth, viewportHeight);
        setWorldSize(baseWidth, baseHeight);  // 월드 크기는 baseWidth, baseHeight로 고정
        apply(centerCamera);
    }
    /**
     * 현재 스케일 값을 반환
     */
    public float getScale() {
        return scale;
    }

    /**
     * 여백 X (좌우 검정 영역)
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * 여백 Y (상하 검정 영역)
     */
    public int getOffsetY() {
        return offsetY;
    }
}
