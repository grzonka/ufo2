package net.grzonka.ufo2.model;

import static net.grzonka.ufo2.model.B2DVars.BIT_BORDER;
import static net.grzonka.ufo2.model.B2DVars.BIT_HUMAN;
import static net.grzonka.ufo2.model.B2DVars.BIT_UFO;
import static net.grzonka.ufo2.model.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

  private HumanMachine humanMachine;

  private Sound soundEffectWarp;
  private final Sound soundEffectTheme;

  private OrthographicCamera camera;


  // variables for input handeling
  final float MAX_VELOCITY = 70;
  final float moveSpeed = 1.5f;
  final float cameraSpeed = 1.5f;
  float ufoRotation = 0;
  private int srcX;


  public Play(GameStateManager gsm) {
    super(gsm);
    customContactListener = new MyContactListener();

    background = new Texture(Gdx.files.internal("background_10_wide.png"), true);
    background.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    backgroundSprite = new Sprite(background);

    world = new World(B2DVars.GRAVITY, true); // Vector2 creates gravity
    world.setContactListener(customContactListener);

    soundEffectWarp = Gdx.audio.newSound(Gdx.files.internal("sound/warp.ogg"));
    soundEffectTheme = Gdx.audio.newSound(Gdx.files.internal("sound/theme.ogg"));

    soundEffectTheme.loop(1f);

    debugRenderer = new Box2DDebugRenderer();

    // create boundaries
    // first top and bottom boundaries
    BodyDef topBottomBodyDef = new BodyDef();
    topBottomBodyDef.type = BodyType.StaticBody;
    topBottomBodyDef.position.set(new Vector2(1000 / PPM, 144.5f / PPM));
    Body topBody = world.createBody(topBottomBodyDef);
    topBottomBodyDef.position.set(new Vector2(1000 / PPM, -0.5f / PPM));
    Body bottomBody = world.createBody(topBottomBodyDef);

    PolygonShape topBottomBox = new PolygonShape();
    topBottomBox.setAsBox(1000 / PPM, 1.0f / PPM);
    FixtureDef topBottomFixtureDef = new FixtureDef();
    topBottomFixtureDef.shape = topBottomBox;
    topBottomFixtureDef.density = 0f;
    topBottomFixtureDef.friction = 0;
    topBottomFixtureDef.restitution = 0;
    topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BORDER;
    topBottomFixtureDef.filter.maskBits = BIT_HUMAN | BIT_UFO;
    topBody.createFixture(topBottomFixtureDef).setUserData("border");
    bottomBody.createFixture(topBottomFixtureDef).setUserData("border");
    topBottomBox.dispose();

    // creating start and end boundaries
    BodyDef startEndBodyDef = new BodyDef();
    startEndBodyDef.type = BodyType.StaticBody;
    startEndBodyDef.position.set(-0.5f / PPM, 80 / PPM);
    Body startBody = world.createBody(startEndBodyDef);
    startEndBodyDef.position.set(160.5f / PPM, 80 / PPM);
    Body endBody = world.createBody(startEndBodyDef);

    PolygonShape startEndBox = new PolygonShape();
    startEndBox.setAsBox(1 / PPM, 144 / PPM);
    FixtureDef startEndFixtureDef = new FixtureDef();
    startEndFixtureDef.shape = startEndBox;
    startEndFixtureDef.density = 0f;
    startEndFixtureDef.friction = 0;
    startEndFixtureDef.restitution = 0;
    startEndFixtureDef.filter.categoryBits = B2DVars.BIT_BORDER;
    startEndFixtureDef.filter.maskBits = BIT_HUMAN | BIT_UFO;
    startBody.createFixture(startEndFixtureDef).setUserData("border");
    endBody.createFixture(startEndFixtureDef).setUserData("border");
    startEndBox.dispose();

    // creating ufo
    ufoTexture = new Texture(Gdx.files.internal("ufo_transparent_0_zap.png")); // 0 - 6 are zap
    ufoSprite = new Sprite(ufoTexture);
    ufoSprite.setScale(0.1f);

    BodyDef UfoBodyDef = new BodyDef();
    UfoBodyDef.type = BodyType.DynamicBody;
    UfoBodyDef.position.set(60 / PPM, 120 / PPM);
    ufoBody = world.createBody(UfoBodyDef);

    PolygonShape ufoShape = new PolygonShape();
    ufoShape.setAsBox(20 / PPM, 7.5f / PPM); // ufo size in meter: (20*7.5) = 150m^2
    FixtureDef ufoFixtureDef = new FixtureDef();
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.density = 0f; // ufo density (kg/m^2)
    // results in ufo mass = 150kg (affected by gravity with -10m/s^2)
    ufoFixtureDef.friction = 0.4f;
    ufoFixtureDef.restitution = 0.0f;
    ufoFixtureDef.filter.categoryBits = B2DVars.BIT_UFO;
    ufoFixtureDef.filter.maskBits = BIT_BORDER;
    ufoBody.setUserData(ufoSprite);
    ufoBody.createFixture(ufoFixtureDef).setUserData("player");

    ufoShape.setAsBox(.5f / PPM, 30f / PPM, new Vector2(0 / PPM, -30 / PPM), 0);
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.isSensor = true;
    ufoFixtureDef.filter.categoryBits = B2DVars.BIT_UFO_LASER;
    ufoFixtureDef.filter.maskBits = BIT_HUMAN;
    // maybe need to insert filter with human here to get this sorted.
    ufoBody.createFixture(ufoFixtureDef).setUserData("sensor");
    ufoShape.dispose();

    humanMachine = new HumanMachine();
    // creating smallBoy
    /*boyTexture = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    boySprite = new Sprite(boyTexture);
    boySprite.setScale(0.1f);

    BodyDef boyBodyDef = new BodyDef();
    boyBodyDef.type = BodyType.DynamicBody;
    boyBodyDef.position.set(100 / PPM, 100 / PPM);
    boyBody = world.createBody(boyBodyDef);

    PolygonShape boyShape = new PolygonShape();
    boyShape.setAsBox(8 / PPM, 9.5f / PPM); // for some reason 1 unit here = 2px
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = boyShape;
    fixtureDef.density = 0.00002f; // human density
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0f;
    fixtureDef.filter.categoryBits = B2DVars.BIT_HUMAN;
    fixtureDef.filter.maskBits = BIT_BORDER | BIT_UFO_LASER;
    boyBody.setUserData(boySprite);
    boyBody.createFixture(fixtureDef).setUserData("human");
    boyShape.dispose();*/

    // setting up camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

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
    /*if (pos.x < camera.position.x - (50/PPM) && camera.position.x > 80/PPM) {
      camera.translate(-1, 0, 0);
    }
    if (pos.x > camera.position.x + 50/PPM && camera.position.x < 1520/PPM) {
      camera.translate(1, 0, 0);
    }
    camera.update();
    */
    if (Gdx.input.isKeyPressed(Keys.SPACE) && customContactListener.isHumanSpotted()) {
      System.out.println("ZAP!!!");
      Body human = customContactListener.getHuman();
      // removing fixture around human in order for them to disappear.
      // TODO: make this more interesting to watch maybe
      if (human != null) {
        human.applyForce(0f, 5000f, 0, 0, true);
        soundEffectWarp.play(0.5f);
      }
    }
  }

  public void update(float dt) {

    handleInput();
    world.step(dt, 8, 3);

  }

  public void render() {
    // clearing screen first
    Array<Body> dummyBodies = new Array<>();
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    spriteBatch.setProjectionMatrix(camera.combined);

    spriteBatch.begin();

    spriteBatch.draw(background, 0, 0, 160, 15, srcX, 0, 1600, 144, false, false);
    srcX += 1;
    if (srcX % 90 == 0) {
      dummyBodies.add(humanMachine.createHuman(100, 100, world));
      //dummyBodies.add(humanMachine.createHuman(40, 100, world));

    }
    // rendering humans

    //dummyBodies.add(boyBody);
    world.getBodies(dummyBodies);
    for (Body b : dummyBodies) {
      Sprite e = (Sprite) b.getUserData();
      if (e != null && b.getFixtureList().get(0).getFilterData().categoryBits == BIT_HUMAN) {
        e.setPosition(b.getPosition().x - 8f, b.getPosition().y - 9.5f);
        e.draw(spriteBatch);
      }
    }

    // rendering ufo
    Sprite e = (Sprite) ufoBody.getUserData();
    if (e != null) {
      e.setPosition(ufoBody.getPosition().x - 20f, ufoBody.getPosition().y - 7.5f);
      e.setRotation(ufoRotation * 30);
      e.draw(spriteBatch);
    }
    for (Body b : dummyBodies) {
      Sprite temp = (Sprite) b.getUserData();
      if (temp != null) {
        //temp.draw(spriteBatch);
      }
    }
    //boySprite.draw(spriteBatch);
    ufoSprite.draw(spriteBatch);
    spriteBatch.end();
    debugRenderer.render(world, camera.combined);
  }

  public void dispose() {
    spriteBatch.dispose();
    background.dispose();
    boyTexture.dispose();
    ufoTexture.dispose();
  }
}
