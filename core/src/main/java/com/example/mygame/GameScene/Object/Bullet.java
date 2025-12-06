package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Manager.BulletManager;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;

import java.util.ArrayList;

public class Bullet extends GameObject {
    private Body body;
    private World world;
    private boolean alive;
    private boolean hasHit = false; // 이미 충돌했는지 체크
    private float lifeTime;
    private float maxLifeTime = 3f;
    private float angle;

    private static final float PPM = 100f;
    private static final float BULLET_SPEED = 4000f;
    private static final float BULLET_RADIUS = 12f;
    private static final float BULLET_SIZE = 85f;

    // 궤적 효과를 위한 이전 위치들
    private ArrayList<Vector2> trail = new ArrayList<>();
    private static final int MAX_TRAIL_LENGTH = 5; // 궤적 길이
    private float trailTimer = 0f;
    private float trailInterval = 0.01f; // 궤적 점 생성 간격

    public Bullet(World world, Vector2 startPos, float angle) {
        super(GameSpriteResources.get("sprite/game/bullet/1.png", Texture.class));
        this.world = world;
        this.alive = true;
        this.lifeTime = 0f;
        this.angle = angle;

        super.setSize(BULLET_SIZE, BULLET_SIZE);

        createPhysicsBody(startPos, angle);

        setPosition(startPos.x - BULLET_SIZE / 2f, startPos.y - BULLET_SIZE / 2f);
        setRotation(angle);

        // 초기 위치를 궤적에 추가
        trail.add(new Vector2(startPos.x, startPos.y));
    }

    private void createPhysicsBody(Vector2 startPos, float angle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startPos.x / PPM, startPos.y / PPM);
        bodyDef.bullet = true;
        bodyDef.gravityScale = 0f;

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(BULLET_RADIUS / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = true;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        circle.dispose();

        float vx = (float) Math.cos(Math.toRadians(angle)) * BULLET_SPEED;
        float vy = (float) Math.sin(Math.toRadians(angle)) * BULLET_SPEED;
        body.setLinearVelocity(vx / PPM, vy / PPM);

        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!alive) return;

        lifeTime += delta;
        trailTimer += delta;

        if (lifeTime >= maxLifeTime) {
            destroy();
            return;
        }

        Vector2 pos = body.getPosition();
        float centerX = pos.x * PPM;
        float centerY = pos.y * PPM;

        setPosition(centerX - BULLET_SIZE / 2f, centerY - BULLET_SIZE / 2f);
        setRotation(angle);

        // 궤적 업데이트
        if (trailTimer >= trailInterval) {
            trail.add(new Vector2(centerX, centerY));
            trailTimer = 0f;

            // 궤적이 너무 길어지면 오래된 것 제거
            if (trail.size() > MAX_TRAIL_LENGTH) {
                trail.remove(0);
            }
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, CoverViewport viewport) {
        if (!alive) return;

        // 총알 본체 렌더링
        batch.setColor(1.5f, 1.2f, 1f, 1f);
        batch.draw(getTexture(),
            getX(), getY(),
            BULLET_SIZE / 2f, BULLET_SIZE / 2f,
            BULLET_SIZE, BULLET_SIZE,
            1f, 1f,
            getRotation(),
            0, 0,
            getTexture().getWidth(), getTexture().getHeight(),
            false, false);
        batch.setColor(1f, 1f, 1f, 1f);
        // 궤적 효과 렌더링
        if (trail.size() > 1) {
            batch.end();

            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (int i = 0; i < trail.size() - 1; i++) {
                Vector2 p1 = trail.get(i);
                Vector2 p2 = trail.get(i + 1);

                // 궤적이 오래될수록 투명하고 얇게
                float alpha = (float) i / trail.size();
                float width = 4f * alpha;

                // 노란색-주황색 그라디언트 효과
                shapeRenderer.setColor(1f, 0.8f - (0.3f * alpha), 0.2f, alpha * 0.8f);
                shapeRenderer.rectLine(p1.x, p1.y, p2.x, p2.y, width);
            }

            shapeRenderer.end();
            batch.begin();
        }
    }

    @Override
    public void onHit() {
        // 이미 충돌했으면 무시
        if (hasHit) return;

        hasHit = true;
        BulletManager.requestDestroy(this);
    }

    public void destroy() {
        if (!alive) return;

        alive = false;
        trail.clear();

        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean hasHit() {
        return hasHit;
    }

    public Body getBody() {
        return body;
    }
}
