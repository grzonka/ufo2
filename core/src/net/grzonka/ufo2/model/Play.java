package net.grzonka.ufo2.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import net.grzonka.ufo2.Game;
import net.grzonka.ufo2.controller.GameStateManager;
import net.grzonka.ufo2.controller.MyContactListener;
import org.w3c.dom.Text;

public class Play extends GameState {

  private World world;
  private MyContactListener customContactListener;
  private Box2DDebugRenderer debugRenderer;

  private Texture background;
  private Sprite backgroundSprite;

  private Texture ufoTexture;
  private Sprite ufoSprite;
  private Body ufoBody;

  private Texture boyTexture;
  private Sprite boySprite;
  private Body boyBody;

  private OrthographicCamera camera;


  // variables for input handeling
  final float MAX_VELOCITY = 70;
  final float moveSpeed = 1.5f;
  final float cameraSpeed = 1.5f;
  float ufoRotation = 0;

  public Play(GameStateManager gsm) {
    super(gsm);
    customContactListener = new MyContactListener();

    background = new Texture(Gdx.files.internal("background_10_wide.png"));
    backgroundSprite = new Sprite(background);

    world = new World(new Vector2(0, -10f), true);
    world.setContactListener(customContactListener);

    debugRenderer = new Box2DDebugRenderer();

    // create boundaries
    // first top and bottom boundaries
    BodyDef topBottomBodyDef = new BodyDef();
    topBottomBodyDef.type = BodyType.StaticBody;
    topBottomBodyDef.position.set(new Vector2(1000, 144.5f));
    Body topBody = world.createBody(topBottomBodyDef);
    topBottomBodyDef.position.set(new Vector2(1000, -0.5f));
    Body bottomBody = world.createBody(topBottomBodyDef);

    PolygonShape topBottomBox = new PolygonShape();
    topBottomBox.setAsBox(1000, 1.0f);
    topBody.createFixture(topBottomBox, 0.0f);
    bottomBody.createFixture(topBottomBox, 0.0f);
    topBottomBox.dispose();

    // creating start and end boundaries
    BodyDef startEndBodyDef = new BodyDef();
    startEndBodyDef.type = BodyType.StaticBody;
    startEndBodyDef.position.set(-0.5f, 80);
    Body startBody = world.createBody(startEndBodyDef);
    startEndBodyDef.position.set(1600.5f, 80);
    Body endBody = world.createBody(startEndBodyDef);

    PolygonShape startEndBox = new PolygonShape();
    startEndBox.setAsBox(1, 144);
    startBody.createFixture(startEndBox, 0.0f);
    endBody.createFixture(startEndBox, 0.0f);
    startEndBox.dispose();

    // creating ufo
    ufoTexture = new Texture(Gdx.files.internal("ufo_transparent_0_zap.png")); // 0 - 6 are zap
    ufoSprite = new Sprite(ufoTexture);

    BodyDef UfoBodyDef = new BodyDef();
    UfoBodyDef.type = BodyType.DynamicBody;
    UfoBodyDef.position.set(60, 120);
    ufoBody = world.createBody(UfoBodyDef);

    PolygonShape ufoShape = new PolygonShape();
    ufoShape.setAsBox(20, 7.5f);
    FixtureDef ufoFixtureDef = new FixtureDef();
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.density = 0f;
    ufoFixtureDef.friction = 0.4f;
    ufoFixtureDef.restitution = 0.0f;
    ufoFixtureDef.filter.categoryBits = B2DVars.BIT_UFO;
    ufoBody.setUserData(ufoSprite);
    ufoBody.createFixture(ufoFixtureDef).setUserData("player");

    ufoShape.setAsBox(.5f, 30f, new Vector2(0, -30), 0);
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.isSensor = true;
    // maybe need to insert filter with human here to get this sorted.
    ufoBody.createFixture(ufoFixtureDef).setUserData("sensor");
    ufoShape.dispose();

    // creating smallBoy
    boyTexture = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    boySprite = new Sprite(boyTexture);

    BodyDef boyBodyDef = new BodyDef();
    boyBodyDef.type = BodyType.DynamicBody;
    boyBodyDef.position.set(100, 100);
    boyBody = world.createBody(boyBodyDef);

    PolygonShape boyShape = new PolygonShape();
    boyShape.setAsBox(8, 9.5f); // for some reason 1 unit here = 2px
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = boyShape;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0f;
    fixtureDef.filter.categoryBits = B2DVars.BIT_HUMAN;
    boyBody.setUserData(boySprite);
    boyBody.createFixture(fixtureDef).setUserData("human");
    boyShape.dispose();

    // setting up camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

  }

  public void handleInput() {

    // ufo movement
    Vector2 vel = ufoBody.getLinearVelocity();
    Vector2 pos = ufoBody.getPosition();
    ufoBody.applyForceToCenter(0f, 10f, true);
    // apply left impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.LEFT) && vel.x > -MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(-moveSpeed, 0, pos.x, pos.y, true);
      if (ufoRotation < 0.3) {
        ufoRotation += 0.1f;
      }
    }
    if (Gdx.input.isKeyPressed(Keys.RIGHT) && vel.x < MAX_VELOCITY) {
      ufoBody.applyLinearImpulse(moveSpeed, 0, pos.x, pos.y, true);
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
    camera.update();

    if (Gdx.input.isKeyPressed(Keys.SPACE) && customContactListener.isHumanSpotted()) {
      System.out.println("ZAP!!!");
      Body human = customContactListener.getHuman();
      // removing fixture around human in order for them to disappear.
      // TODO: make this more interesting to watch maybe
      if (human != null) {
        human.destroyFixture(human.getFixtureList().get(0));
      }
    }
  }

  public void update(float dt) {

    handleInput();

    world.step(dt, 8, 3);

  }

  public void render() {
    // clearing screen first

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    spriteBatch.setProjectionMatrix(camera.combined);

    spriteBatch.begin();

    // rendering humans
    Array<Body> dummyBodies = new Array<>();
    dummyBodies.add(boyBody);
    world.getBodies(dummyBodies);
    for (Body b : dummyBodies) {
      Sprite e = (Sprite) b.getUserData();
      if (e != null) {
        e.setPosition(b.getPosition().x - 8f, b.getPosition().y - 9.5f);
        e.draw(spriteBatch);
      }
    }

    // rendering ufo
    Sprite e = (Sprite) ufoBody.getUserData();
    if (e != null) {
      //System.out.println(e);
      e.setPosition(ufoBody.getPosition().x - 20f, ufoBody.getPosition().y - 7.5f);
      e.setRotation(ufoRotation * 30);
      e.draw(spriteBatch);
    }

    backgroundSprite.draw(spriteBatch);
    boySprite.draw(spriteBatch);
    ufoSprite.draw(spriteBatch);
    spriteBatch.end();
    debugRenderer.render(world, camera.combined);
  }

  public void dispose() {
    spriteBatch.dispose();
    //background.dispose();
    boyTexture.dispose();
    ufoTexture.dispose();
  }
}
