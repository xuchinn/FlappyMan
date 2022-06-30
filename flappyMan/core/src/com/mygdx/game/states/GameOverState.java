package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.FlappyMan;

public class GameOverState extends State{
    private Texture background, bg1, bg2;
    private Texture gameOver;
    private Texture playButton;
    private Sound gameOverSfx;
    private int count;

    public GameOverState(GameStateManager gameStateManager, int count) {
        super(gameStateManager);
        camera.setToOrtho(false, FlappyMan.width / 2, FlappyMan.height / 2);
//        background = new Texture("bgroll.png");
        bg1 = new Texture("bgsiang02.png");
        bg2 = new Texture("bgmalam02.png");
        gameOver = new Texture("gameOver.png");
        playButton = new Texture("playbtn.png");
        gameOverSfx = Gdx.audio.newSound(Gdx.files.internal("gameOverSfx.mp3"));
        this.count = count;
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            FlappyMan.music.play();
            gameStateManager.set(new MenuState(gameStateManager));
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
//        spriteBatch.draw(background,0,0);
        if (count < 10) {
            spriteBatch.draw(bg1,0,0);
        }
        else {
            spriteBatch.draw(bg2,0,0);
        }
        spriteBatch.draw(gameOver, camera.position.x - gameOver.getWidth() / 2, camera.position.y);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
//        background.dispose();
        bg1.dispose();
        bg2.dispose();
        playButton.dispose();
        gameOverSfx.dispose();
//        System.out.println("Game Over State Disposed");
    }

    public void overSfx() {
        FlappyMan.music.stop();
        gameOverSfx.play(0.5f);
    }
}
