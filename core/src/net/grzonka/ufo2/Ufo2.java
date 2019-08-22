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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.w3c.dom.Entity;

public class Ufo2 extends ApplicationAdapter {

  SpriteBatch batch;
  Texture background;
  Texture smallBoy;
  BitmapFont font;
  Sprite sprite;
  Sprite backgroundSprite;
  Camera camera;
  World world;
  Box2DDebugRenderer debugRenderer;
  Body body;
  Body groundBody;

  @Override
  public void create() {

    // box2d initialisation
    Box2D.init();
    // this vector creates gravity for the world with -10 (m/s^2)
    // 1 Box2d unit = 1 meter.
    world = new World(new Vector2(0, -10), true);
    // debug renderer for box2d
    debugRenderer = new Box2DDebugRenderer();

    batch = new SpriteBatch();
    background = new Texture(Gdx.files.internal("background_10_wide.png"));
    smallBoy = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    backgroundSprite = new Sprite(background);
    sprite = new Sprite(smallBoy);
    camera = new OrthographicCamera(160, 144);
    // moves camera to be centered that the beginning of the background is in place
    camera.translate(80f, 72f, 0f);

    System.out.println("MOVE RIGHT!");
    // TODO: integrate box2d.

    // First we create a body definition
    BodyDef UfoBody = new BodyDef();
    // We set our body to dynamic, for something like ground which doesn't move we would set it to
    // StaticBody
    UfoBody.type = BodyType.DynamicBody;
    // Set our body's starting position in the world
    UfoBody.position.set(100, 100);

    // Create our body in the world using our body definition
    body = world.createBody(UfoBody);
    PolygonShape polyShape = new PolygonShape();
    polyShape.setAsBox(8, 9.5f); // for some reason 1 unit here = 2px
    CircleShape circle = new CircleShape();
    circle.setRadius(6f);

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = polyShape;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0.6f;
    body.setUserData(sprite);

    // Create our fixture and attach it to the body
    Fixture fixture = body.createFixture(fixtureDef);

    // Remember to dispose of any shapes after you are donw with them!
    // BodyDef and FixtureDef do not need disposing, but shapes do.
    circle.dispose();
    polyShape.dispose();

    // Creating static body.
    BodyDef groundBodyDef = new BodyDef();
    // Set its world position
    groundBodyDef.position.set(new Vector2(0, 1));

    // Create a body from the defintion and add it to the world
    groundBody = world.createBody(groundBodyDef);

    // Create a polygon shape
    PolygonShape groundBox = new PolygonShape();
    // Set the polygon shape as a box which is twice the size of our view port and 20 high
    // (setAsBox takes half-width and half-height as arguments)
    groundBox.setAsBox(camera.viewportWidth, 1.0f);
    // Create a fixture from our polygon shape and add it to our ground body
    groundBody.createFixture(groundBox, 0.0f);
    // Clean up after ourselves
    groundBox.dispose();


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

    batch.begin();

    Array<Body> bodies = new Array<>();
    bodies.add(body, groundBody);

    world.getBodies(bodies);
    for (Body b : bodies) {
      Sprite e = (Sprite) b.getUserData();

      if (e != null) {
        e.setPosition(b.getPosition().x, b.getPosition().y);
        e.draw(batch);
      }
    }

    // System.out.println("Sprites location is: (" + sprite.getX() + "," + sprite.getY() + ").");
    // System.out.println(camera.combined);

    // camera setup:
    batch.setProjectionMatrix(camera.combined);
    // continous update not bound to movement yet, just a sidescroller for now
    //camera.translate(0.03f,0f,0f);
    camera.update();


    backgroundSprite.draw(batch);
    sprite.draw(batch);

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
    smallBoy.dispose();
  }
}