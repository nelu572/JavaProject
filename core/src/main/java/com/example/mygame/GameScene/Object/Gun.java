package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.example.mygame.GameScene.Resorces.GameSpriteResources;

import java.util.ArrayList;

public class Gun extends GameObject {
    private final Player player;
    private final CoverViewport viewport;
    private float angle;
    private boolean aiming;

    private float offsetX = 0;
    private float offsetY = -5;

    private float pivotX = 0.3f;
    private float pivotY = 0.735f;

    private float muzzleX = 1f;
    private float muzzleY = 0.735f;

    private float recoilOffset = 0f;
    private float recoilRecovery = 20f;

    private ShapeRenderer shapeRenderer;

    private World world;
    private static final float PPM = 100f;

    private ArrayList<Bullet> bullets = new ArrayList<>();

    // 발사 쿨타임

    public Gun(Player player, CoverViewport viewport, World world) {
        super(GameSpriteResources.get("sprite/game/gun/M92.png", Texture.class));
        this.player = player;
        this.viewport = viewport;
        this.world = world;
        super.setSize(19 * 5.5f, 12 * 5.5f);
        shapeRenderer = new ShapeRenderer();
        updatePosition();   
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

                    // fixture가 가진 Body가 Tower인지 확인
                    if (fixture.getBody().getUserData() instanceof Tower) {
                        return 1f; // Tower는 무시
                    }

                    // Tower가 아니면 충돌 처리
                    hitPoint.set(point.x * PPM, point.y * PPM);
                    return fraction;
                }
            }, rayStart, rayEnd);


            // 레이저 렌더링
            batch.end();

            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);

            float lineWidth = 2f;
            shapeRenderer.rectLine(muzzlePos.x, muzzlePos.y, hitPoint.x, hitPoint.y, lineWidth);

            shapeRenderer.end();
            batch.begin();
        }

        // 총알 궤적 효과와 함께 렌더
        for(Bullet b : bullets) {
            b.render(batch, shapeRenderer, viewport);
        }
    }

    private void updatePosition() {
        float playerCenterX = player.getX() + player.getWidth() / 2f;
        float playerCenterY = player.getY() + player.getHeight() / 2f;

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

        float current = normalizeAngle(angle);
        float target = normalizeAngle(targetAngle);

        if(target >= -90f && target <= 80f){
            aiming = true;
            angle = MathUtils.lerpAngleDeg(current, target, 0.5f);
        } else {
            aiming = false;
            angle = MathUtils.lerpAngleDeg(current, 0f, 0.1f);
        }

        angle = normalizeAngle(angle);
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

        if(recoilOffset > 0) {
            recoilOffset = Math.max(0, recoilOffset - recoilRecovery * delta);
            aiming = false;
        }

        // 연사 가능하도록 수정
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && aiming) {
            shoot();
        }
    }

    public void shoot() {
        aiming = false;

        Vector2 muzzlePos = getMuzzleWorldPosition();
        Bullet bullet = new Bullet(world, muzzlePos, angle);
        bullets.add(bullet);

        // 반동 추가
        recoilOffset = 10f;
    }

    public Vector2 getMuzzleWorldPosition() {
        float gunX = getX();
        float gunY = getY();
        float gunWidth = getWidth();
        float gunHeight = getHeight();

        float pivotWorldX = gunX + gunWidth * pivotX;
        float pivotWorldY = gunY + gunHeight * pivotY;

        float muzzleOffsetX = gunWidth * muzzleX - gunWidth * pivotX;
        float muzzleOffsetY = gunHeight * muzzleY - gunHeight * pivotY;

        float cos = (float) Math.cos(Math.toRadians(getRotation()));
        float sin = (float) Math.sin(Math.toRadians(getRotation()));
        float rotatedMuzzleX = pivotWorldX + muzzleOffsetX * cos - muzzleOffsetY * sin;
        float rotatedMuzzleY = pivotWorldY + muzzleOffsetX * sin + muzzleOffsetY * cos;

        return new Vector2(rotatedMuzzleX, rotatedMuzzleY);
    }
}
