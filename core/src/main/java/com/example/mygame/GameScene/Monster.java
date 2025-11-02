package com.example.mygame.GameScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Monster {
    private float x, y;
    private float hp;
    private float speed;
    private Texture texture;

    public Monster(float x, float y, float hp, float speed) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
        this.texture = new Texture("sprite/ui/main button/gs.jpg"); // 리소스 필요
    }

    public void update(float delta) {
        // 단순 경로 이동 예시: 오른쪽으로 이동
        x += speed * delta;
    }

    public void takeDamage(float damage) {
        hp -= damage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
