package net.grzonka.ufo2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;
import net.grzonka.ufo2.controller.Content;
import net.grzonka.ufo2.controller.GameStateManager;
import net.grzonka.ufo2.model.B2DVars;

public class Game extends ApplicationAdapter {

  public static boolean gameHasStarted = false;

  public static final String TITLE = "Ufo2 - early release.";
  public static final int V_WIDTH = 160;
  public static final int V_HEIGHT = 144;
  public static final int SCALE = 1;
  private static final int MAX_HEALTH = 8000;

  private static int score;
  private static int health = MAX_HEALTH;
  private String scoreDiplayed;
  BitmapFont scoreFont;

  private SpriteBatch spriteBatch;
  private OrthographicCamera camera;

  Texture startScreen;
  Sprite startScreenSprite = new Sprite();

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

    score = 0;


    this.startScreen = new Texture(Gdx.files.internal("start_screen.png"));
    startScreenSprite = new Sprite(startScreen);
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
    if(gameHasStarted) {

      spriteBatch.begin();
      scoreDiplayed = Integer.toString(score);
      scoreFont = new BitmapFont();
      scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

      spriteBatch.end();

      spriteBatch.begin();
      scoreFont.setColor(0f, 0f, 0f, 1f);
      scoreFont.getData().setScale(0.15f);


      ShapeRenderer shapeRenderer = new ShapeRenderer();
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
      shapeRenderer.identity();
      shapeRenderer.setColor(Color.LIGHT_GRAY);
      shapeRenderer.rect(74 / B2DVars.PPM, 136 / B2DVars.PPM, 80 / B2DVars.PPM, 5 / B2DVars.PPM);
      if(health >= 0) {
        if ((health <= MAX_HEALTH / 4) && ((health / 60) % 3 == 0)) { //blinking effect when health drops below 25 %
          shapeRenderer.setColor(Color.LIGHT_GRAY);
        } else {
          shapeRenderer.setColor(Color.BLACK);
        }
        shapeRenderer
                .rect(74 / B2DVars.PPM, 136 / B2DVars.PPM, (health / 100f) / B2DVars.PPM, 5 / B2DVars.PPM);
      }
      if (health >= 0 &&gameHasStarted) {
        increaseHealth(-6);
      } else {
        System.out.println("GAME Should end here");
        // TODO: display highscore here
        gameHasStarted = false;
        health = MAX_HEALTH;
        score = 0;
      }
      shapeRenderer.end();
      spriteBatch.end();

      spriteBatch.begin();
      scoreFont.draw(spriteBatch, scoreDiplayed, 0.5f, 14);
      spriteBatch.end();

    }else{
    spriteBatch.begin();
    //TODO remove this when game hast started
    spriteBatch.draw(startScreenSprite,0,0,160f/B2DVars.PPM,144f/B2DVars.PPM);
    spriteBatch.end();}
  }

  public static void increaseHealth(int hpIncrease) {
    if (health >= 0) {
      if ((health + hpIncrease) <= MAX_HEALTH) {
        health += hpIncrease;
      } else {
        health = MAX_HEALTH;
      }
    }else {health = 0;}
  }


  public static void increaseScore(int additionalScore) {
    score += additionalScore;
  }


  @Override
  public void resize(int width, int height) {
  }

  public void gameHasStartedHud(){
    this.gameHasStarted = true;
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