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

public class Cyclops extends GameObject {

    private Body body;
    private World world;
    private static final float PPM = 100f;

    // 그래픽
    private Texture idle_texture;
    private ArrayList<Texture> walk_textures = new ArrayList<>();
    private int walk_index = 0;
    private boolean isWalking = false;
    private float walkSpeed = -1.25f; // 왼쪽으로 이동 (속도 감소)
    private static final float SIZE_SCALE = 7f; // 크기 배율 증가

    // 박스 크기 (고정값)
    private float fixedBodyWidth;
    private float fixedBodyHeight;

    // 피격
    private boolean isHitting = false;
    private Texture hurt_texture;
    private float hurt_timer = 0f;
    private static final float hurt_duration = 0.5f;
    private static int MAX_HP = 550;
    private int hp;

    // 죽음 애니메이션
    private float deathTimer = 0f;
    private static final float DEATH_DELAY = 0.5f; // 죽음 후 사라지는 시간

    // 공격 관련
    private boolean isAttacking = false;
    private ArrayList<Texture> attack_textures = new ArrayList<>();
    private int attack_index = 0;
    private static int Damage = 22;

    private static final float attack_delay = 2.5f;
    private float attack_cooldown = 0f;

    // 투사체 관련
    private ArrayList<Rock> rocks = new ArrayList<>();
    private boolean rockThrown = false;

    // 사망 관련
    private boolean isDead = false;
    private boolean pendingRemove = false;
    private static final int COIN = 450;

    // 충돌 필터
    public static final short CATEGORY_MONSTER = 0x0002;
    public static final short MASK_GROUND = 0x0001;
    public static final short MASK_BULLET = 0x0004;

    // -----------------------
    // 생성자
    // -----------------------
    public Cyclops(World world) {
        super(GameMonsterResources.get("sprite/game/monster/cyclops/idle/1.png", Texture.class));
        this.world = world;

        // 걷기 애니메이션 로드
        for (int i = 1; i <= 12; i++) {
            walk_textures.add(GameMonsterResources.get("sprite/game/monster/cyclops/walk/" + i + ".png", Texture.class));
        }

        // 공격 애니메이션 로드
        for (int i = 1; i <= 9; i++) {
            attack_textures.add(GameMonsterResources.get("sprite/game/monster/cyclops/attack/" + i + ".png", Texture.class));
        }

        // 피격 텍스처 로드
        hurt_texture = GameMonsterResources.get("sprite/game/monster/cyclops/hurt/1.png", Texture.class);

        idle_texture = getTexture();

        setPosition(800, -300);
        updateSize(idle_texture); // 초기 크기 설정
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
    // Box2D 바디 생성 (Box 형태)
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

        float width = getWidth() / PPM * 0.7f; // 박스 크기 조정
        float height = getHeight() / PPM * 0.8f;

        // 박스 크기 저장 (고정값)
        fixedBodyWidth = width;
        fixedBodyHeight = height;

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2f, height / 2f);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = box;
        fixture.density = 2f;
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

            if (deathTimer >= DEATH_DELAY) {
                // 렌더링을 멈추기 위한 플래그는 render()에서 처리
            }
            return;
        }

        // 투사체 업데이트
        for (int i = rocks.size() - 1; i >= 0; i--) {
            Rock rock = rocks.get(i);
            rock.update(delta);
            if (rock.shouldRemove()) {
                rock.dispose(world);
                rocks.remove(i);
            }
        }

        boolean onGround = isOnGround();


        if (attack_cooldown > 0) {
            attack_cooldown -= delta;
            if (attack_cooldown < 0) attack_cooldown = 0;
        }
        if (isHitting) {
        hurt_timer += delta;
        body.setLinearVelocity(0,body.getLinearVelocity().y);
        if (hurt_timer >= hurt_duration) {
            isHitting = false;
            hurt_timer = 0f;
            setTexture(idle_texture);
            updateSize(idle_texture);
            // 피격 후 다시 이동 시작
            if (onGround) {
                isWalking = true;
            }
        }
        } else if (isAttacking) {
            updateAttack(delta);
        } else {
            // 타겟 감지 및 공격
            if (detectTarget() && attack_cooldown == 0) {
                startAttack();
            } else if (onGround) {
                // 쿨다운 중이거나 타겟이 없으면 idle 상태로 대기
                if (attack_cooldown > 0 || detectTarget()) {
                    // 대기 상태
                    isWalking = false;
                    body.setLinearVelocity(0, body.getLinearVelocity().y);
                    setTexture(idle_texture);
                    updateSize(idle_texture);
                } else {
                    // 걷기
                    walk(delta);
                }
            }
        }

        Vector2 position = body.getPosition();
        // 이미지의 가운데 아래를 기준으로 텍스처 위치 설정
        setPosition(
            position.x * PPM - getWidth() / 2f,
            position.y * PPM - getHeight() / 2f
        );
    }

    // -----------------------
    // 걷기
    // -----------------------
    private void walk(float delta) {
        if (!isWalking) {
            isWalking = true;
            walk_index = 0;
        }

        // 걷기 애니메이션 (프레임 시간 증가: /4 -> /6)
        if (walk_index / 6 >= walk_textures.size()) {
            walk_index = 0;
        }
        Texture currentTexture = walk_textures.get(walk_index / 6);
        setTexture(currentTexture);
        updateSize(currentTexture);
        walk_index++;

        // 이동
        body.setLinearVelocity(walkSpeed, body.getLinearVelocity().y);
    }

    // -----------------------
    // 공격 시작
    // -----------------------
    public void startAttack() {
        isAttacking = true;
        attack_index = 0;
        rockThrown = false;
        isWalking = false;
        body.setLinearVelocity(0, body.getLinearVelocity().y); // 공격 중 정지
    }

    // -----------------------
    // 전방 레이 감지 (사거리 증가)
    // -----------------------
    private boolean detectTarget() {
        float maxDistance = 9.0f; // 사거리 증가

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
    // 공격 애니메이션 업데이트
    // -----------------------
    private void updateAttack(float delta) {
        if (attack_index / 5 >= attack_textures.size()) {
            // 공격 종료
            isAttacking = false;
            attack_index = 0;
            attack_cooldown = attack_delay;
            setTexture(idle_texture);
            updateSize(idle_texture);
        } else {
            Texture currentTexture = attack_textures.get(attack_index / 5);
            setTexture(currentTexture);
            updateSize(currentTexture);

            // 애니메이션 끝날 때쯤 돌 던지기 (7번째 프레임)
            if (attack_index / 5 == 7 && !rockThrown) {
                throwRock();
                rockThrown = true;
            }

            attack_index++;
        }
    }

    // -----------------------
    // 돌 던지기
    // -----------------------
    private void throwRock() {
        Vector2 startPos = body.getPosition().cpy();
        startPos.x -= 0.5f; // 사일롭스 앞쪽에서 생성
        startPos.y += 1.5f; // 위로 올림

        Rock rock = new Rock(world, startPos, Damage);
        rocks.add(rock);
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
    // 피격 처리
    // -----------------------
    @Override
    public void onHit() {
        if (isDead || isHitting) return;

        int previousHp = hp;
        hp -= ValueManager.getPlayerAttack();

        if (hp <= 0) {
            hp = 0;
            // 체력이 0이 되면 밀림
            body.setLinearVelocity(new Vector2(2.0f, 0f));

            // 피격 상태 시작
            isHitting = true;
            isWalking = false;
            hurt_timer = 0f;
            setTexture(hurt_texture);
            updateSize(hurt_texture);

            die();
            return;
        }

        // 공격 중이면 중단
        if (isAttacking) {
            isAttacking = false;
            attack_index = 0;
            attack_cooldown = attack_delay;
        }
        body.setLinearVelocity(0,0);
        // 피격 상태 시작
        isHitting = true;
        isWalking = false;
        hurt_timer = 0f;
        setTexture(hurt_texture);
        updateSize(hurt_texture);
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

    public boolean isDead() {
        return isDead;
    }

    // -----------------------
    // 렌더링
    // -----------------------
    @Override
    public void render(SpriteBatch batch) {
        // 사망 후 일정 시간 지나면 렌더링 안함
        if (isDead && deathTimer >= DEATH_DELAY) return;

        // Body 위치를 기준으로 이미지의 가운데 아래가 박스의 가운데 아래에 오도록
        Vector2 bodyPos = body.getPosition();

        batch.draw(
            getTexture(),
            bodyPos.x * PPM - getWidth() / 2f,  // 가운데 정렬
            (bodyPos.y - fixedBodyHeight / 2f) * PPM,  // 고정된 박스 아래에 맞춤
            getWidth(),
            getHeight()
        );

        // 돌 렌더링
        for (Rock rock : rocks) {
            rock.render(batch);
        }
    }

    public Body getBody() {
        return body;
    }

    // -----------------------
    // Rock 충돌 처리용 public 메서드
    // -----------------------
    public static void handleRockCollision(Object rockObj, Object targetObj) {
        if (rockObj != null && rockObj.getClass().getSimpleName().equals("Rock")) {
            try {
                // Rock의 damage 가져오기
                int damage = (int) rockObj.getClass().getMethod("getDamage").invoke(rockObj);

                // Player나 Tower와 충돌 시 데미지
                if (targetObj instanceof Player || targetObj instanceof Tower) {
                    ValueManager.damageTower(damage);
                }

                // Rock 제거 플래그 설정
                rockObj.getClass().getMethod("setShouldRemove").invoke(rockObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 게임 오버 시 모든 돌 제거
    public void disposeAllRocks(World world) {
        for (int i = rocks.size() - 1; i >= 0; i--) {
            Rock rock = rocks.get(i);
            if (!world.isLocked()) {
                rock.dispose(world);
            }
        }
        rocks.clear();
    }

    // -----------------------
    // 투사체(돌) 클래스
    // -----------------------
    private class Rock {
        private Body body;
        private Texture texture;
        private float width, height;
        private int damage;
        private boolean shouldRemove = false;
        private float lifeTime = 0f;
        private static final float MAX_LIFE_TIME = 5f;

        // 충돌 필터
        public static final short CATEGORY_ROCK = 0x0008;
        public static final short MASK_ALL = -1; // 모든 것과 충돌

        public Rock(World world, Vector2 startPos, int damage) {
            this.damage = damage;
            this.texture = GameMonsterResources.get("sprite/game/monster/cyclops/rock/1.png", Texture.class);
            this.width = texture.getWidth() * 6f; // 크기 더 증가
            this.height = texture.getHeight() * 6f; // 크기 더 증가

            // 투사체 바디 생성
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(startPos);
            bodyDef.bullet = true;
            bodyDef.gravityScale = 0.5f; // 중력 약간 증가

            body = world.createBody(bodyDef);

            CircleShape circle = new CircleShape();
            circle.setRadius(0.4f); // 충돌 반경도 증가

            FixtureDef fixture = new FixtureDef();
            fixture.shape = circle;
            fixture.density = 0.5f;
            fixture.isSensor = true;

            // 충돌 필터 설정
            fixture.filter.categoryBits = CATEGORY_ROCK;
            fixture.filter.maskBits = MASK_ALL;

            body.createFixture(fixture);
            body.setUserData(this);
            circle.dispose();

            // 왼쪽으로 거의 수평으로 발사
            body.setLinearVelocity(-12f, 0.5f);
        }

        public void update(float delta) {
            lifeTime += delta;
            if (lifeTime >= MAX_LIFE_TIME) {
                shouldRemove = true;
            }
        }

        public void render(SpriteBatch batch) {
            Vector2 pos = body.getPosition();
            batch.draw(
                texture,
                pos.x * PPM - width / 2f,
                pos.y * PPM - height / 2f,
                width,
                height
            );
        }

        public void onHitTarget() {
            shouldRemove = true;
            ValueManager.damageTower(damage);
        }

        public boolean shouldRemove() {
            return shouldRemove;
        }

        public void setShouldRemove() {
            shouldRemove = true;
        }

        public void dispose(World world) {
            world.destroyBody(body);
        }

        public Body getBody() {
            return body;
        }

        public int getDamage() {
            return damage;
        }
    }
}
