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
    private static final float PPM = 100f;

    private static Texture idle_textures;

    private static final float jump_delay = 0.75f;
    private float jump_cooldown = 0.75f;
    private boolean isJumping = true;

    private boolean isHitting = false;
    private static ArrayList<Texture> hurt_textures = new ArrayList<>();
    private int hurt_index = 0;

    private boolean isAttacking = false;
    private boolean attackForward = false;
    private boolean attackBackward = false;

    private float attackTimer = 0f;
    private float attackForwardTime = 0.15f;
    private float attackBackwardTime = 0.5f;

    // 공격 시작 위치 저장
    private Vector2 attackStartPosition = new Vector2();
    private float attackDistance = 3.0f;  // 공격 거리 (미터 단위)

    // 공격 쿨타임
    private static final float attack_delay = 2.0f;
    private float attack_cooldown = 0f;

    // 위치 이동 대기열
    private boolean needsPositionReset = false;
    private Vector2 targetResetPosition = new Vector2();

    public Slime(World world) {
        super(GameMonsterResources.get("sprite/game/monster/slime/idle/1.png", Texture.class));
        this.world = world;

        for(int i=1;i<=8;i++){
            hurt_textures.add(GameMonsterResources.get("sprite/game/monster/slime/hurt/"+i+".png", Texture.class));
        }
        idle_textures = getTexture();
        super.setPosition(0, 0);
        super.setSize(idle_textures.getWidth() * 3.5f, idle_textures.getHeight() * 4f);

        jump_cooldown = 0f;

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
        // 대기 중인 위치 리셋 처리 (World가 잠기지 않은 상태에서 안전하게 실행)
        if (needsPositionReset) {
            body.setTransform(targetResetPosition.x, targetResetPosition.y, body.getAngle());
            body.setLinearVelocity(0, 0);
            needsPositionReset = false;
        }

        boolean onGround = isOnGround();

        if(isHitting){
            animation();
        }
        if(isAttacking){
            updateAttack(delta);
        }
        else if(body.getPosition().x < -5.0){
            // 공격 쿨타임 감소
            if (attack_cooldown > 0) {
                attack_cooldown -= delta;
                if (attack_cooldown < 0) {
                    attack_cooldown = 0;
                }
            }
            // 공격 조건: 착지 상태이고, x 좌표가 -5.0보다 작고, 쿨타임이 끝났을 때
            if (onGround && attack_cooldown <= 0) {
                startAttack();
            }
        }
        jump(onGround, delta);
        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    public void startAttack() {
        isAttacking = true;
        attackForward = true;
        attackBackward = false;
        attackTimer = 0f;

        // 현재 위치 저장
        attackStartPosition.set(body.getPosition());
    }

    private void updateAttack(float delta) {
        if (!isAttacking) return;

        attackTimer += delta;

        // 1단계: 앞으로 돌진 (속도 기반 이동)
        if (attackForward) {
            float progress = attackTimer / attackForwardTime;
            if (progress >= 1.0f) {
                progress = 1.0f;
                attackForward = false;
                attackBackward = true;
                attackTimer = 0f;
            }

            // 목표 속도 계산
            float targetX = attackStartPosition.x - attackDistance;
            float distanceToGo = targetX - body.getPosition().x;
            float timeRemaining = attackForwardTime - attackTimer;

            if (timeRemaining > 0) {
                float velocityX = distanceToGo / timeRemaining;
                body.setLinearVelocity(velocityX, body.getLinearVelocity().y);
            }

            return;
        }

        // 2단계: 뒤로 복귀 (속도 기반 이동)
        if (attackBackward) {
            float progress = attackTimer / attackBackwardTime;
            if (progress >= 1.0f) {
                progress = 1.0f;
                attackBackward = false;
                isAttacking = false;
                attackTimer = 0f;

                // 공격 쿨타임 설정
                attack_cooldown = attack_delay;

                // 위치 리셋 예약 - X, Y 좌표 모두 원래 위치로 복귀
                targetResetPosition.set(attackStartPosition.x, attackStartPosition.y);
                needsPositionReset = true;
                body.setLinearVelocity(0, 0);
                return;
            }

            // 복귀 속도 계산 - X와 Y 좌표 모두 계산
            float distanceX = attackStartPosition.x - body.getPosition().x;
            float distanceY = attackStartPosition.y - body.getPosition().y;
            float timeRemaining = attackBackwardTime - attackTimer;

            if (timeRemaining > 0) {
                float velocityX = distanceX / timeRemaining;
                float velocityY = distanceY / timeRemaining;
                body.setLinearVelocity(velocityX, velocityY);
            }
        }
    }

    private void animation(){
        if(hurt_index/4 >= hurt_textures.size()){
            isHitting = false;
            setTexture(idle_textures);
            hurt_index = 0;
        }
        else{
            setTexture(hurt_textures.get(hurt_index/4));
            hurt_index+=1;
        }
    }

    private void jump(boolean onGround, float delta) {
        if (isJumping) {
            if (onGround) {
                isJumping = false;
                jump_cooldown = jump_delay;
            }
        }
        else if(body.getPosition().x >= -5.0){
            if (jump_cooldown > 0) {
                jump_cooldown -= delta;
                if (jump_cooldown <= 0) {
                    jump_cooldown = 0;
                }
            }
            else if (onGround && !isHitting && !isAttacking) {
                body.setLinearVelocity(new Vector2(-1.0f, 5.0f));
                isJumping = true;
            }
        }
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
        if(!isHitting){
            // 공격 중이면 공격 캔슬하고 원래 위치로 복귀 예약
            if(isAttacking) {
                targetResetPosition.set(attackStartPosition.x, attackStartPosition.y);
                needsPositionReset = true;

                isAttacking = false;
                attackForward = false;
                attackBackward = false;
                attackTimer = 0f;
            }

            body.applyLinearImpulse(new Vector2(1.75f, -0.3f), body.getWorldCenter(), true);
            isHitting = true;
            hurt_index = 0;
        }
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
