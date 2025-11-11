package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;

public class Ground extends GameObject {
    private Body body;
    public static final float PPM = 100f;  // Pixels Per Meter

    public Ground(World world) {
        super(GameSpriteResources.get("sprite/game/img/ground.png", Texture.class));

        // 바닥 위치 및 크기 설정
        Texture groundTexture = GameSpriteResources.get("sprite/game/img/ground.png", Texture.class);
        super.setSize(groundTexture.getWidth(), groundTexture.getHeight());
        super.setPosition(-2560/2f, -1440/2f);

        // Box2D Body 생성
        createBody(world);
    }

    private void createBody(World world) {
        // Body 정의 (정적 - 움직이지 않음)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
            (getX() + getWidth() / 2f) / PPM,
            (getY() + getHeight() / 2f) / PPM
        );

        body = world.createBody(bodyDef);

        // 충돌 Shape 정의 (사각형)
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            getWidth() / 2f / PPM,
            getHeight() / 2f / PPM
        );

        // Fixture 생성 (물리 속성)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;  // 반발력 없음

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }

    public float getTopY() {
        return getY() + getHeight();
    }
}
