package com.example.mygame.GameScene.Manager;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.mygame.GameScene.Object.Bullet;
import com.example.mygame.GameScene.Object.Ground;
import com.example.mygame.GameScene.Object.Monster.Slime;
import com.example.mygame.GameScene.Object.Monster.Zombie;
import com.example.mygame.GameScene.Object.Monster.Bat;
import com.example.mygame.GameScene.Object.Monster.Cyclops;
import com.example.mygame.GameScene.Object.Player;
import com.example.mygame.GameScene.Object.Tower;

import java.util.ArrayList;

public class MyContactListener implements ContactListener {

    // Rock 충돌 처리를 지연시키기 위한 큐
    private static ArrayList<Runnable> pendingRockCollisions = new ArrayList<>();

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        // 총알이 이미 충돌했는지 먼저 체크
        Bullet bullet = null;
        if (a instanceof Bullet) {
            bullet = (Bullet) a;
        } else if (b instanceof Bullet) {
            bullet = (Bullet) b;
        }

        // 총알이 이미 충돌했으면 무시
        if (bullet != null && bullet.hasHit()) {
            return;
        }

        // 총알과 지면 충돌
        handleCollision(a, b, Bullet.class, Ground.class,
            bulletObj -> ((Bullet) bulletObj).onHit(), null);

        // 총알과 슬라임 충돌
        handleCollision(a, b, Bullet.class, Slime.class,
            bulletObj -> ((Bullet) bulletObj).onHit(),
            slime -> ((Slime) slime).onHit());

        // 총알과 좀비 충돌
        handleCollision(a, b, Bullet.class, Zombie.class,
            bulletObj -> ((Bullet) bulletObj).onHit(),
            zombie -> ((Zombie) zombie).onHit());

        // 총알과 박쥐 충돌
        handleCollision(a, b, Bullet.class, Bat.class,
            bulletObj -> ((Bullet) bulletObj).onHit(),
            bat -> ((Bat) bat).onHit());

        // 총알과 사일롭스 충돌
        handleCollision(a, b, Bullet.class, Cyclops.class,
            bulletObj -> ((Bullet) bulletObj).onHit(),
            cyclops -> ((Cyclops) cyclops).onHit());

        // 돌(Rock)과 Player 충돌 - 지연 처리
        handleRockCollisionDelayed(a, b, Player.class);

        // 돌(Rock)과 Tower 충돌 - 지연 처리
        handleRockCollisionDelayed(a, b, Tower.class);

        // 돌(Rock)과 Ground 충돌 - 지연 처리
        handleRockCollisionDelayed(a, b, Ground.class);
    }

    private <T, U> void handleCollision(Object a, Object b,
                                        Class<T> typeA, Class<U> typeB,
                                        java.util.function.Consumer<T> actionA,
                                        java.util.function.Consumer<U> actionB) {
        System.out.println(a);
        System.out.println(b);
        if (typeA.isInstance(a) && typeB.isInstance(b)) {
            if (actionA != null) actionA.accept(typeA.cast(a));
            if (actionB != null) actionB.accept(typeB.cast(b));
        }
        else if (typeA.isInstance(b) && typeB.isInstance(a)) {
            if (actionA != null) actionA.accept(typeA.cast(b));
            if (actionB != null) actionB.accept(typeB.cast(a));
        }
    }

    // Rock 충돌 처리를 큐에 추가 (world.step() 이후 처리)
    private <T> void handleRockCollisionDelayed(Object a, Object b, Class<T> targetType) {
        Object rock = null;
        Object target = null;

        // Rock 클래스 이름으로 체크 (내부 클래스라서)
        String aClassName = a != null ? a.getClass().getSimpleName() : "";
        String bClassName = b != null ? b.getClass().getSimpleName() : "";

        if ("Rock".equals(aClassName) && targetType.isInstance(b)) {
            rock = a;
            target = b;
        } else if ("Rock".equals(bClassName) && targetType.isInstance(a)) {
            rock = b;
            target = a;
        }

        if (rock != null) {
            // 충돌 처리를 즉시 실행하지 않고 큐에 추가
            final Object finalRock = rock;
            final Object finalTarget = target;
            pendingRockCollisions.add(() -> {
                Cyclops.handleRockCollision(finalRock, finalTarget);
            });
        }
    }

    // world.step() 이후 호출할 메서드
    public static void processPendingRockCollisions() {
        for (Runnable collision : pendingRockCollisions) {
            collision.run();
        }
        pendingRockCollisions.clear();
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
