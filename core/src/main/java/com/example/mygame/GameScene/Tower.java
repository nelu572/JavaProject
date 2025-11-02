package com.example.mygame.GameScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Tower {
    private float x, y;
    private float range = 150;
    private float attackCooldown = 1f; // 1초당 공격
    private float timeSinceLastShot = 0;
    private Texture texture;

    public Tower(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = new Texture("sprite/ui/main button/gs.jpg"); // 리소스 필요
    }

    public void update(float delta, ArrayList<Monster> monsters, ArrayList<Bullet> bullets) {
        timeSinceLastShot += delta;

        if (timeSinceLastShot >= attackCooldown) {
            for (Monster m : monsters) {
                float dx = m.getX() - x;
                float dy = m.getY() - y;
                if (Math.sqrt(dx*dx + dy*dy) <= range) {
                    bullets.add(new Bullet(x, y, m));
                    timeSinceLastShot = 0;
                    break;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
