package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Manager.BulletManager;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;

public class Bullet extends GameObject {
    private Body body;
    private World world;
    private boolean alive;
    private float lifeTime;
    private float maxLifeTime = 3f;
    private float angle;

    private static final float PPM = 100f;
    private static final float BULLET_SPEED = 4000f;
    private static final float BULLET_RADIUS = 16f;
    private static final float BULLET_SIZE = 128f;

    public Bullet(World world, Vector2 startPos, float angle) {
        super(GameSpriteResources.get("sprite/game/bullet/1.png", Texture.class));
        this.world = world;
        this.alive = true;
        this.lifeTime = 0f;
        this.angle = angle;

        super.setSize(BULLET_SIZE, BULLET_SIZE);

        createPhysicsBody(startPos, angle);

        // 생성 직후 GameObject 위치 즉시 설정
        setPosition(startPos.x - BULLET_SIZE / 2f, startPos.y - BULLET_SIZE / 2f);
        setRotation(angle);
    }

    private void createPhysicsBody(Vector2 startPos, float angle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startPos.x / PPM, startPos.y / PPM);
        bodyDef.bullet = true;
        bodyDef.gravityScale = 0f; // 중력 영향 받지 않음

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(BULLET_RADIUS / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = true; // 여기서 트리거처럼 설정
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f; // 반발력 제거


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

        if (lifeTime >= maxLifeTime) {
            destroy();
            return;
        }

        Vector2 pos = body.getPosition();
        setPosition(pos.x * PPM - BULLET_SIZE / 2f, pos.y * PPM - BULLET_SIZE / 2f);
        setRotation(angle);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!alive) return;

        batch.draw(getTexture(),
            getX(), getY(),
            BULLET_SIZE / 2f, BULLET_SIZE / 2f,
            BULLET_SIZE, BULLET_SIZE,
            1f, 1f,
            getRotation(),
            0, 0,
            getTexture().getWidth(), getTexture().getHeight(),
            false, false);
    }

    @Override
    public void onHit() {
        // 바로 삭제하지 않고 요청만 등록
        BulletManager.requestDestroy(this);
    }

    public void destroy() {
        if (!alive) return;

        alive = false;
        if (body != null) {
            world.destroyBody(body); // 여기서는 Step이 끝난 후 안전하게 호출
            body = null;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Body getBody() {
        return body;
    }
}
