package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Ufo2 extends ApplicationAdapter {

  private static final float MAX_VELOCITY = 100;
  private SpriteBatch batch;
  private Texture background;
  private Texture smallBoyTexture;
  private Texture ufoTexture;

  private Sprite boySprite;
  private Sprite ufoSprite;
  private Sprite backgroundSprite;
  private Camera camera;
  private World world;
  private Box2DDebugRenderer debugRenderer;
  private Body boyBody;
  private Body groundBody;
  private Body ufoBody;
  private final float moveSpeed = 1.5f;
  private final float cameraSpeed = 1.5f;


  @Override
  public void create() {

    // box2d initialisation
    Box2D.init();
    // this vector creates gravity for the world with -10 (m/s^2)
    // 1 Box2d unit = 1 meter.
    world = new World(new Vector2(0, -100), true);
    // debug renderer for box2d
    debugRenderer = new Box2DDebugRenderer();

    batch = new SpriteBatch();
    background = new Texture(Gdx.files.internal("background_10_wide.png"));
    smallBoyTexture = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    ufoTexture = new Texture(Gdx.files.internal("ufo_transparent_0_zap.png")); // 0 - 6 are zap
    // intensites going from none to full zap
    backgroundSprite = new Sprite(background);
    boySprite = new Sprite(smallBoyTexture);
    ufoSprite = new Sprite(ufoTexture);
    camera = new OrthographicCamera(160, 144);
    // moves camera to be centered that the beginning of the background is in place
    camera.translate(80f, 72f, 0f);

    System.out.println("MOVE RIGHT!");
    // TODO: integrate box2d.

    BodyDef UfoBodyDef = new BodyDef();
    UfoBodyDef.type = BodyType.DynamicBody;
    UfoBodyDef.position.set(60, 120);
    ufoBody = world.createBody(UfoBodyDef);
    PolygonShape ufoShape = new PolygonShape();
    ufoShape.setAsBox(20, 7.5f);
    FixtureDef ufoFixtureDef = new FixtureDef();
    ufoFixtureDef.shape = ufoShape;
    /*ufoFixtureDef.density = 0.5f;
    ufoFixtureDef.friction = 0.4f;
    ufoFixtureDef.restitution = 0f;*/
    ufoBody.setUserData(ufoSprite);
    Fixture ufoFixture = ufoBody.createFixture(ufoFixtureDef);

    ufoShape.dispose();

    // First we create a boyBody definition
    BodyDef BoyBodyDef = new BodyDef();
    // We set our boyBody to dynamic, for something like ground which doesn't move we would set it to
    // StaticBody
    BoyBodyDef.type = BodyType.DynamicBody;
    // Set our boyBody's starting position in the world
    BoyBodyDef.position.set(100, 100);

    // Create our boyBody in the world using our boyBody definition
    boyBody = world.createBody(BoyBodyDef);
    PolygonShape polyShape = new PolygonShape();
    polyShape.setAsBox(8, 9.5f); // for some reason 1 unit here = 2px
    /*CircleShape circle = new CircleShape();
    circle.setRadius(6f);*/

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = polyShape;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0f;
    boyBody.setUserData(boySprite);

    // Create our fixture and attach it to the boyBody
    // necessary even tho return value is never used.
    Fixture fixture = boyBody.createFixture(fixtureDef);

    // Remember to dispose of any shapes after you are donw with them!
    // BodyDef and FixtureDef do not need disposing, but shapes do.
    /*circle.dispose();*/
    polyShape.dispose();

    // Creating static boyBody.
    BodyDef groundBodyDef = new BodyDef();
    // Set its world position
    groundBodyDef.position.set(new Vector2(0, 1));
    // Create a boyBody from the defintion and add it to the world
    groundBody = world.createBody(groundBodyDef);
    // Create a polygon shape
    PolygonShape groundBox = new PolygonShape();
    // Set the polygon shape as a box which is twice the size of our view port and 20 high
    // (setAsBox takes half-width and half-height as arguments)
    groundBox.setAsBox(camera.viewportWidth, 1.0f);
    // Create a fixture from our polygon shape and add it to our ground boyBody
    groundBody.createFixture(groundBox, 0.0f);
    // Clean up after ourselves
    groundBox.dispose();
    //ufoBody.setLinearVelocity(0.0f, 1.0f);
//ufoBody.applyForceToCenter(10f,0f,true);
  }

  // render() is called each frame. (can be thought of as an event loop)
  @Override
  public void render() {

    // gdx.gl is exposing OpenGL functionality
    Gdx.gl.glClearColor(255, 255, 255, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Ufo movement
    Vector2 vel = ufoBody.getLinearVelocity();
    Vector2 pos = ufoBody.getPosition();
    // this permanent force resists gravity.
    ufoBody.applyForceToCenter(0f, 100f, true);
// apply left impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.LEFT) && vel.x > -MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(-moveSpeed, 0, pos.x, pos.y, true);
    }
// apply right impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.RIGHT) && vel.x < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(moveSpeed, 0, pos.x, pos.y, true);

    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP) && vel.y < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(0, moveSpeed, pos.x, pos.y, true);
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN) && vel.y < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(0, -moveSpeed, pos.x, pos.y, true);
    }
    // System.out.println(ufoBody.getPosition().x   + "     " + pos.x);
    //System.out.println(pos.x);
    if (pos.x < camera.position.x - 50) {
      camera.translate(-1, 0, 0);
    }

    if (pos.x > camera.position.x + 50) {
      camera.translate(1, 0, 0);
    }

    /*System.out.println("Ufo at: " + pos.x + "    camera at: " + camera.position.x + "     speed: "
        + vel);*/





    /*if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      // this would be manipulation of just the boySprite, we want to manipulate box2d objects
      // instead.
      // boySprite.translateX(-1f);
      ufoBody.setTransform(ufoBody.getPosition().x - moveSpeed, ufoBody.getPosition().y, 0);

    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      ufoBody.setTransform(ufoBody.getPosition().x + moveSpeed, ufoBody.getPosition().y, 0);

    }*/

    /*if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      ufoBody.setTransform(ufoBody.getPosition().x, ufoBody.getPosition().y + moveSpeed, 0);
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN)) {
      ufoBody.setTransform(ufoBody.getPosition().x, ufoBody.getPosition().y - moveSpeed, 0);
    }*/

    if (Gdx.input.isKeyPressed(Keys.R)) {
      boySprite.rotate(4f);
    }

    batch.begin();

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
        e.draw(batch);
      }
    }

    // TODO: decide if array declaration here is wanted/preferred, since it is not needed to run.
    /*Array<Body> ufoBodies = new Array<>();
    ufoBodies.add(ufoBody);
    world.getBodies(ufoBodies);*/

    Sprite e = (Sprite) ufoBody.getUserData();
    if (e != null) {
      //System.out.println(e);
      e.setPosition(ufoBody.getPosition().x - 20f, ufoBody.getPosition().y - 7.5f);
      e.draw(batch);
    }

    // System.out.println("Sprites location is: (" + boySprite.getX() + "," + boySprite.getY() + ").");
    // System.out.println(camera.combined);

    // camera setup:
    batch.setProjectionMatrix(camera.combined);
    // continous update not bound to movement yet, just a sidescroller for now
    // camera.translate(0.03f,0f,0f);
    camera.update();

    backgroundSprite.draw(batch);
    boySprite.draw(batch);
    ufoSprite.draw(batch);
    batch.end();

    // simulation of box2d should be made here at the end of render:
    world.step(1 / 60f, 6, 2);
    debugRenderer.render(world, camera.combined);
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
    smallBoyTexture.dispose();
    ufoTexture.dispose();
  }
}