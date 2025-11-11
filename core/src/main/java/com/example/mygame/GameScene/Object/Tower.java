package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;

import java.util.ArrayList;

public class Tower extends GameObject {
    private int level;
    private static final int MAX_HP = 100;
    private int hp;
    private final ArrayList<Texture> textures = new ArrayList<>();

    // Box2D
    private Body body;
    private static final float PPM = 100f;

    public Tower(World world, int level) {
        super(GameSpriteResources.get("sprite/game/tower/LV1.png", Texture.class));

        super.setSize(32 * 6, 24 * 6);
        super.setPosition(-920, -483);

        textures.add(GameSpriteResources.get("sprite/game/tower/LV1.png", Texture.class));
        textures.add(GameSpriteResources.get("sprite/game/tower/LV2.png", Texture.class));

        this.level = level;
        this.hp = MAX_HP;

        // Box2D Body 생성
        createBody(world);
    }

    private void createBody(World world) {
        // Body 정의 (정적 - 타워는 움직이지 않음)
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
        fixtureDef.restitution = 0f;

        // 타워임을 식별하기 위한 UserData 설정
        body.setUserData(this);

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void takeDamage(int damage) {
        if (isDead()) {
            return;
        }
        hp -= damage;
        if (isDead()) {
            hp = 0;
        }
    }

    private boolean isDead() {
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }

    public void setTowerLevel(int level) {
        this.level = level;
        super.setTexture(textures.get(level));
    }

    public int getTowerLevel() {
        return level;
    }

    public Body getBody() {
        return body;
    }
}
