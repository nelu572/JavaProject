package com.example.mygame.GameScene.Object.Monster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Manager.ValueManager;
import com.example.mygame.GameScene.Object.Ground;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;

import java.util.ArrayList;

public class Slime extends GameObject {

    private Body body;
    private World world;
    private static final float PPM = 100f;

    // 그래픽
    private Texture idle_texture;
    private static final float jump_delay = 0.75f;
    private float jump_cooldown = 0.75f;
    private boolean isJumping = true;

    // 피격
    private boolean isHitting = false;
    private ArrayList<Texture> hurt_textures = new ArrayList<>();
    private int hurt_index = 0;
    private static int MAX_HP = 40;
    private int hp;

    // 공격 관련
    private boolean isAttacking = false;
    private boolean attackForward = false;
    private boolean attackBackward = false;

    private static int Damage = 5;

    private float attackTimer = 0f;
    private float attackForwardTime = 0.15f;
    private float attackBackwardTime = 0.5f;

    private Vector2 attackStartPosition = new Vector2();
    private float attackDistance = 1.5f;

    private static final float attack_delay = 1.0f;
    private float attack_cooldown = 0f;

    private boolean needsPositionReset = false;
    private Vector2 targetResetPosition = new Vector2();

    // 사망 관련
    private boolean isDead = false;
    private boolean pendingRemove = false;

    // 충돌 필터
    public static final short CATEGORY_MONSTER = 0x0002;
    public static final short MASK_GROUND = 0x0001;
    public static final short MASK_BULLET = 0x0004;

    // -----------------------
    // 생성자
    // -----------------------
    public Slime(World world) {
        super(GameMonsterResources.get("sprite/game/monster/slime/idle/1.png", Texture.class));
        this.world = world;

        for (int i = 1; i <= 8; i++) {
            hurt_textures.add(GameMonsterResources.get("sprite/game/monster/slime/hurt/" + i + ".png", Texture.class));
        }
        idle_texture = getTexture();

        setPosition(0, 0);
        setSize(idle_texture.getWidth() * 3.5f, idle_texture.getHeight() * 4f);
        jump_cooldown = jump_delay;
        hp = MAX_HP;

        createBody();
    }

    // -----------------------
    // Box2D 바디 생성
    // -----------------------
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

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = circle;
        fixture.density = 1f;
        fixture.friction = 1f;
        fixture.restitution = 0.1f;

        fixture.filter.categoryBits = CATEGORY_MONSTER;
        fixture.filter.maskBits = MASK_GROUND | MASK_BULLET;

        body.createFixture(fixture);
        body.setUserData(this);
        circle.dispose();
    }

    // -----------------------
    // update
    // -----------------------
    @Override
    public void update(float delta) {

        // 사망 처리된 바디 제거
        if (pendingRemove) {
            world.destroyBody(body);
            pendingRemove = false;
            return;
        }

        // 죽어있으면 아무것도 하지 않음
        if (isDead) return;

        if (needsPositionReset) {
            body.setTransform(targetResetPosition.x, targetResetPosition.y, body.getAngle());
            body.setLinearVelocity(0, 0);
            needsPositionReset = false;
        }

        boolean onGround = isOnGround();

        if (isHitting) {
            animation();

        } else if (isAttacking) {
            updateAttack(delta);

        } else {
            if (onGround) {
                if (detectTarget()) {
                    if (attack_cooldown == 0) {
                        startAttack();
                    }
                    if (attack_cooldown > 0) {
                        attack_cooldown -= delta;
                        if (attack_cooldown < 0) attack_cooldown = 0;
                    }
                } else {
                    jump(onGround, delta);
                }
            }
        }

        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    // -----------------------
    // 공격 시작
    // -----------------------
    public void startAttack() {
        isAttacking = true;
        attackForward = true;
        attackBackward = false;
        attackTimer = 0f;

        attackStartPosition.set(body.getPosition());
    }

    // -----------------------
    // 전방 레이 감지
    // -----------------------
    private boolean detectTarget() {
        float maxDistance = 3.0f;

        Vector2 start = body.getPosition();
        Vector2 end = new Vector2(start.x - maxDistance, start.y);

        final boolean[] found = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            Object data = fixture.getBody().getUserData();
            if (data instanceof Player || data instanceof Tower) {
                found[0] = true;
                return 0;
            }
            return -1;
        }, start, end);

        return found[0];
    }

    // -----------------------
    // 공격 단계 업데이트
    // -----------------------
    private void updateAttack(float delta) {

        attackTimer += delta;

        if (attackForward) {

            float targetX = attackStartPosition.x - attackDistance;

            float distanceToGo = targetX - body.getPosition().x;

            float timeRemaining = attackForwardTime - attackTimer;

            if (timeRemaining <= 0) {
                ValueManager.damageTower(Damage);
                attackForward = false;
                attackBackward = true;
                attackTimer = 0f;
            } else {
                float velX = distanceToGo / timeRemaining;
                body.setLinearVelocity(velX, body.getLinearVelocity().y);
            }
            return;
        }

        if (attackBackward) {

            float distanceX = attackStartPosition.x - body.getPosition().x;
            float distanceY = attackStartPosition.y - body.getPosition().y;

            float timeRemaining = attackBackwardTime - attackTimer;

            if (timeRemaining <= 0) {

                attackBackward = false;
                isAttacking = false;
                attackTimer = 0f;

                attack_cooldown = attack_delay;

                targetResetPosition.set(attackStartPosition);
                needsPositionReset = true;

                body.setLinearVelocity(0, 0);
                return;
            }

            float velX = distanceX / timeRemaining;
            float velY = distanceY / timeRemaining;
            body.setLinearVelocity(velX, velY);
        }
    }

    // -----------------------
    // 피격 애니메이션
    // -----------------------
    private void animation() {
        if (hurt_index / 4 >= hurt_textures.size()) {
            isHitting = false;
            setTexture(idle_texture);
            hurt_index = 0;
        } else {
            setTexture(hurt_textures.get(hurt_index / 4));
            hurt_index += 1;
        }
    }

    // -----------------------
    // 점프
    // -----------------------
    private void jump(boolean onGround, float delta) {
        if (isJumping) {
            if (onGround) {
                isJumping = false;
                jump_cooldown = jump_delay;
            }
        } else {
            if (jump_cooldown > 0) {
                jump_cooldown -= delta;
                if (jump_cooldown < 0) jump_cooldown = 0;
            } else if (onGround && !isHitting && !isAttacking) {
                body.setLinearVelocity(new Vector2(-1.0f, 5.0f));
                isJumping = true;
            }
        }
    }

    // -----------------------
    // 바닥 감지
    // -----------------------
    private boolean isOnGround() {
        float bodyHeight = getHeight() / PPM;
        float rayLength = (bodyHeight / 2f) + 0.02f;

        Vector2 start = body.getPosition();
        Vector2 end = new Vector2(start.x, start.y - rayLength);

        final boolean[] isGround = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            Object fUD = fixture.getUserData();
            Object bUD = fixture.getBody().getUserData();

            if ((fUD instanceof Ground) || (bUD instanceof Ground)) {
                isGround[0] = true;
                return 0;
            }

            return -1;
        }, start, end);

        return isGround[0];
    }

    // -----------------------
    // 피격 처리 (HP 감소 포함)
    // -----------------------
    @Override
    public void onHit() {
        if (isDead || isHitting) return;
        hp -= ValueManager.getPlayerAttack();

        if (hp <= 0) {
            die();
            return;
        }
        if (isAttacking) {
            isAttacking = false;
            attackForward = false;
            attackBackward = false;
            attackTimer = 0f;
            attack_cooldown = attack_delay;
        }

        body.setLinearVelocity(new Vector2(2.75f, -0.3f));
        isHitting = true;
        hurt_index = 0;
    }

    // -----------------------
    // 죽음 처리
    // -----------------------
    private void die() {
        isDead = true;
        pendingRemove = true;
    }

    // -----------------------
    // 렌더링
    // -----------------------
    @Override
    public void render(SpriteBatch batch) {
        if (isDead) return;
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
