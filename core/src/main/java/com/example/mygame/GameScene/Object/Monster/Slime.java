package com.example.mygame.GameScene.Object.Monster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;

import java.util.ArrayList;

public class Slime extends GameObject {

    private Body body;
    private World world;
    private static final float PPM = 100f; // 픽셀-미터 변환

    private static Texture idle_textures;

    private static final float jump_delay = 1.0f;
    private float jump_cooldown = 1.0f;  // 점프 쿨타임 (0이 되면 점프 가능)
    private boolean isJumping = true;

    private boolean isHitting = false;
    private static ArrayList<Texture> hurt_textures = new ArrayList<>();
    private int hurt_index = 0;

    public Slime(World world) {
        super(GameMonsterResources.get("sprite/game/monster/slime/idle/1.png", Texture.class));
        this.world = world;

        for(int i=1;i<=11;i++){
            hurt_textures.add(GameMonsterResources.get("sprite/game/monster/slime/hurt/"+i+".png", Texture.class));
        }
        idle_textures = getTexture();
        super.setPosition(0, 0);
        super.setSize(idle_textures.getWidth() * 4f, idle_textures.getHeight() * 4f);

        jump_cooldown = 0f;  // 초기 쿨타임 0 (바로 점프 가능)

        createBody();
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
            (getX() + getWidth() / 2f) / PPM,
            (getY() + getHeight() / 2f) / PPM
        );

        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        float width = getWidth() / PPM;
        float height = getHeight() / PPM;
        float radius = Math.min(width / 2f, height / 2f);

        CircleShape Circle = new CircleShape();
        Circle.setRadius(radius);
        Circle.setPosition(new Vector2(0, 0));
        FixtureDef Fixture = new FixtureDef();
        Fixture.shape = Circle;
        Fixture.density = 1f;
        Fixture.friction = 1f;
        Fixture.restitution = 0.1f;
        body.createFixture(Fixture);

        body.setUserData(this);
        Circle.dispose();
    }

    @Override
    public void update(float delta) {
        boolean onGround = isOnGround();  // ✅ 한 번만 호출

        // 피격 시간 감소
        if (isHitting) {
            if(hurt_index/5 >= hurt_textures.size()){
                isHitting = false;
                setTexture(idle_textures);
            }
            else{
                setTexture(hurt_textures.get(hurt_index/5));
                hurt_index+=1;
            }
        }

        if (isJumping) {
            // 착지 감지
            if (onGround) {
                isJumping = false;
                jump_cooldown = jump_delay;
            }
        }
        else {
            // 쿨타임 감소 (0보다 클 때만)
            if (jump_cooldown > 0) {
                jump_cooldown -= delta;
                if (jump_cooldown <= 0) {
                    jump_cooldown = 0;
                }
            }
            // 점프 조건 체크
            else if (onGround && !isHitting) {
                jump();
            }
        }

        // 물리 위치 동기화
        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    private void jump() {
        body.setLinearVelocity(new Vector2(-1.5f, 5.5f));
        isJumping = true;
    }

    private boolean isOnGround() {
        float bodyHeight = getHeight() / PPM;
        float rayLength = (bodyHeight / 2f) + 0.02f;

        Vector2 start = body.getPosition();
        Vector2 end = new Vector2(start.x, start.y - rayLength);

        final boolean[] hit = { false };

        world.rayCast((fixture, point, normal, fraction) -> {
            hit[0] = true;
            return 0;
        }, start, end);

        return hit[0];
    }

    @Override
    public void onHit() {
        body.applyLinearImpulse(new Vector2(1.75f, -0.3f), body.getWorldCenter(), true);
        isHitting = true;
        hurt_index = 0;
    }

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
