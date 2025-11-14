package com.example.mygame.GameScene.Object.Monster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;

public class Slime extends GameObject {

    private Body body;
    private World world;
    private static final float PPM = 100f; // 픽셀-미터 변환

    public Slime(World world) {
        super(GameMonsterResources.get("sprite/game/monster/slime/jump/start/1.png", Texture.class));
        this.world = world;

        Texture slimeTexture = getTexture();
        super.setPosition(0, 0);
        super.setSize(slimeTexture.getWidth() * 5f, slimeTexture.getHeight() * 5f); // 크기 그대로 사용

        createBody();
    }

    // ---------------------------------------------------
    // Box2D 물리 바디 생성 — 원형 캡슐 근사
    // ---------------------------------------------------
    private void createBody() {
        // Body 정의
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
            (getX() + getWidth() / 2f) / PPM,
            (getY() + getHeight() / 2f) / PPM
        );
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        float width = getWidth() / PPM;   // 36 / 100 = 0.36
        float height = getHeight() / PPM; // 22 / 100 = 0.22
        float radius = Math.min(width / 2f, height / 2f); // 0.11

        // 상단 원
        CircleShape topCircle = new CircleShape();
        topCircle.setRadius(radius);
        topCircle.setPosition(new Vector2(0, (height / 2f) - radius));
        FixtureDef topFixture = new FixtureDef();
        topFixture.shape = topCircle;
        topFixture.density = 1f;
        topFixture.friction = 0.5f;
        topFixture.restitution = 0.3f;
        body.createFixture(topFixture);
        topCircle.dispose();

        // 하단 원
        CircleShape bottomCircle = new CircleShape();
        bottomCircle.setRadius(radius);
        bottomCircle.setPosition(new Vector2(0, -(height / 2f) + radius));
        FixtureDef bottomFixture = new FixtureDef();
        bottomFixture.shape = bottomCircle;
        bottomFixture.density = 1f;
        bottomFixture.friction = 0.5f;
        bottomFixture.restitution = 0.3f;
        body.createFixture(bottomFixture);
        bottomCircle.dispose();

        // 중간 원 (직사각형 없이 원 3개로 캡슐 근사)
        CircleShape middleCircle = new CircleShape();
        middleCircle.setRadius(radius);
        middleCircle.setPosition(new Vector2(0, 0));
        FixtureDef middleFixture = new FixtureDef();
        middleFixture.shape = middleCircle;
        middleFixture.density = 1f;
        middleFixture.friction = 0.5f;
        middleFixture.restitution = 0.3f;
        body.createFixture(middleFixture);
        middleCircle.dispose();
    }

    // ---------------------------------------------------
    // Update — 애니메이션 진행 + 물리 동기화
    // ---------------------------------------------------
    @Override
    public void update(float delta) {
        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    // ---------------------------------------------------
    // Render — 현재 프레임 출력
    // ---------------------------------------------------
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(
            getTexture(),
            getX(),
            getY(),
            getWidth(),
            getHeight()
        );
    }

    public Body getBody() {
        return body;
    }
}
