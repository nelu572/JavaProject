package com.example.mygame.GameScene.Manager;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.mygame.GameScene.Object.Bullet;
import com.example.mygame.GameScene.Object.Ground;
import com.example.mygame.GameScene.Object.Monster.Slime;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        handleCollision(a, b, Bullet.class, Ground.class,
            bullet -> ((Bullet) bullet).onHit(), null);

        handleCollision(a, b, Bullet.class, Slime.class,
            bullet -> ((Bullet) bullet).onHit(),
            slime -> ((Slime) slime).onHit());


    }

    private <T, U> void handleCollision(Object a, Object b,
                                        Class<T> typeA, Class<U> typeB,
                                        java.util.function.Consumer<T> actionA,
                                        java.util.function.Consumer<U> actionB) {

        if (typeA.isInstance(a) && typeB.isInstance(b)) {
            System.out.println("gkgkgkg");
            if (actionA != null) actionA.accept(typeA.cast(a));
            if (actionB != null) actionB.accept(typeB.cast(b));
        }
        else if (typeA.isInstance(b) && typeB.isInstance(a)) {
            System.out.println("gkgkg");
            if (actionA != null) actionA.accept(typeA.cast(b));
            if (actionB != null) actionB.accept(typeB.cast(a));
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
