package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.FlappyMan;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;


public class LivesState extends State {
    private Texture background, bg1, bg2;
    private int lives, score;
    private Music time;
    private BitmapFont clickAnywhere;
    private int count;
    private final String text = "CLICK ANYWHERE\nTO CONTINUE...";
    public LivesState(GameStateManager gameStateManager, int score, int lives, int count) {
        super(gameStateManager);
        camera.setToOrtho(false, FlappyMan.width / 2, FlappyMan.height / 2);
//        background = new Texture("bgroll.png");
        bg1 = new Texture("bgsiang02.png");
        bg2 = new Texture("bgmalam02.png");
        time = Gdx.audio.newMusic(Gdx.files.internal("time.mp3"));
        this.lives = lives;
        this.score = score;
        this.count = count;
        clickAnywhere =  new BitmapFont(Gdx.files.internal("clickAnywhere.fnt"));
        clickAnywhere.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            time.stop();
            FlappyMan.music.play();
            gameStateManager.set(new PlayState(gameStateManager, score, lives, count));
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
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Font font1 = new Font("VT232", Font.BOLD, 30);
        int textwidth1 = (int)(font1.getStringBounds(text, frc).getWidth());
        int textheight1 = (int)(font1.getStringBounds(text, frc).getHeight());
        clickAnywhere.draw(spriteBatch, text, camera.position.x - textwidth1 / 6, camera.position.y + textheight1);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
//        background.dispose();
        bg1.dispose();
        bg2.dispose();
        clickAnywhere.dispose();
        time.dispose();
    }

    public void timeSfx() {
        FlappyMan.music.pause();
        time.setVolume(0.5f);
        time.setLooping(true);
        time.play();
    }
}
