package com.example.mygame.GameScene.Manager;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.mygame.GameScene.Object.Bullet;
import com.example.mygame.GameScene.Object.Ground;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        // Bullet ↔ Ground
        if (a instanceof Bullet && b instanceof Ground) ((Bullet) a).onHit();
        if (b instanceof Bullet && a instanceof Ground) ((Bullet) b).onHit();
    }

    @Override
    public void endContact(Contact contact) {
        // 접촉이 끝났을 때 처리 (필요 없으면 비워둬도 됨)
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // 충돌 처리 전 (필요 없으면 비워둬도 됨)
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // 충돌 처리 후 (필요 없으면 비워둬도 됨)
    }
}
