package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.FlappyMan;

public class Superman {
    private static final int gravity = -15;
    private static final int movement = 100;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation supermanAnimation;
    private Texture superman;
    private Sound tapSfx;

    public Superman(int x, int y){
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        superman = new Texture("supermanAnimation.png");
        supermanAnimation = new Animation(new TextureRegion(superman), 3, 0.5f);
        bounds = new Rectangle(x,y, superman.getWidth() / 3, superman.getHeight());
        tapSfx = Gdx.audio.newSound(Gdx.files.internal("sfx.ogg"));
    }

    public void update(float deltaTime){
        supermanAnimation.update(deltaTime);
        if(position.y > 0){
            velocity.add(0, gravity,0);
        }
        velocity.scl(deltaTime);
        position.add(movement * deltaTime,velocity.y,0);
        if(position.y < 0){
            position.y = 0;
        }
        else if (position.y > FlappyMan.height)
            position.y = FlappyMan.height;

        velocity.scl(1/ deltaTime);
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getSuperman() {
        return supermanAnimation.getFrame();
    }

    public void jump(){
        velocity.y = 250;
        tapSfx.play(0.5f);
    }

    public Rectangle getBounds(){
        return  bounds;
    }

    public void dispose(){
        superman.dispose();
        tapSfx.dispose();
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
