package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.GameSpriteResources;

import java.util.ArrayList;

public class Gun extends GameObject {
    private final Player player;
    private final CoverViewport viewport;
    private float angle;
    private boolean aiming;

    // 플레이어 기준 오프셋 (플레이어 손 위치)
    private float offsetX = 0;
    private float offsetY = -5;

    // 회전축 위치 (0~1, 0이 왼쪽, 1이 오른쪽)
    private float pivotX = 0.3f; // 테스트: 총의 왼쪽 끝
    private float pivotY = 0.735f; // 총 높이의 50% 지점 (중앙)

    // 총구 위치
    private float muzzleX = 1f;
    private float muzzleY = 0.735f;
    // Gun 클래스에 추가
    private float recoilOffset = 0f;
    private float recoilRecovery = 15f; // 복구 속도

    private ShapeRenderer shapeRenderer;

    private World world;
    private static final float PPM = 100f;

    private ArrayList<Bullet> bullets = new ArrayList<>();

    public Gun(Player player, CoverViewport viewport, World world) {
        super(GameSpriteResources.get("sprite/game/gun/M92.png", Texture.class));
        this.player = player;
        this.viewport = viewport;
        this.world = world;
        super.setSize(19 * 4.5f, 12 * 4.5f);
        shapeRenderer = new ShapeRenderer();
        updatePosition();
    }

    public void shoot() {
        if(!aiming) return;

        Vector2 muzzlePos = getMuzzleWorldPosition();
        Bullet bullet = new Bullet(world, muzzlePos, angle);
        bullets.add(bullet);// Gun 클래스에 추가
        // 반동 추가
        recoilOffset = 10f; // 10픽셀 뒤로
    }


    @Override
    public void render(SpriteBatch batch) {
        // 총 스프라이트 렌더
        batch.draw(getTexture(), getX(), getY(), getWidth() * pivotX, getHeight() * pivotY,
            getWidth(), getHeight(), 1f, 1f, getRotation(),
            0, 0, getTexture().getWidth(), getTexture().getHeight(), false, false);
        if(aiming) {
            // Raycast로 레이저 충돌 체크
            Vector2 muzzlePos = getMuzzleWorldPosition();
            float dx = (float) Math.cos(Math.toRadians(angle));
            float dy = (float) Math.sin(Math.toRadians(angle));

            Vector2 rayStart = new Vector2(muzzlePos.x / PPM, muzzlePos.y / PPM);
            Vector2 rayEnd = new Vector2(
                (muzzlePos.x + dx * 5000f) / PPM,
                (muzzlePos.y + dy * 5000f) / PPM
            );

            final Vector2 hitPoint = new Vector2(rayEnd.x * PPM, rayEnd.y * PPM);

            world.rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    hitPoint.set(point.x * PPM, point.y * PPM);
                    return fraction;
                }
            }, rayStart, rayEnd);

            // 레이저 렌더링 (굵은 선)
            batch.end();

            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);  // Filled로 변경
            shapeRenderer.setColor(Color.RED);

            float lineWidth = 2f;  // 선 굵기 (픽셀 단위)
            shapeRenderer.rectLine(muzzlePos.x, muzzlePos.y, hitPoint.x, hitPoint.y, lineWidth);

            shapeRenderer.end();
            batch.begin();
        }
        for(Bullet b : bullets) {
            b.render(batch);
        }
    }


    // updatePosition 수정
    private void updatePosition() {
        float playerCenterX = player.getX() + player.getWidth() / 2f;
        float playerCenterY = player.getY() + player.getHeight() / 2f;

        // 반동 적용 (총이 바라보는 반대 방향으로)
        float recoilX = -(float)Math.cos(Math.toRadians(angle)) * recoilOffset;
        float recoilY = -(float)Math.sin(Math.toRadians(angle)) * recoilOffset;

        float handX = playerCenterX + offsetX + recoilX;
        float handY = playerCenterY + offsetY + recoilY;

        setPosition(
            handX - getWidth() * pivotX,
            handY - getHeight() * pivotY
        );
    }


    private float normalizeAngle(float angle) {
        return ((angle + 180f) % 360f + 360f) % 360f - 180f;
    }

    public void update(float delta, CoverViewport viewport) {
        super.update(delta);
        updatePosition();

        Vector3 mousePos = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float gunPivotWorldX = getX() + getWidth() * pivotX;
        float gunPivotWorldY = getY() + getHeight() * pivotY;

        float targetAngle = (float)Math.toDegrees(
            Math.atan2(mousePos.y - gunPivotWorldY, mousePos.x - gunPivotWorldX)
        );

// 현재 각도와 목표 각도를 -180~180로 정규화
        float current = normalizeAngle(angle);
        float target = normalizeAngle(targetAngle);

// 목표 각도가 ±80도 범위 안이면 보간
        if(target >= -90f && target <= 80f){
            aiming = true;
            angle = MathUtils.lerpAngleDeg(current, target, 0.5f);
        } else {// 범위를 벗어나면 0도로 부드럽게 보정
            aiming = false;
            angle = MathUtils.lerpAngleDeg(current, 0f, 0.1f);
        }

// 보간 후 각도 정규화
        angle = normalizeAngle(angle);

// 최종 각도 클램프
        angle = MathUtils.clamp(angle, -90f, 80f);


        setRotation(angle);

        for(int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update(delta);
            if(!b.isAlive()) {
                b.destroy();
                bullets.remove(i);
            }
        }

        // 발사 (예: 마우스 클릭시)
        if(Gdx.input.justTouched() && aiming) {
            shoot();
        }
        if(recoilOffset > 0) {
            recoilOffset = Math.max(0, recoilOffset - recoilRecovery * delta);
        }
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
