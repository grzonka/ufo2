package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MotorJointDef;
import com.badlogic.gdx.utils.Array;
import net.grzonka.ufo2.controller.Content;
import net.grzonka.ufo2.controller.GameStateManager;
import net.grzonka.ufo2.controller.MyContactListener;

public class Game extends ApplicationAdapter {

  public static final String TITLE = "Ufo2";
  public static final int V_WIDTH = 160;
  public static final int V_HEIGHT = 144;
  public static final int SCALE = 1;

  private SpriteBatch spriteBatch;
  private OrthographicCamera camera;


  // old code below
  private static final float MAX_VELOCITY = 70;

  private Texture background;
  private Texture smallBoyTexture;
  private Texture ufoTexture;

  private GameStateManager gameStateManager;

  public static Content resources;

  public static final float STEP = 1 / 60f;
  private float timeAccum;


  private Sprite boySprite;
  private Sprite ufoSprite;
  private Sprite backgroundSprite;

  private World world;
  private Box2DDebugRenderer debugRenderer;
  private Body boyBody;
  private Body groundBody;
  private Body heavenBody;
  private Body startBody;
  private Body endBody;
  private Body ufoBody;
  private final float moveSpeed = 1.5f;
  private final float cameraSpeed = 1.5f;
  private float ufoRotation = 0;

  private MyContactListener customContactListener;

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

    // TODO: clean up this mess and structure it into different classes/packages.

    customContactListener = new MyContactListener();

    // box2d initialisation
    Box2D.init();
  }

  // render() is called each frame. (can be thought of as an event loop)
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