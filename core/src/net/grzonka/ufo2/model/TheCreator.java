package net.grzonka.ufo2.model;

import static net.grzonka.ufo2.model.B2DVars.BIT_BORDER;
import static net.grzonka.ufo2.model.B2DVars.BIT_BUILDING;
import static net.grzonka.ufo2.model.B2DVars.BIT_HUMAN;
import static net.grzonka.ufo2.model.B2DVars.BIT_UFO;
import static net.grzonka.ufo2.model.B2DVars.BIT_UFO_LASER;
import static net.grzonka.ufo2.model.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Random;

public class TheCreator {

  Texture boyTexture = new Texture(Gdx.files.internal("small_boy_transparent.png"));
  Texture girlTexture = new Texture(Gdx.files.internal("small_girl_transparent_human.png"));

  Random randomGen = new Random();

  public Body createHuman(int xPixel, int yPixel, World world) {

    Texture humanTexture;

    if(randomGen.nextBoolean()){
      System.out.println("Its a girl");
      humanTexture = girlTexture;
    } else {
      System.out.println("Its a boy");
      humanTexture = boyTexture;
    }

    Sprite humanSprite = new Sprite(humanTexture);
    humanSprite.setScale(0.1f);

    BodyDef boyBodyDef = new BodyDef();
    boyBodyDef.type = BodyType.DynamicBody;
    boyBodyDef.position.set(xPixel / PPM, yPixel / PPM);
    Body boyBody = world.createBody(boyBodyDef);

    PolygonShape humanShape = new PolygonShape();
    humanShape.setAsBox(8 / PPM, 9.5f / PPM);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = humanShape;
    fixtureDef.density = 0.00002f; // human density
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0f;
    fixtureDef.filter.categoryBits = B2DVars.BIT_HUMAN;
    fixtureDef.filter.maskBits = BIT_BUILDING | BIT_UFO_LASER | B2DVars.BIT_DESPAWN;
    boyBody.setUserData(humanSprite);
    boyBody.createFixture(fixtureDef).setUserData("human");
    humanShape.dispose();

    return boyBody;
  }

  public Body createBuilding(World world, int xPixel) {
    int randomHeight = 0;
    // decide wether or not to spawn something at all

    if (!randomGen.nextBoolean()) {
      randomHeight = randomGen.nextInt(5);// generates 0-4
    }

    // first top and bottom boundaries
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.KinematicBody;
    // spawn location
    bodyDef.position.set(new Vector2(xPixel / PPM, 1f / PPM));
    Body bottomBody = world.createBody(bodyDef);

    PolygonShape bodyBox = new PolygonShape();
    //Random randomGen = new Random();
    //int randomHeight = randomGen.nextInt(5);// generates 0-4
    bodyBox.setAsBox(20 / PPM, randomHeight * 20 / PPM); // should be of height 20,40,60,80 or
    // 100.
    FixtureDef topBottomFixtureDef = new FixtureDef();
    topBottomFixtureDef.shape = bodyBox;
    topBottomFixtureDef.density = 0f;
    topBottomFixtureDef.friction = 1f;
    topBottomFixtureDef.restitution = 0;
    topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
    topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
    bottomBody.createFixture(topBottomFixtureDef).setUserData("building");
    bottomBody.setLinearVelocity(-8, 0);
    bodyBox.dispose();

    return bottomBody;
  }

}
