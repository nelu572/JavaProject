package com.example.mygame.EveryScene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float width, height;
    private Rectangle bounds;
    private float rotation = 0f; // 회전 각도 추가

    public GameObject(Texture texture) {
        this.texture = texture;
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(0, 0, width, height);
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        bounds.setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch) {
        // 회전을 고려한 렌더링
        if (rotation != 0) {
            batch.draw(texture,
                position.x, position.y,           // 그릴 위치
                width / 2f, height / 2f,          // 회전 중심점 (오브젝트 중심)
                width, height,                     // 크기
                1f, 1f,                           // 스케일
                rotation,                          // 회전 각도
                0, 0,                             // 텍스처 시작 위치
                texture.getWidth(), texture.getHeight(), // 텍스처 크기
                false, false);                    // flip
        } else {
            batch.draw(texture, position.x, position.y, width, height);
        }
    }

    // Getter/Setter
    public Rectangle getBounds() { return bounds; }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        bounds.setPosition(position.x, position.y);
    }

    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        bounds.setSize(width, height);
    }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public Texture getTexture() {
        return texture;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }
}
