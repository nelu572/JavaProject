package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.GameSpriteResources;

public class Gun extends GameObject {
    private final Player player;
    private final Camera camera;

    // 플레이어 기준 오프셋 (플레이어 손 위치)
    private float offsetX = 0;
    private float offsetY = -5;

    // 회전축 위치 (0~1, 0이 왼쪽, 1이 오른쪽)
    private float pivotX = 0.2f; // 테스트: 총의 왼쪽 끝
    private float pivotY = 0.7f; // 총 높이의 50% 지점 (중앙)

    private ShapeRenderer shapeRenderer;

    public Gun(Player player, Camera camera) {
        super(GameSpriteResources.get("sprite/game/gun/M92.png", Texture.class));
        this.player = player;
        this.camera = camera;
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

    @Override
    public void update(float delta) {
        super.update(delta);

        // 위치 업데이트
        updatePosition();

        // 마우스 좌표 → 월드 좌표 변환
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        // 총의 실제 회전축 위치 (렌더링되는 pivot의 월드 좌표)
        float gunPivotWorldX = getX() + getWidth() * pivotX;
        float gunPivotWorldY = getY() + getHeight() * pivotY;

        // 총의 회전축에서 마우스로 향하는 각도 계산
        float angle = (float) Math.toDegrees(
            Math.atan2(mousePos.y - gunPivotWorldY, mousePos.x - gunPivotWorldX)
        );
        setRotation(angle);
    }
    public boolean isFilp() {
        return getRotation()>90 || getRotation()<-90 ;
    }
    @Override
    public void render(SpriteBatch batch) {
        boolean flipped = isFilp();

        float drawX = getX();
        float drawY = getY();
        float originX = getWidth() * pivotX;
        float originY = getHeight() * pivotY;

        // 🔹 Y플립 시 위치 보정
        if (flipped) {
            drawY += getHeight() * (2 * pivotY - 1f);
            originY = getHeight() * (1f - pivotY);
        }

        // 🔹 총 스프라이트 렌더
        batch.draw(
            getTexture(),
            drawX, drawY,
            originX, originY,
            getWidth(), getHeight(),
            1f, 1f,
            getRotation(),
            0, 0,
            getTexture().getWidth(), getTexture().getHeight(),
            false, flipped
        );

        // 🔹 총구에서 화면 끝까지 이어지는 빨간선
        batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        // 회전축(총구) 위치 계산
        float gunPivotWorldX = getX() + getWidth() * pivotX;
        float gunPivotWorldY = getY() + getHeight() * pivotY;

        // 마우스 좌표를 월드 좌표로 변환
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        // 방향 벡터 계산
        float dx = mousePos.x - gunPivotWorldX;
        float dy = mousePos.y - gunPivotWorldY;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        dx /= len;
        dy /= len;

        // 선 길이를 충분히 크게 설정 (예: 화면 너비 또는 더 긴 거리)
        float laserLength = 5000f;

        // 끝점 계산
        float endX = gunPivotWorldX + dx * laserLength;
        float endY = gunPivotWorldY + dy * laserLength;

        // 선 그리기
        shapeRenderer.line(
            gunPivotWorldX, gunPivotWorldY,
            endX, endY
        );

        shapeRenderer.end();
        batch.begin();
    }



}
