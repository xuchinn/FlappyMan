package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Obstacle {
    public static final int obstacleWidth = 52;
    private Texture topObstacle, bottomObstacle;
    private Vector2 posTopObstacle, posBottomObstacle;
    private Rectangle boundsTop, boundsBot;
    private Random random;
    private int randomNumber;
    private Sound collidesSfx;

    public Obstacle(float x){
        topObstacle = new Texture("top-obstacle.png");
        bottomObstacle = new Texture("bottom-obstacle.png");
        random = new Random();

        posTopObstacle = new Vector2(x, random.nextInt(130) + 220);
        posBottomObstacle = new Vector2(x, posTopObstacle.y - 100 - bottomObstacle.getHeight());

        boundsTop = new Rectangle(posTopObstacle.x, posTopObstacle.y, topObstacle.getWidth(), topObstacle.getHeight());
        boundsBot = new Rectangle(posBottomObstacle.x, posBottomObstacle.y, bottomObstacle.getWidth(), bottomObstacle.getHeight());

        randomNumber = random.nextInt(3);
        collidesSfx = Gdx.audio.newSound(Gdx.files.internal("collidesSfx.mp3"));
    }

    public Texture getTopObstacle() {
        return topObstacle;
    }

    public Texture getBottomObstacle() {
        return bottomObstacle;
    }

    public Vector2 getPosTopObstacle() {
        return posTopObstacle;
    }

    public Vector2 getPosBottomObstacle() {
        return posBottomObstacle;
    }

    public void reposition(float x){
        posTopObstacle.set(x, random.nextInt(130) + 100 + 120);
        posBottomObstacle.set(x, posTopObstacle.y - 100 - bottomObstacle.getHeight());
        boundsTop.setPosition(posTopObstacle.x, posTopObstacle.y);
        boundsBot.setPosition(posBottomObstacle.x, posBottomObstacle.y);

    }

    /**
     * function untuk menggerakkan buildings sebagai obstacle.
     *
     * Apabila randomNumber = 0, building akan bergerak ke atas jika posisi building di bawah 370.
     * Apabila posisi building sudah melebihi 370, maka randomNumber diubah ke randomNumber = 1, sehingga bisa memantul kembali ke bawah.
     *
     * Apabila randomNumber = 1, building bergerak ke bawah jika posisi building di atas -250.
     * Apabila posisi building di bawah -250, maka ran diubah menjadi randomNumber = 0, sehingga building bisa memantul ke atas.
     *
     * Apabila randomNumber = 2, building akan tetap sesuai posisi yang digenerate (tidak bergerak).
     *
     * Terakhir, boundsTop dan boundsBot juga disesuaikan sehingga posisi dimana superman mati(fungsi collides) bisa tersesuaikan
     */
    public void move(){
        if (randomNumber == 0){
            if (posTopObstacle.y < 370) {
                posTopObstacle.y += 20 * Gdx.graphics.getDeltaTime();
                posBottomObstacle.y += 20 * Gdx.graphics.getDeltaTime();
            }
            else
                randomNumber = 1;
        }
        else if (randomNumber == 1){
            if (posBottomObstacle.y > -250) {
                posTopObstacle.y -= 20 * Gdx.graphics.getDeltaTime();
                posBottomObstacle.y -= 20 * Gdx.graphics.getDeltaTime();
            }
            else
                randomNumber = 0;
        }
        boundsTop.setPosition(posTopObstacle.x, posTopObstacle.y);
        boundsBot.setPosition(posBottomObstacle.x, posBottomObstacle.y);
    }

    /**
     * Fungsi untuk mendeteksi apabila superman menyentuh buildings
     * @param character Superman
     * @return boolean. Apabila superman overlap dengan building top atau building bottom, maka akan me-return boolean
     * true.
     */
    public boolean collides(Rectangle character){
        if (character.overlaps(boundsTop) || character.overlaps(boundsBot)) {
            collidesSfx();
            return true;
        }
        else {
            return false;
        }
    }

    public void dispose(){
        topObstacle.dispose();
        bottomObstacle.dispose();
    }

    public void collidesSfx() {
        collidesSfx.play(0.5f);
    }
}
