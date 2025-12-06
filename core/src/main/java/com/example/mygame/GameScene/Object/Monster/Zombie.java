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

public class Zombie extends GameObject {

    private Body body;
    private World world;
    private static final float PPM = 100f;

    // 그래픽
    private Texture idle_texture;
    private Texture hurt_texture;
    private ArrayList<Texture> attack_textures = new ArrayList<>();
    private int attack_frame_index = 0;

    // 이동
    private static final float MOVE_SPEED = -0.75f; // 왼쪽으로 등속 이동

    // HP
    private static int MAX_HP = 150;
    private int hp;

    // 피격
    private boolean isHitting = false;
    private float hitTimer = 0f;
    private static final float HIT_DURATION = 0.35f;

    // 죽음 애니메이션
    private float deathTimer = 0f;
    private static final float DEATH_DELAY = 0.5f; // 죽음 후 사라지는 시간

    // 공격 관련
    private boolean isAttacking = false;
    private float attackAnimationTimer = 0f;
    private static final float ATTACK_FRAME_DURATION = 0.2f; // 프레임당 0.1초
    private static final float ATTACK_TOTAL_DURATION = 1.4f; // 7프레임 * 0.1초

    private static int Damage = 5;
    private static final float ATTACK_RANGE = 0.55f; // 짧은 사거리
    private static final float attack_delay = 1.2f;
    private float attack_cooldown = 0f;

    private boolean damageDealt = false; // 공격당 한번만 데미지

    // 사망 관련
    private boolean isDead = false;
    private boolean pendingRemove = false;
    private static final int COIN = 150;

    // 충돌 필터
    public static final short CATEGORY_MONSTER = 0x0002;
    public static final short MASK_GROUND = 0x0001;
    public static final short MASK_BULLET = 0x0004;

    // -----------------------
    // 생성자
    // -----------------------
    public Zombie(World world) {
        super(GameMonsterResources.get("sprite/game/monster/zombie/1.png", Texture.class));
        this.world = world;

        // 피격 텍스처 로드
        hurt_texture = GameMonsterResources.get("sprite/game/monster/zombie/8.png", Texture.class);

        // 공격 애니메이션 텍스처 로드 (7프레임)
        for (int i = 1; i <= 7; i++) {
            attack_textures.add(GameMonsterResources.get("sprite/game/monster/zombie/" + i + ".png", Texture.class));
        }

        idle_texture = getTexture();

        setPosition(800,  -300);
        setSize(idle_texture.getWidth() * 7f, idle_texture.getHeight() * 7f);
        hp = MAX_HP;

        createBody();
    }

    // -----------------------
    // Box2D 바디 생성 (박스 콜라이더 - 가로 줄이고 오른쪽 치우침)
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

        // 박스 콜라이더 - 가로 70%로 줄이고 오른쪽으로 10% 이동
        float boxWidth = width * 0.7f;
        float boxHeight = height;
        float offsetX = width * 0.1f; // 오른쪽으로 오프셋

        PolygonShape box = new PolygonShape();
        box.setAsBox(boxWidth / 2f, boxHeight / 2f, new Vector2(offsetX, 0), 0);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = box;
        fixture.density = 1f;
        fixture.friction = 1f;
        fixture.restitution = 0f;

        fixture.filter.categoryBits = CATEGORY_MONSTER;
        fixture.filter.maskBits = MASK_GROUND | MASK_BULLET;

        body.createFixture(fixture);
        body.setUserData(this);
        box.dispose();
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

        // 죽어있으면 타이머만 업데이트하고 렌더링만 함
        if (isDead) {
            deathTimer += delta;
            // 위치 동기화는 계속 (밀리는 모션 표현)
            Vector2 position = body.getPosition();
            setPosition(
                position.x * PPM - getWidth() / 2f,
                position.y * PPM - getHeight() / 2f
            );
            return;
        }

        if (isHitting) {
            hitTimer += delta;

            if (hitTimer >= HIT_DURATION) {
                isHitting = false;
                hitTimer = 0f;
                setTexture(idle_texture);
            }

        } else if (isAttacking) {
            updateAttack(delta);
            body.setLinearVelocity(0, body.getLinearVelocity().y); // 공격시 이동 정지

        } else {
            // 공격 쿨다운 감소
            if (attack_cooldown > 0) {
                attack_cooldown -= delta;
                if (attack_cooldown < 0) attack_cooldown = 0;
            }

            // 타겟 감지 및 행동 결정
            if (detectTarget()) {
                if (attack_cooldown == 0) {
                    startAttack();
                } else {
                    // 쿨다운 중에는 정지
                    body.setLinearVelocity(0, body.getLinearVelocity().y);
                }
            } else {
                // 타겟 없으면 전진
                move();
            }
        }

        // 위치 동기화
        Vector2 position = body.getPosition();
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    // -----------------------
    // 등속 이동
    // -----------------------
    private void move() {
        if (isOnGround()) {
            body.setLinearVelocity(MOVE_SPEED, body.getLinearVelocity().y);
            setTexture(idle_texture);
        }
    }

    // -----------------------
    // 공격 시작
    // -----------------------
    public void startAttack() {
        isAttacking = true;
        attackAnimationTimer = 0f;
        attack_frame_index = 0;
        damageDealt = false;
        setTexture(attack_textures.get(0));
    }

    // -----------------------
    // 전방 레이 감지 (짧은 사거리)
    // -----------------------
    private boolean detectTarget() {
        Vector2 start = body.getPosition();
        Vector2 end = new Vector2(start.x - ATTACK_RANGE, start.y);

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
    // 공격 애니메이션 업데이트 (7프레임)
    // -----------------------
    private void updateAttack(float delta) {
        attackAnimationTimer += delta;

        // 애니메이션 프레임 업데이트
        int currentFrame = (int)(attackAnimationTimer / ATTACK_FRAME_DURATION);

        if (currentFrame < attack_textures.size()) {
            attack_frame_index = currentFrame;
            setTexture(attack_textures.get(attack_frame_index));
        }

        // 애니메이션 중간 시점에 데미지 적용 (4번째 프레임 정도)
        if (!damageDealt && attackAnimationTimer >= ATTACK_TOTAL_DURATION * 0.5f) {
            if (detectTarget()) {
                ValueManager.damageTower(Damage);
            }
            damageDealt = true;
        }

        // 공격 애니메이션 종료
        if (attackAnimationTimer >= ATTACK_TOTAL_DURATION) {
            isAttacking = false;
            attackAnimationTimer = 0f;
            attack_frame_index = 0;
            attack_cooldown = attack_delay;
            setTexture(idle_texture);
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
    // 피격 처리 (HP 감소 + 피격 텍스처 + 순간 밀림)
    // -----------------------
    @Override
    public void onHit() {
        if (isDead || isHitting) return;

        int previousHp = hp;
        hp -= ValueManager.getPlayerAttack();

        if (hp <= 0) {
            hp = 0;
            // 체력이 0이 되면 밀림
            body.setLinearVelocity(new Vector2(2.0f, 1.0f));

            // 피격 상태 시작
            isHitting = true;
            hitTimer = 0f;
            setTexture(hurt_texture);

            die();
            return;
        }

        // 30% 임계값 계산 (40%, 70%, 100%)
        // 40% 남음 = 60 HP
        // 70% 남음 = 105 HP
        int firstThreshold = (int)(MAX_HP * 0.4f);  // 60
        int secondThreshold = (int)(MAX_HP * 0.7f); // 105

        boolean shouldKnockback = false;

        // 70% -> 40% 구간 통과
        if (previousHp > secondThreshold && hp <= secondThreshold) {
            shouldKnockback = true;
        }
        // 40% -> 0% 구간 통과
        else if (previousHp > firstThreshold && hp <= firstThreshold) {
            shouldKnockback = true;
        }

        // 공격 중이었다면 취소
        if (isAttacking) {
            isAttacking = false;
            attackAnimationTimer = 0f;
            attack_frame_index = 0;
            attack_cooldown = attack_delay;
        }

        // 피격 상태 시작
        isHitting = true;
        hitTimer = 0f;
        setTexture(hurt_texture);

        // 임계값을 넘었을 때만 뒤로 밀림
        if (shouldKnockback) {
            body.setLinearVelocity(new Vector2(2.0f, 1.0f));
        }
    }

    // -----------------------
    // 죽음 처리
    // -----------------------
    private void die() {
        isDead = true;
        deathTimer = 0f;
        pendingRemove = true; // 바디 즉시 제거
        ValueManager.addCoin(COIN);
    }

    // -----------------------
    // 렌더링
    // -----------------------
    @Override
    public void render(SpriteBatch batch) {
        // 사망 후 일정 시간 지나면 렌더링 안함
        if (isDead && deathTimer >= DEATH_DELAY) return;

        batch.draw(
            getTexture(),
            getX(),
            getY(),
            getWidth(),
            getHeight()
        );
    }

    // -----------------------
    // Getter 메서드
    // -----------------------
    public Body getBody() {
        return body;
    }

    public boolean isDead() {
        return isDead;
    }
}
