package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;

import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.GameSpriteResources;

public class Gun extends GameObject {
    private final Player player;
    private final CoverViewport viewport;
    private float angle;

    // 플레이어 기준 오프셋 (플레이어 손 위치)
    private float offsetX = 0;
    private float offsetY = -5;

    // 회전축 위치 (0~1, 0이 왼쪽, 1이 오른쪽)
    private float pivotX = 0.2f; // 테스트: 총의 왼쪽 끝
    private float pivotY = 0.725f; // 총 높이의 50% 지점 (중앙)

    // 총구 위치
    private float muzzleX = 1f;
    private float muzzleY = 0.7f;

    private ShapeRenderer shapeRenderer;

    public Gun(Player player, CoverViewport viewport) {
        super(GameSpriteResources.get("sprite/game/gun/M92.png", Texture.class));
        this.player = player;
        this.viewport = viewport;
        super.setSize(19 * 4.5f, 12 * 4.5f);
        shapeRenderer = new ShapeRenderer();

        // 초기 위치만 설정 (각도 계산은 첫 update에서)
        updatePosition();
    }


    private void updatePosition() {
        float playerCenterX = player.getX() + player.getWidth() / 2f;
        float playerCenterY = player.getY() + player.getHeight() / 2f;
        float handX = playerCenterX + offsetX;
        float handY = playerCenterY + offsetY;

        setPosition(
            handX - getWidth() * pivotX,
            handY - getHeight() * pivotY
        );
    }

    public void update(float delta, CoverViewport viewport) {
        super.update(delta);
        updatePosition();

        Vector3 mousePos = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float gunPivotWorldX = getX() + getWidth() * pivotX;
        float gunPivotWorldY = getY() + getHeight() * pivotY;

        // 총 앞쪽이면 회전
        if (mousePos.x >= gunPivotWorldX) {
            float targetAngle = (float) Math.toDegrees(
                Math.atan2(mousePos.y - gunPivotWorldY, mousePos.x - gunPivotWorldX)
            );

            // 이전 각도와 자연스럽게 보간
            float smoothAngle = MathUtils.lerpAngleDeg(angle, targetAngle, 0.2f); // 속도 조절

            // 최종 각도 제한
            float minAngle = -60f;
            float maxAngle = 60f;
            angle = MathUtils.clamp(smoothAngle, minAngle, maxAngle);

            setRotation(angle);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        float drawX = getX();
        float drawY = getY();
        float originX = getWidth() * pivotX;
        float originY = getHeight() * pivotY;

        // flip 처리 제거

        // 총 스프라이트 렌더
        batch.draw(
            getTexture(),
            drawX, drawY,
            originX, originY,
            getWidth(), getHeight(),
            1f, 1f,
            getRotation(),
            0, 0,
            getTexture().getWidth(), getTexture().getHeight(),
            false, false
        );

        // 레이저 렌더
        batch.end();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        Vector2 muzzlePos = getMuzzleWorldPosition();
        float dx = (float) Math.cos(Math.toRadians(angle));
        float dy = (float) Math.sin(Math.toRadians(angle));
        float laserLength = 5000f;

        shapeRenderer.line(muzzlePos.x, muzzlePos.y,
            muzzlePos.x + dx * laserLength,
            muzzlePos.y + dy * laserLength);

        shapeRenderer.end();
        batch.begin();
    }

    /**
     * 총구(Muzzle)의 월드 좌표 계산
     * @return Vector2 : x, y가 월드 좌표
     */
    public Vector2 getMuzzleWorldPosition() {
        float gunX = getX();
        float gunY = getY();
        float gunWidth = getWidth();
        float gunHeight = getHeight();

        // pivot 기준 위치
        float pivotWorldX = gunX + gunWidth * pivotX;
        float pivotWorldY = gunY + gunHeight * pivotY;

        // 총구 기준 월드 좌표 (pivot 기준)
        float muzzleOffsetX = gunWidth * muzzleX - gunWidth * pivotX;
        float muzzleOffsetY = gunHeight * muzzleY - gunHeight * pivotY;

        // 회전 적용
        float cos = (float) Math.cos(Math.toRadians(getRotation()));
        float sin = (float) Math.sin(Math.toRadians(getRotation()));
        float rotatedMuzzleX = pivotWorldX + muzzleOffsetX * cos - muzzleOffsetY * sin;
        float rotatedMuzzleY = pivotWorldY + muzzleOffsetX * sin + muzzleOffsetY * cos;

        return new Vector2(rotatedMuzzleX, rotatedMuzzleY);
    }



}
