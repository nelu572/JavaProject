package com.example.mygame.GameScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.mygame.GameScene.Monster;

public class Bullet {
    private float x, y;
    private float speed = 300;
    private Monster target;
    private boolean hit = false;
    private Texture texture;

    public Bullet(float x, float y, Monster target) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.texture = new Texture("sprite/ui/main button/gs.jpg"); // 리소스 필요
    }

    public void update(float delta) {
        if (target.isDead()) {
            hit = true;
            return;
        }

        float dx = target.getX() - x;
        float dy = target.getY() - y;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);

        if (dist < 5) {
            target.takeDamage(10);
            hit = true;
        } else {
            x += dx / dist * speed * delta;
            y += dy / dist * speed * delta;
        }
    }

    public boolean hasHitTarget() {
        return hit;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
