package com.example.mygame.GameScene.Object.Monster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.example.mygame.EveryScene.GameObject;
import com.example.mygame.GameScene.Manager.ValueManager;
import com.example.mygame.GameScene.Object.Ground;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;
import com.example.mygame.GameScene.Resorces.GameMonsterResources;

import java.util.ArrayList;

public class Bat extends GameObject {

    private Body body;
    private World world;
    private static final float PPM = 100f;

    // 그래픽
    private ArrayList<Texture> fly_textures = new ArrayList<>();
    private int fly_index = 0;
    private static final float SIZE_SCALE = 5.5f; // 크기 배율 (약간 증가)

    // 박스 크기 (고정값)
    private float fixedBodyWidth;
    private float fixedBodyHeight;

    // 피격
    private boolean isHitting = false;
    private Texture hurt_texture;
    private float hurt_timer = 0f;
    private static final float hurt_duration = 0.4f;
    private static int MAX_HP = 60;
    private int hp;

    // 죽음 애니메이션
    private float deathTimer = 0f;
    private static final float DEATH_DELAY = 0.5f;

    // 이동 관련
    private static final float MOVE_SPEED = 1.5f; // 등속 이동 속도

    // 공격 관련
    private boolean isAttacking = false;
    private boolean attackForward = false;
    private boolean attackBackward = false;

    private static int Damage = 2; // 슬라임보다 약한 공격력

    private float attackTimer = 0f;
    private float attackForwardTime = 0.1f; // 빠른 공격
    private float attackBackwardTime = 0.5f;

    private Vector2 attackStartPosition = new Vector2();
    private Vector2 hitRetreatTarget = new Vector2(); // 피격 시 복귀 목표 위치
    private float attackDistance = 1.25f;
    private float attackDownwardOffset = 1f; // 대각선 아래로 이동

    // 공격 중 피격 시 복귀 상태
    private boolean isRetreatingFromHit = false; // 공격 중 피격 후 복귀 중 (무적)
    private float extraRetreatDistance = 1.0f; // 추가 후퇴 거리
    private float hitBackwardTime = 0.5f; // 피격 시 빠른 복귀

    private static final float attack_delay = 0.25f; // 빠른 공속
    private float attack_cooldown = 0.25f;

    // 사망 관련
    private boolean isDead = false;
    private boolean pendingRemove = false;
    private static final int COIN = 100;

    // 충돌 필터
    public static final short CATEGORY_MONSTER = 0x0002;
    public static final short MASK_MONSTER = ~CATEGORY_MONSTER; // 몬스터끼리 충돌 안함

    // -----------------------
    // 생성자
    // -----------------------
    public Bat(World world) {
        super(GameMonsterResources.get("sprite/game/monster/bat/fly/1.png", Texture.class));
        this.world = world;

        // 날아다니는 애니메이션 (4프레임)
        for (int i = 1; i <= 4; i++) {
            fly_textures.add(GameMonsterResources.get("sprite/game/monster/bat/fly/" + i + ".png", Texture.class));
        }

        // 피격 텍스처 로드
        hurt_texture = GameMonsterResources.get("sprite/game/monster/bat/hurt/1.png", Texture.class);

        setPosition(800, -100); // 더 높은 공중에 생성
        updateSize(fly_textures.get(0)); // 초기 크기 설정
        hp = MAX_HP;

        createBody();
    }

    // -----------------------
    // 텍스처 크기에 맞춰 사이즈 업데이트
    // -----------------------
    private void updateSize(Texture texture) {
        setSize(texture.getWidth() * SIZE_SCALE, texture.getHeight() * SIZE_SCALE);
    }

    // -----------------------
    // Box2D 바디 생성 (중력 영향 없음)
    // -----------------------
    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Dynamic으로 변경

        bodyDef.position.set(
            (getX() + getWidth() / 2f) / PPM,
            (getY() + getHeight() / 2f) / PPM
        );

        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0f; // 중력 영향 0으로 설정

        body = world.createBody(bodyDef);

        float width = getWidth() / PPM * 0.75f;
        float height = getHeight() / PPM * 0.75f;

        // 박스 크기 저장 (고정값)
        fixedBodyWidth = width;
        fixedBodyHeight = height;

        CircleShape circle = new CircleShape();
        circle.setRadius(Math.min(width, height) / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = circle;
        fixture.density = 1f;
        fixture.friction = 0f;
        fixture.restitution = 0f;

        fixture.filter.categoryBits = CATEGORY_MONSTER;
        fixture.filter.maskBits = MASK_MONSTER; // 몬스터끼리 충돌 안함

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
            if (!world.isLocked()) {
                world.destroyBody(body);
            }
            pendingRemove = false;
            return;
        }

        // 죽어있으면 타이머만 업데이트
        if (isDead) {
            deathTimer += delta;
            Vector2 position = body.getPosition();
            setPosition(
                position.x * PPM - getWidth() / 2f,
                position.y * PPM - getHeight() / 2f
            );
            return;
        }

        if (isHitting) {
            hurt_timer += delta;
            if (hurt_timer >= hurt_duration) {
                isHitting = false;
                hurt_timer = 0f;
                // 복귀 상태가 아닐 때만 텍스처 복구
                if (!isRetreatingFromHit) {
                    setTexture(fly_textures.get(0));
                    updateSize(fly_textures.get(0));
                }
            }
        } else if (isAttacking) {
            updateAttack(delta);

        } else {
            // 공격 쿨다운 업데이트
            if (attack_cooldown > 0) {
                attack_cooldown -= delta;
                if (attack_cooldown < 0) attack_cooldown = 0;
            }

            // 타겟 감지
            if (detectTarget()) {
                if (attack_cooldown == 0) {
                    startAttack();
                } else {
                    // 공격 대기 중에는 제자리에서 멈춤
                    body.setLinearVelocity(0, 0);
                    updateFlyAnimation();
                }
            } else {
                // 등속 이동
                body.setLinearVelocity(-MOVE_SPEED, 0);
                updateFlyAnimation();
            }
        }

        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    // -----------------------
    // 날아다니는 애니메이션 업데이트
    // -----------------------
    private void updateFlyAnimation() {
        fly_index++;
        if (fly_index / 8 >= fly_textures.size()) {
            fly_index = 0;
        }
        Texture currentTexture = fly_textures.get(fly_index / 8);
        setTexture(currentTexture);
        updateSize(currentTexture);
    }

    // -----------------------
    // 공격 시작
    // -----------------------
    public void startAttack() {
        isAttacking = true;
        attackForward = true;
        attackBackward = false;
        attackTimer = 0f;
        isRetreatingFromHit = false;

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
    // 공격 단계 업데이트 (대각선 아래로)
    // -----------------------
    private void updateAttack(float delta) {

        attackTimer += delta;

        if (attackForward) {
            // 대각선 아래로 돌진
            float targetX = attackStartPosition.x - attackDistance;
            float targetY = attackStartPosition.y - attackDownwardOffset;

            float distanceX = targetX - body.getPosition().x;
            float distanceY = targetY - body.getPosition().y;

            float timeRemaining = attackForwardTime - attackTimer;

            if (timeRemaining <= 0) {
                ValueManager.damageTower(Damage);
                attackForward = false;
                attackBackward = true;
                attackTimer = 0f;
            } else {
                float velX = distanceX / timeRemaining;
                float velY = distanceY / timeRemaining;
                body.setLinearVelocity(velX, velY);
            }
            return;
        }

        if (attackBackward) {
            // 복귀 목표 위치 (피격 시에는 더 뒤로)
            float targetX = isRetreatingFromHit ? hitRetreatTarget.x : attackStartPosition.x;
            float targetY = isRetreatingFromHit ? hitRetreatTarget.y : attackStartPosition.y;

            float distanceX = targetX - body.getPosition().x;
            float distanceY = targetY - body.getPosition().y;
            float distance = (float)Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            // 복귀 시간 선택 (피격 시 더 느리게)
            float backwardTime = isRetreatingFromHit ? hitBackwardTime : attackBackwardTime;

            // 목표 위치에 충분히 가까워지면 공격 종료
            if (distance < 0.1f || attackTimer >= backwardTime) {
                attackBackward = false;
                isAttacking = false;
                attackTimer = 0f;
                attack_cooldown = attack_delay;

                // 복귀 완료 시 상태 초기화
                if (isRetreatingFromHit) {
                    isRetreatingFromHit = false;
                    setTexture(fly_textures.get(0));
                    updateSize(fly_textures.get(0));
                }

                body.setTransform(targetX, targetY, body.getAngle());
                body.setLinearVelocity(0, 0);
                return;
            }

            // 남은 시간에 따라 속도 계산
            float timeRemaining = backwardTime - attackTimer;
            float velX = distanceX / timeRemaining;
            float velY = distanceY / timeRemaining;

            // 속도 제한
            float maxReturnSpeed = 8.0f;
            float speed = (float)Math.sqrt(velX * velX + velY * velY);
            if (speed > maxReturnSpeed) {
                velX = (velX / speed) * maxReturnSpeed;
                velY = (velY / speed) * maxReturnSpeed;
            }

            body.setLinearVelocity(velX, velY);
        }
    }

    // -----------------------
    // 피격 처리 (HP 감소 포함)
    // -----------------------
    @Override
    public void onHit() {
        // 죽었거나, 일반 피격 중이거나, 복귀 중일 때는 피격 무시
        if (isDead || isHitting || isRetreatingFromHit) return;

        hp -= ValueManager.getPlayerAttack();

        if (hp <= 0) {
            hp = 0;

            isHitting = true;
            hurt_timer = 0f;
            setTexture(hurt_texture);
            updateSize(hurt_texture);

            die();
            return;
        }

        if (isAttacking) {
            body.setLinearVelocity(new Vector2(0f, 0f));
            // 공격 중 맞으면 복귀 목표 위치 고정
            hitRetreatTarget.set(
                attackStartPosition.x + extraRetreatDistance,
                attackStartPosition.y
            );

            // 복귀 상태로 전환 (무적 상태 활성화)
            attackForward = false;
            attackBackward = true;
            attackTimer = 0f;
            isRetreatingFromHit = true;

            // hurt 텍스처는 표시하되 isHitting은 false 유지
            setTexture(hurt_texture);
            updateSize(hurt_texture);
        } else {
            // 평소에 맞으면 뒤로 밀림
            body.setLinearVelocity(new Vector2(1f, 0f));

            isHitting = true;
            hurt_timer = 0f;
            setTexture(hurt_texture);
            updateSize(hurt_texture);
        }
    }

    // -----------------------
    // 죽음 처리
    // -----------------------
    private void die() {
        isDead = true;
        deathTimer = 0f;
        pendingRemove = true;
        ValueManager.addCoin(COIN);
    }

    public boolean isDead() {
        return isDead;
    }

    // -----------------------
    // 렌더링
    // -----------------------
    @Override
    public void render(SpriteBatch batch) {
        if (isDead && deathTimer >= DEATH_DELAY) return;

        // Body 위치를 기준으로 이미지의 가운데에 맞춤
        Vector2 bodyPos = body.getPosition();

        batch.draw(
            getTexture(),
            bodyPos.x * PPM - getWidth() / 2f,
            bodyPos.y * PPM - getHeight() / 2f,
            getWidth(),
            getHeight()
        );
    }

    public Body getBody() {
        return body;
    }
}
