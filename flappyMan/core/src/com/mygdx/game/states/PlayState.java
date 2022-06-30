package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FlappyMan;
import com.mygdx.game.sprites.Obstacle;
import com.mygdx.game.sprites.Superman;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class PlayState extends State {
    private static final int obstacleSpacing = 125;
    private static final int obstacleCount = 4;
    private static final int groundYOffset = -50;

    private Superman superman;
    private GameOverState gameOverState;
    private LivesState livesState;
    private Texture bg, bg1, bg2;
    private Vector2 bgPos1, bgPos2;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Texture lives1, lives2, lives3;
    private int score;
    private int lives;
    private Sound scoreSfx;
    private Sound passSfx;
    private int count;


    private Array<Obstacle> tubesArray;
    private BitmapFont scoreFont, livesFont;


    public PlayState(GameStateManager gameStateManager, int score, int lives, int count) {
        super(gameStateManager);
        initialize();
        this.score = score;
        this.lives = lives;
        this.count = count;
    }

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        initialize();
        score = 0;
        lives = 3;
    }

    private void initialize() {
        superman = new Superman(50, 300);
        camera.setToOrtho(false, FlappyMan.width / 2, FlappyMan.height / 2);
        bg = new Texture("bgroll.png");
        bg1 = new Texture("bgsiang02.png");
        bg2 = new Texture("bgmalam02.png");
        bgPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, 0);
        bgPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + bg1.getWidth(), 0);
        count = 0;
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, groundYOffset);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), groundYOffset);

        tubesArray = new Array<Obstacle>();

        for (int i = 1; i <= obstacleCount; i++) {
            tubesArray.add(new Obstacle(i * (obstacleSpacing + Obstacle.obstacleWidth)));
        }
        gameOverState = new GameOverState(this.gameStateManager, count);
        scoreSfx = Gdx.audio.newSound(Gdx.files.internal("scoreSfx.mp3"));
        passSfx = Gdx.audio.newSound(Gdx.files.internal("passSfx.mp3"));
        lives1 = new Texture("lives1.png");
        lives2 = new Texture("lives2.png");
        lives3 = new Texture("lives3.png");
        scoreFont =  new BitmapFont(Gdx.files.internal("score.fnt"));
        scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        livesFont =  new BitmapFont(Gdx.files.internal("lives.fnt"));
        livesFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            superman.jump();
        }
    }


    @Override
    public void update(float deltaTime) {
        handleInput();
        updateBg();
        updateGround();
        superman.update(deltaTime);
        camera.position.x = superman.getPosition().x + 80;

        for (int i = 0; i < tubesArray.size; i++) {
            Obstacle obstacle = tubesArray.get(i);

            if (camera.position.x - (camera.viewportWidth / 2) > obstacle.getPosTopObstacle().x + obstacle.getTopObstacle().getWidth()) {
                score++;
                if (count == 19) count = 0;
                count = score % 20;
                System.out.println("count: " + count);
                pass();
//                System.out.println("Score: " + score);
                if (score % 10 == 0) {
                    score();
                }
                obstacle.reposition(obstacle.getPosTopObstacle().x + ((Obstacle.obstacleWidth + obstacleSpacing) * obstacleCount));
            }

            if (obstacle.collides(superman.getBounds())) {
                lives--;
//                System.out.println("lives: " + lives);
                gameStateManager.set(livesState = new LivesState(gameStateManager, score, lives, count));
                livesState.timeSfx();
                superman.setPosition(new Vector3(superman.getPosition().x - 100, superman.getPosition().y, 0));
                if (lives == 0) {
                    lives = 3;
                    gameOverState.overSfx();
                    gameStateManager.set(gameOverState);
                }
            }
        }

        if (superman.getPosition().y <= ground.getHeight() + groundYOffset) {
            lives--;
            System.out.println("lives: " + lives);
            gameStateManager.set(livesState = new LivesState(gameStateManager, score, lives, count));
            livesState.timeSfx();
            superman.setPosition(new Vector3(superman.getPosition().x - 100, camera.position.y, 0));
            if (lives == 0) {
                lives = 3;
                gameOverState.overSfx();
                gameStateManager.set(gameOverState);
            }
        }
        camera.update();

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
//        spriteBatch.draw(bg, bgPos1.x, bgPos1.y);
//        spriteBatch.draw(bg, bgPos2.x, bgPos2.y);
        if (count < 10) {
            spriteBatch.draw(bg1, bgPos1.x, bgPos1.y);
            spriteBatch.draw(bg1, bgPos2.x, bgPos2.y);
        }
        else {
            spriteBatch.draw(bg2, bgPos1.x, bgPos1.y);
            spriteBatch.draw(bg2, bgPos2.x, bgPos2.y);
        }
        spriteBatch.draw(superman.getSuperman(), superman.getPosition().x, superman.getPosition().y);
        for (Obstacle obstacle : tubesArray) {
            spriteBatch.draw(obstacle.getTopObstacle(), obstacle.getPosTopObstacle().x, obstacle.getPosTopObstacle().y);
            spriteBatch.draw(obstacle.getBottomObstacle(), obstacle.getPosBottomObstacle().x, obstacle.getPosBottomObstacle().y);
            obstacle.move();
        }

        spriteBatch.draw(ground, groundPos1.x, groundPos1.y);
        spriteBatch.draw(ground, groundPos2.x, groundPos2.y);
        if (lives == 3) spriteBatch.draw(lives3, camera.position.x - lives3.getWidth() / 2, 0);
        else if (lives == 2) spriteBatch.draw(lives2, camera.position.x - lives2.getWidth() / 2, 0);
        else spriteBatch.draw(lives1, camera.position.x - lives1.getWidth() / 2, 0);

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Font font1 = new Font("VT232", Font.BOLD, 40);
        int textwidth1 = (int)(font1.getStringBounds(String.valueOf(score), frc).getWidth());
        scoreFont.draw(spriteBatch, String.valueOf(score), camera.position.x - textwidth1 / 2, 380);
        livesFont.draw(spriteBatch, "LIVES", camera.position.x - lives2.getWidth() / 4, 50);

        spriteBatch.end();

    }

    @Override
    public void dispose() {
        bg.dispose();
        bg1.dispose();
        bg2.dispose();
        superman.dispose();
        ground.dispose();
        scoreSfx.dispose();
        passSfx.dispose();
        for (Obstacle obstacle : tubesArray) {
            obstacle.dispose();
        }
        scoreFont.dispose();
        livesFont.dispose();
//        System.out.println("Play State Disposed");
    }

    private void updateGround() {
        if (camera.position.x - (camera.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }

        if (camera.position.x - (camera.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    private void updateBg() {
        if (camera.position.x - (camera.viewportWidth / 2) > bgPos1.x + bg1.getWidth()) {
            bgPos1.add(bg1.getWidth() * 2, 0);
        }

        if (camera.position.x - (camera.viewportWidth / 2) > bgPos2.x + bg1.getWidth()) {
            bgPos2.add(bg1.getWidth() * 2, 0);
        }
    }

    public void score() {
        scoreSfx.play(0.5f);
    }

    public void pass() {
        passSfx.play(0.5f);
    }
}
