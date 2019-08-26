package net.grzonka.ufo2.model;

import static net.grzonka.ufo2.model.B2DVars.BIT_BORDER;
import static net.grzonka.ufo2.model.B2DVars.BIT_BUILDING;
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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import net.grzonka.ufo2.Game;
import net.grzonka.ufo2.controller.GameStateManager;
import net.grzonka.ufo2.controller.MyContactListener;

public class Play extends GameState {


  private World world;
  private MyContactListener customContactListener;
  private Box2DDebugRenderer debugRenderer;

  private static ArrayList<Body> garbageCollector;

  private Texture background;
  private Sprite backgroundSprite;

  Texture laserTexture = new Texture(Gdx.files.internal("laser.png"));
  Sprite laserSprite = new Sprite(laserTexture);


  private Texture ufoTexture;
  private Sprite ufoSprite;
  private Body ufoBody;

  private TheCreator theCreator;

  private Sound soundEffectLaser;
  private Sound soundEffectWarp;
  private final Sound soundEffectTheme;

  private OrthographicCamera camera;


  // variables for input handeling
  final float MAX_VELOCITY = 10;
  final float moveSpeed = 20f;
  final float cameraSpeed = 1.5f;
  float ufoRotation = 0;
  private float srcX;
  private boolean spacePressed = false;


  public Play(GameStateManager gsm) {
    super(gsm);
    customContactListener = new MyContactListener();

    garbageCollector = new ArrayList<>();

    background = new Texture(Gdx.files.internal("background_10_wide.png"), true);
    background.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    backgroundSprite = new Sprite(background);

    laserSprite.setScale(1 / PPM);

    world = new World(B2DVars.GRAVITY, true); // Vector2 creates gravity
    world.setContactListener(customContactListener);

    soundEffectWarp = Gdx.audio.newSound(Gdx.files.internal("sound/warp.ogg"));
    soundEffectTheme = Gdx.audio.newSound(Gdx.files.internal("sound/theme.ogg"));
    soundEffectLaser = Gdx.audio.newSound(Gdx.files.internal("sound/laser.ogg"));

    soundEffectTheme.loop(1f);

    debugRenderer = new Box2DDebugRenderer();

    // create boundaries
    // first top and bottom boundaries
    BodyDef topBottomBodyDef = new BodyDef();
    topBottomBodyDef.type = BodyType.StaticBody;
    topBottomBodyDef.position.set(1000 / PPM, 144.5f / PPM);
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
    startEndBodyDef.position.set(-20.5f / PPM, 80 / PPM);
    Body startBody = world.createBody(startEndBodyDef);
    startEndBodyDef.position.set(180.5f / PPM, 80 / PPM);
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

    // making a despawn boundary to remove humans and buildings.
    startEndBodyDef.position.set(-20 / PPM, 80 / PPM);  // position at which humans get disposed
    Body despawnBody = world.createBody(startEndBodyDef);
    startEndFixtureDef.filter.categoryBits = B2DVars.BIT_DESPAWN;
    startEndFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_BUILDING;
    despawnBody.createFixture(startEndFixtureDef).setUserData("despawn");

    startEndBox.dispose();

    // creating ufo
    ufoTexture = new Texture(Gdx.files.internal("ufo_transparent_0_zap.png")); // 0 - 6 are zap
    ufoSprite = new Sprite(ufoTexture);
    ufoSprite.setScale(1 / PPM);

    BodyDef UfoBodyDef = new BodyDef();
    UfoBodyDef.type = BodyType.DynamicBody;
    UfoBodyDef.position.set(60 / PPM, 120 / PPM);
    ufoBody = world.createBody(UfoBodyDef);
    ufoBody.setGravityScale(0);

    PolygonShape ufoShape = new PolygonShape();
    ufoShape.setAsBox(20 / PPM, 7.5f / PPM);
    FixtureDef ufoFixtureDef = new FixtureDef();
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.density = .0f; // ufo density (kg/m^2)
    ufoFixtureDef.friction = 0.7f;
    ufoFixtureDef.restitution = 0.0f;
    ufoFixtureDef.filter.categoryBits = B2DVars.BIT_UFO;
    ufoFixtureDef.filter.maskBits = BIT_BORDER | B2DVars.BIT_BUILDING;
    ufoBody.setUserData(ufoSprite);
    ufoBody.createFixture(ufoFixtureDef).setUserData("player");

    ufoShape.setAsBox(4f / PPM, 30f / PPM, new Vector2(0 / PPM, -30 / PPM), 0);
    ufoFixtureDef.shape = ufoShape;
    ufoFixtureDef.isSensor = true;
    ufoFixtureDef.filter.categoryBits = B2DVars.BIT_UFO_LASER;
    ufoFixtureDef.filter.maskBits = BIT_HUMAN;
    // maybe need to insert filter with human here to get this sorted.
    ufoBody.createFixture(ufoFixtureDef).setUserData("sensor");
    ufoShape.dispose();

    theCreator = new TheCreator();

    // setting up camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

  }

  public void handleInput() {

    // ufo movement
    Vector2 vel = ufoBody.getLinearVelocity();
    Vector2 pos = ufoBody.getPosition();

    //ufoBody.applyForceToCenter(0f, 10f, true);
    // apply left impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.LEFT) && vel.x > -MAX_VELOCITY) {
      //ufoBody.applyLinearImpulse(-moveSpeed, 0, pos.x, pos.y, true);
      ufoBody.applyForceToCenter(-moveSpeed, 0, true);
      if (ufoRotation < 0.3) {
        ufoRotation += 0.1f;
      }
    }
    if (Gdx.input.isKeyPressed(Keys.RIGHT) && vel.x < MAX_VELOCITY) {
      //ufoBody.applyLinearImpulse(moveSpeed, 0, pos.x, pos.y, true);
      ufoBody.applyForceToCenter(moveSpeed, 0, true);
      if (ufoRotation > -0.3) {
        ufoRotation -= 0.1f;
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP) && vel.y < MAX_VELOCITY) {
      //ufoBody.applyLinearImpulse(0, moveSpeed, pos.x, pos.y, true);
      ufoBody.applyForceToCenter(0, moveSpeed, true);
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN) && vel.y < MAX_VELOCITY) {
      //ufoBody.applyLinearImpulse(0, -moveSpeed, pos.x, pos.y, true);
      ufoBody.applyForceToCenter(0, -moveSpeed, true);
    }

    if (Gdx.input.isKeyJustPressed(Keys.SPACE) && !Game.gameHasStarted) {
      Game.gameHasStarted = true;

    }

    if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
      spacePressed = true;
     /* laserSprite.setPosition(ufoBody.getPosition().x, ufoBody.getPosition().y);
      spriteBatch.begin();
      laserSprite.draw(spriteBatch);
      spriteBatch.end();*/
      if (customContactListener.isHumanSpotted()) {
        //System.out.println("ZAP!!!");
        soundEffectWarp.play(0.5f);
        Body human = customContactListener.getHuman();
        // removing fixture around human in order for them to disappear.
        // TODO: make this more interesting to watch maybe
        if (human != null) {
          Game.increaseHealth(2000);
          Game.increaseScore(1);
          human.applyForceToCenter(0f, 2700f, true);
          addToGarbageCollector(human);
        }
      } else {
        soundEffectLaser.play(0.5f);
        Game.increaseHealth(-200);
      }


    }


  }

  public static void addToGarbageCollector(Body body) {
    garbageCollector.add(body);
  }

  public void update(float dt) {

    handleInput();
    world.step(dt, 8, 3);
    clearGarbageCollector();
    //spacePressed = false;
  }

  private void clearGarbageCollector() {
    for (Body b : garbageCollector) {
      world.destroyBody(b);
    }
    garbageCollector.clear();
  }

  public void render() {

    // clearing screen first
    Array<Body> dummyBodies = new Array<>(0);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    spriteBatch.setProjectionMatrix(camera.combined);

    spriteBatch.begin();

    spriteBatch.draw(background, 0, 0, 160, 15, (int) srcX, 0, 1600, 144, false, false);
    srcX += 0.5;

    if (Game.gameHasStarted) {

      if (srcX % 15 == 0) {
        dummyBodies.add(theCreator.createBuilding(world, 300));
      }
      if (srcX % 60 == 0) {
        dummyBodies.add(theCreator.createHuman(300, 120, world));
      }
    }

    // rendering humans
    world.getBodies(dummyBodies);
    for (Body b : dummyBodies) {
      Sprite e = (Sprite) b.getUserData();
      if (e != null && b.getFixtureList().get(0).getFilterData().categoryBits == BIT_HUMAN) {
        //System.out.println("PRINTING HUMAN");
        e.setPosition(b.getPosition().x - 8f, b.getPosition().y - 9.5f);
        e.draw(spriteBatch);
        //System.out.println("Human is at: " + b.getPosition().x);
      } else if (e != null && b.getFixtureList().get(0).getUserData().equals("building0")) {
       /* System.out.println("PRINTING BUILDING at x: " + b.getPosition().x + "    y: "+ b
         .getPosition().y);*/
        e.setPosition(b.getPosition().x - 10, b.getPosition().y - 0.5f);

        e.draw(spriteBatch);
      } else if (e != null && b.getFixtureList().get(0).getUserData().equals("building20")) {
       /* System.out.println("PRINTING BUILDING at x: " + b.getPosition().x + "    y: "+ b
         .getPosition().y);*/
        e.setPosition(b.getPosition().x - 10, b.getPosition().y - 9f);

        e.draw(spriteBatch);
      } else if (e != null && b.getFixtureList().get(0).getUserData().equals("building40")) {
       /* System.out.println("PRINTING BUILDING at x: " + b.getPosition().x + "    y: "+ b
         .getPosition().y);*/
        e.setPosition(b.getPosition().x - 10, b.getPosition().y - 18f);

        e.draw(spriteBatch);
      } else if (e != null && b.getFixtureList().get(0).getUserData().equals("building60")) {
       /* System.out.println("PRINTING BUILDING at x: " + b.getPosition().x + "    y: "+ b
         .getPosition().y);*/
        e.setPosition(b.getPosition().x - 10, b.getPosition().y - 27f);

        e.draw(spriteBatch);
      } else if (e != null && b.getFixtureList().get(0).getUserData().equals("building80")) {
       /* System.out.println("PRINTING BUILDING at x: " + b.getPosition().x + "    y: "+ b
         .getPosition().y);*/
        e.setPosition(b.getPosition().x - 10, b.getPosition().y - 36f);

        e.draw(spriteBatch);
      }
      // delete buildings that leave the screen. (number should be grater then human despawn
      // location.
      if (b.getType() == BodyType.KinematicBody && b.getPosition().x < -50 / PPM) {
        addToGarbageCollector(b);
        // System.out.println("scheduled " + b + "for deletion.");
        // System.out.println(dummyBodies.size);
      }
    }

    // rendering ufo
    Sprite e = (Sprite) ufoBody.getUserData();
    if (e != null) {
      e.setPosition(ufoBody.getPosition().x - 20f, ufoBody.getPosition().y - 7.5f);
      e.setRotation(ufoRotation * 30);
      e.draw(spriteBatch);
    }
    // drawing laser:
    if (spacePressed) {
      laserSprite.setScale(.1f);
      spriteBatch.draw(laserSprite,ufoBody.getPosition().x - 1f, ufoBody.getPosition().y - 6f
          , 2, 60 / 10);
      spacePressed = false;
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
    // debugRenderer.render(world, camera.combined);

  }

  public void resetUfoPosition() {

  }

  public void dispose() {
    spriteBatch.dispose();
    background.dispose();
    ufoTexture.dispose();
  }

}
