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
import net.grzonka.ufo2.controller.GameStateManager;
import net.grzonka.ufo2.controller.MyContactListener;

public class Game extends ApplicationAdapter {

  public static final String TITLE = "Ufo2";
  public static final int V_WIDTH = 160;
  public static final int V_HEIGHT = 144;

  private SpriteBatch spriteBatch;
  private OrthographicCamera camera;


  // old code below
  private static final float MAX_VELOCITY = 70;

  private Texture background;
  private Texture smallBoyTexture;
  private Texture ufoTexture;

  private GameStateManager gameStateManager;

  public static final float STEP = 1/60f;
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

  public SpriteBatch getSpriteBatch(){
    return spriteBatch;
  }
  public OrthographicCamera getCamera(){
    return camera;
  }

  @Override
  public void create() {

    spriteBatch = new SpriteBatch();
    camera = new OrthographicCamera(160, 144);
    camera.translate(80f, 72f, 0f);


    gameStateManager = new GameStateManager(this);

    // TODO: clean up this mess and structure it into different classes/packages.

    customContactListener = new MyContactListener();

    // box2d initialisation
    Box2D.init();
    // this vector creates gravity for the world with -10 (m/s^2)
    // 1 Box2d unit = 1 meter.
    /*world = new World(new Vector2(0, -100), true);
    world.setContactListener(customContactListener);
    // debug renderer for box2d
    debugRenderer = new Box2DDebugRenderer();*/

    //background = new Texture(Gdx.files.internal("background_10_wide.png"));
    /*smallBoyTexture = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    ufoTexture = new Texture(Gdx.files.internal("ufo_transparent_0_zap.png")); // 0 - 6 are zap
    // intensites going from none to full zap
    backgroundSprite = new Sprite(background);
    boySprite = new Sprite(smallBoyTexture);*/
    //ufoSprite = new Sprite(ufoTexture);
    //camera = new OrthographicCamera(160, 144);
    // moves camera to be centered that the beginning of the background is in place
    //camera.translate(80f, 72f, 0f);

    /*BodyDef UfoBodyDef = new BodyDef();
    UfoBodyDef.type = BodyType.DynamicBody;
    UfoBodyDef.position.set(60, 120);
    ufoBody = world.createBody(UfoBodyDef);
    PolygonShape ufoShape = new PolygonShape();
    ufoShape.setAsBox(20, 7.5f);
    FixtureDef ufoFixtureDef = new FixtureDef();
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.density = 0f;
    ufoFixtureDef.friction = 0.4f;
    ufoFixtureDef.restitution = 0.1f;
    ufoBody.setUserData(ufoSprite);
    ufoBody.createFixture(ufoFixtureDef).setUserData("player");

    ufoShape.setAsBox(.5f, 30f, new Vector2(0, -30), 0);
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.isSensor = true;
    ufoBody.createFixture(ufoFixtureDef).setUserData("sensor");
    ufoShape.dispose();*/

    /*// First we create a boyBody definition
    BodyDef boyBodyDef = new BodyDef();
    // We set our boyBody to dynamic, for something like ground which doesn't move we would set it to
    // StaticBody
    boyBodyDef.type = BodyType.DynamicBody;
    // Set our boyBody's starting position in the world
    boyBodyDef.position.set(100, 100);

    // Create our boyBody in the world using our boyBody definition
    boyBody = world.createBody(boyBodyDef);
    PolygonShape polyShape = new PolygonShape();
    polyShape.setAsBox(8, 9.5f); // for some reason 1 unit here = 2px
    *//*CircleShape circle = new CircleShape();
    circle.setRadius(6f);*//*

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = polyShape;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0f;
    boyBody.setUserData(boySprite);

    // Create our fixture and attach it to the boyBody
    // necessary even tho return value is never used.
    boyBody.createFixture(fixtureDef).setUserData("human");

    // Remember to dispose of any shapes after you are donw with them!
    // BodyDef and FixtureDef do not need disposing, but shapes do.
    *//*circle.dispose();*//*
    polyShape.dispose();*/

    // Creating static Body.
    /*BodyDef topBottomBodyDef = new BodyDef();
    BodyDef startEndBodyDef = new BodyDef();
    // Set its world position
    startEndBodyDef.position.set(-0.5f, 80);
    topBottomBodyDef.position.set(new Vector2(1000, -0.5f));
    // Create a Body from the defintion and add it to the world
    groundBody = world.createBody(topBottomBodyDef);
    startBody = world.createBody(startEndBodyDef);
    topBottomBodyDef.position.set(new Vector2(1000, 144.5f));
    startEndBodyDef.position.set(1600.5f, 80);
    heavenBody = world.createBody(topBottomBodyDef);
    endBody = world.createBody(startEndBodyDef);
    // Create a polygon shape
    PolygonShape topBottomBox = new PolygonShape();
    PolygonShape startEndBox = new PolygonShape();
    // Set the polygon shape as a box which is twice the size of our view port and 20 high
    // (setAsBox takes half-width and half-height as arguments)
    topBottomBox.setAsBox(1000, 1.0f);
    startEndBox.setAsBox(1, 144);
    // Create a fixture from our polygon shape and add it to our ground boyBody
    groundBody.createFixture(topBottomBox, 0.0f);
    heavenBody.createFixture(topBottomBox, 0.0f);
    startBody.createFixture(startEndBox, 0.0f);
    endBody.createFixture(startEndBox, 0.0f);
    // Clean up after ourselves
    topBottomBox.dispose();
    //ufoBody.setLinearVelocity(0.0f, 1.0f);
    //ufoBody.applyForceToCenter(10f,0f,true);

    MotorJointDef jointDef = new MotorJointDef();
    jointDef.angularOffset = 0f;
    jointDef.collideConnected = true;
    jointDef.correctionFactor = 0.5f;
    jointDef.maxForce = 1f;
    jointDef.maxTorque = 1f;
    jointDef.initialize(boyBody, ufoBody);*/
  }

  // render() is called each frame. (can be thought of as an event loop)
  @Override
  public void render() {

    timeAccum += Gdx.graphics.getDeltaTime();
    while (timeAccum >= STEP){
      timeAccum -= STEP;
      gameStateManager.update(STEP);
      gameStateManager.render();
    }
    // gdx.gl is exposing OpenGL functionality
    //Gdx.gl.glClearColor(255, 255, 255, 1);
    /*Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Ufo movement
    Vector2 vel = ufoBody.getLinearVelocity();
    Vector2 pos = ufoBody.getPosition();
    // this permanent force resists gravity.
    ufoBody.applyForceToCenter(0f, 100f, true);
    // apply left impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.LEFT) && vel.x > -MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(-moveSpeed, 0, pos.x, pos.y, true);
      *//*if (ufoBody.getAngle() < 0.3) {
        //ufoBody.setTransform(pos.x, pos.y, ufoBody.getAngle() + 0.1f);
        // TODO: decide whether or not rotation of ufoBody is that useful or if rotation should only
        //  be applied to sprite.
      }*//*
      if (ufoRotation < 0.3) {
        ufoRotation += 0.1f;
      }

      //System.out.println(ufoBody.getAngle());
    }
    // apply right impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.RIGHT) && vel.x < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(moveSpeed, 0, pos.x, pos.y, true);
      *//*if (ufoBody.getAngle() > -0.3) {
        // ufoBody.setTransform(pos.x, pos.y, ufoBody.getAngle() - 0.1f);

        // TODO: decide whether or not rotation of ufoBody is that useful or if rotation should only
        //  be applied to sprite.
      }*//*
      if (ufoRotation > -0.3) {
        ufoRotation -= 0.1f;
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP) && vel.y < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(0, moveSpeed, pos.x, pos.y, true);
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN) && vel.y < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(0, -moveSpeed, pos.x, pos.y, true);
    }
    if (pos.x < camera.position.x - 50 && camera.position.x > 80) {
      camera.translate(-1, 0, 0);
    }
    if (pos.x > camera.position.x + 50 && camera.position.x < 1520) {
      camera.translate(1, 0, 0);
    }

    if (Gdx.input.isKeyPressed(Keys.SPACE) && customContactListener.isHumanSpotted()) {
      System.out.println("ZAP!!!");

      Body human = customContactListener.getHuman();

      // removing fixture around human in order for them to disappear.
      // TODO: make this more interesting to watch maybe
      if (human != null) {
        human.destroyFixture(human.getFixtureList().get(0));
      }

    }

    // starts libgdx rendering queue.
    spriteBatch.begin();

    // This will render all humans correctly
    // TODO: for some reason the ufo sprite gets used in this loop and therefore rendered here
    //  aswell. Interestingly the ufo model rendered here is behind the "background".
    Array<Body> dummyBodies = new Array<>();
    dummyBodies.add(boyBody, groundBody);
    world.getBodies(dummyBodies);
    for (Body b : dummyBodies) {
      Sprite e = (Sprite) b.getUserData();
      if (e != null) {
        e.setPosition(b.getPosition().x - 8f, b.getPosition().y - 9.5f);
        e.draw(spriteBatch);
      }
    }

    // TODO: decide if array declaration here is wanted/preferred, since it is not needed to run.
    *//*Array<Body> ufoBodies = new Array<>();
    ufoBodies.add(ufoBody);
    world.getBodies(ufoBodies);*//*

    Sprite e = (Sprite) ufoBody.getUserData();
    if (e != null) {
      //System.out.println(e);
      e.setPosition(ufoBody.getPosition().x - 20f, ufoBody.getPosition().y - 7.5f);
      e.setRotation(ufoRotation * 30);
      e.draw(spriteBatch);
    }

    // System.out.println("Sprites location is: (" + boySprite.getX() + "," + boySprite.getY() + ").");
    // System.out.println(camera.combined);

    // camera setup:
    spriteBatch.setProjectionMatrix(camera.combined);
    // continous update not bound to movement yet, just a sidescroller for now
    // camera.translate(0.03f,0f,0f);
    camera.update();

    // TODO: add procedural world generation. easy solution seems to be random int (1..3) for
    //  length combined with random height for box (1/n) of height where n (2..8) or so.
    //  best to actually move this to a seperate class "RandomBlocks"

    backgroundSprite.draw(spriteBatch);
    boySprite.draw(spriteBatch);
    //ufoSprite.setRotation(ufoBody.getAngle()*30);
    ufoSprite.draw(spriteBatch);
    spriteBatch.end();

    // simulation of box2d should be made here at the end of render:
    world.step(STEP, 6, 2);
    debugRenderer.render(world, camera.combined);*/
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