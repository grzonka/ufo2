package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import net.grzonka.ufo2.controller.Content;
import net.grzonka.ufo2.controller.GameStateManager;

public class Game extends ApplicationAdapter {

  public static final String TITLE = "Ufo2 - early release.";
  public static final int V_WIDTH = 160;
  public static final int V_HEIGHT = 144;
  public static final int SCALE = 8;

  private SpriteBatch spriteBatch;
  private OrthographicCamera camera;

  private GameStateManager gameStateManager;

  public static Content resources;

  public static final float STEP = 1 / 60f;
  private float timeAccum;

  public SpriteBatch getSpriteBatch() {
    return spriteBatch;
  }

  public OrthographicCamera getCamera() {
    return camera;
  }

  @Override
  public void create() {

    resources = new Content();

    spriteBatch = new SpriteBatch();
    camera = new OrthographicCamera(V_WIDTH, V_HEIGHT);
    camera.translate(80f, 72f, 0f);

    gameStateManager = new GameStateManager(this);

    // box2d initialisation // TODO: need to decide wether or not this is needed.
    Box2D.init();
  }

  // render() is called each frame.
  @Override
  public void render() {

    timeAccum += Gdx.graphics.getDeltaTime();
    while (timeAccum >= STEP) {
      timeAccum -= STEP;
      gameStateManager.update(STEP);
      gameStateManager.render();
    }
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
    //gameStateManager.dispose
  }
}