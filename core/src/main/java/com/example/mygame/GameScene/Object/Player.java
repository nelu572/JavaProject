package com.example.mygame.GameScene.Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.CoverViewport;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Resorces.GameSpriteResources;

public class Player extends GameObject {
    private Gun gun;
    private CoverViewport viewport;
    private Body body;
    private World world;
    private static final float PPM = 100f;

    public Player(World world, CoverViewport viewport) {
        super(GameSpriteResources.get("sprite/game/player/idle1.png", Texture.class));
        this.world = world;
        this.viewport = viewport;

        super.setPosition(-830, -340);
        Texture playerTexture = getTexture();
        super.setSize(playerTexture.getWidth() * 6f, playerTexture.getHeight() * 6f);

        createBody();
        gun = new Gun(this, viewport, world);
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  // 동적 (중력 적용)
        bodyDef.position.set(
            (getX() + getWidth() / 2f) / PPM,
            (getY() + getHeight() / 2f) / PPM
        );
        bodyDef.fixedRotation = true;  // 회전 방지

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2f / PPM, getHeight() / 2f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void update(float delta) {

        // Box2D Body 위치를 GameObject에 동기화
        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );

        gun.update(delta, viewport);
    }

    @Override
    public void render(SpriteBatch batch) {
        float drawX = getX();
        float drawY = getY();
        float width = getWidth();
        float height = getHeight();

        batch.draw(getTexture(), drawX, drawY, width, height);
        gun.render(batch);
    }

    public Gun getGun() {
        return gun;
    }

    public Body getBody() {
        return body;
    }
}
