package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ufo2 extends ApplicationAdapter {

  SpriteBatch batch;
  Texture background;
  Texture smallBoy;
  BitmapFont font;
  Sprite sprite;
  Sprite backgroundSprite;

  @Override
  public void create() {
    batch = new SpriteBatch();
    background = new Texture(Gdx.files.internal("background.png"));
    smallBoy = new Texture(Gdx.files.internal("small_boy.png"));
    backgroundSprite = new Sprite(background);
    sprite = new Sprite(smallBoy);
  }

  // render() is called each frame. (can be thought of as an event loop)
  @Override
  public void render() {
    // gdx.gl is exposing OpenGL functionality
    Gdx.gl.glClearColor(255, 255, 255, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      sprite.translateX(-3f);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      sprite.translateX(3f);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      sprite.translateY(3f);
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN)) {
      sprite.translateY(-3f);
    }

    batch.begin();
    backgroundSprite.draw(batch);
    sprite.draw(batch);
    batch.end();
  }

  @Override
  public void resize(int width, int height) {
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
    batch.dispose();
    background.dispose();
    smallBoy.dispose();
  }
}