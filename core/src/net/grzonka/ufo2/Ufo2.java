package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
  Camera camera;

  @Override
  public void create() {
    batch = new SpriteBatch();
    background = new Texture(Gdx.files.internal("background_10_wide.png"));
    smallBoy = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    backgroundSprite = new Sprite(background);
    sprite = new Sprite(smallBoy);
    camera = new OrthographicCamera(160,144);
    // moves camera to be centered that the beginning of the background is in place
    camera.translate(80f,72f,0f);

    System.out.println("MOVE RIGHT!");
    // TODO: integrate box2d.

  }

  // render() is called each frame. (can be thought of as an event loop)
  @Override
  public void render() {
    // gdx.gl is exposing OpenGL functionality
    Gdx.gl.glClearColor(255, 255, 255, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      sprite.translateX(-1f);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      sprite.translateX(1f);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      sprite.translateY(1f);
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN)) {
      sprite.translateY(-1f);
    }
    if (Gdx.input.isKeyPressed(Keys.R)) {
      sprite.rotate(4f);
    }

    // System.out.println("Sprites location is: (" + sprite.getX() + "," + sprite.getY() + ").");
    // System.out.println(camera.combined);

    // camera setup:
    batch.setProjectionMatrix(camera.combined);
    // continous update not bound to movement yet, just a sidescroller for now
    camera.translate(0.3f,0f,0f);
    camera.update();

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