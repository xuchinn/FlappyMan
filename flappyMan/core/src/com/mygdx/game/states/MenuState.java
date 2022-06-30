package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.FlappyMan;

public class MenuState extends State{
    private Texture background;
    private Texture playButton;
    private Texture title;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, FlappyMan.width / 2, FlappyMan.height / 2);
        background = new Texture("bgsiang02.png");
        playButton = new Texture("playbtn.png");
        title = new Texture("title.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            gameStateManager.set(new PlayState(gameStateManager));
        }

    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background,0,0);
        spriteBatch.draw(playButton, camera.position.x - playButton.getWidth() / 2, camera.position.y - 10);
        spriteBatch.draw(title, camera.position.x - title.getWidth() / 2, camera.position.y + playButton.getHeight());
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
//        System.out.println("Menu State Disposed");
    }
}
