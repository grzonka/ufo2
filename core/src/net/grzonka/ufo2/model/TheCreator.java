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
  Texture house00 = new Texture(Gdx.files.internal("house00.png"));
  Texture house20a = new Texture(Gdx.files.internal("house20a.png"));
  Texture house20b = new Texture(Gdx.files.internal("house20b.png"));
  Texture house20c = new Texture(Gdx.files.internal("house20c.png"));
  Texture house40a = new Texture(Gdx.files.internal("house40a.png"));
  Texture house40b = new Texture(Gdx.files.internal("house40b.png"));
  Texture house40c = new Texture(Gdx.files.internal("house40c.png"));
  Texture house60a = new Texture(Gdx.files.internal("house60a.png"));
  Texture house60b = new Texture(Gdx.files.internal("house60b.png"));
  Texture house60c = new Texture(Gdx.files.internal("house60c.png"));
  Texture house80a = new Texture(Gdx.files.internal("house80a.png"));
  Texture house80b = new Texture(Gdx.files.internal("house80b.png"));
  Texture house80c = new Texture(Gdx.files.internal("house80c.png"));


  Random randomGen = new Random();

  public Body createHuman(int xPixel, int yPixel, World world) {

    Texture humanTexture;

    if (randomGen.nextBoolean()) {
      humanTexture = girlTexture;
    } else {
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
    Texture buildingTexture = house00;

    int randomHeight = 0;
    int randomVersion = 0;
    // decide wether or not to spawn something at all

    if (!randomGen.nextBoolean()) {
      randomHeight = randomGen.nextInt(5);// generates 0-4
      randomVersion = randomGen.nextInt(3);
    }
    Sprite buildingSprite;
    switch (randomHeight) {
      case 0:
        buildingSprite= new Sprite(house00);
        buildingSprite.setScale(1 / PPM);

        // first top and bottom boundaries
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        // spawn location
        bodyDef.position.set(xPixel / PPM, 1/PPM);
        Body bottomBody = world.createBody(bodyDef);

        PolygonShape bodyBox = new PolygonShape();
        //Random randomGen = new Random();
        //int randomHeight = randomGen.nextInt(5);// generates 0-4
        bodyBox.setAsBox(10 / PPM, 1 / PPM); // should be of height 20,40,60,80 or
        // 100.
        FixtureDef topBottomFixtureDef = new FixtureDef();
        topBottomFixtureDef.shape = bodyBox;
        topBottomFixtureDef.density = 0f;
        topBottomFixtureDef.friction = 1f;
        topBottomFixtureDef.restitution = 0;
        topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
        topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
        bottomBody.setUserData(buildingSprite);
        bottomBody.createFixture(topBottomFixtureDef).setUserData("building0");
        bottomBody.setLinearVelocity(-8, 0);
        bodyBox.dispose();

        return bottomBody;

      case 1:
        switch (randomVersion) {
          case 0:
            buildingTexture = house20a;

            break;
          case 1:
            buildingTexture = house20b;
            break;
          case 2:
            buildingTexture = house20c;
            break;
        }

        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setScale(1 / PPM);

        // first top and bottom boundaries
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        // spawn location
        bodyDef.position.set(xPixel / PPM, 1/PPM);
        bottomBody = world.createBody(bodyDef);

         bodyBox = new PolygonShape();
        //Random randomGen = new Random();
        //int randomHeight = randomGen.nextInt(5);// generates 0-4
        bodyBox.setAsBox(10 / PPM, 20 / PPM); // should be of height 20,40,60,80 or
        // 100.
        topBottomFixtureDef = new FixtureDef();
        topBottomFixtureDef.shape = bodyBox;
        topBottomFixtureDef.density = 0f;
        topBottomFixtureDef.friction = 1f;
        topBottomFixtureDef.restitution = 0;
        topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
        topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
        bottomBody.setUserData(buildingSprite);
        bottomBody.createFixture(topBottomFixtureDef).setUserData("building20");
        bottomBody.setLinearVelocity(-8, 0);
        bodyBox.dispose();

        return bottomBody;


      case 2:
        switch (randomVersion) {
          case 0:
            buildingTexture = house40a;
            break;
          case 1:
            buildingTexture = house40b;
            break;
          case 2:
            buildingTexture = house40c;
            break;
        }
        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setScale(1 / PPM);

        // first top and bottom boundaries
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        // spawn location
        bodyDef.position.set(xPixel / PPM, 1/PPM);
        bottomBody = world.createBody(bodyDef);

        bodyBox = new PolygonShape();
        //Random randomGen = new Random();
        //int randomHeight = randomGen.nextInt(5);// generates 0-4
        bodyBox.setAsBox(10 / PPM, 40 / PPM); // should be of height 20,40,60,80 or
        // 100.
        topBottomFixtureDef = new FixtureDef();
        topBottomFixtureDef.shape = bodyBox;
        topBottomFixtureDef.density = 0f;
        topBottomFixtureDef.friction = 1f;
        topBottomFixtureDef.restitution = 0;
        topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
        topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
        bottomBody.setUserData(buildingSprite);
        bottomBody.createFixture(topBottomFixtureDef).setUserData("building40");
        bottomBody.setLinearVelocity(-8, 0);
        bodyBox.dispose();

        return bottomBody;
      case 3:
        switch (randomVersion) {
          case 0:
            buildingTexture = house60a;
            System.out.println("created 60a");
            break;
          case 1:
            buildingTexture = house60b;
            break;
          case 2:
            buildingTexture = house60c;
            break;
        }
        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setScale(1 / PPM);

        // first top and bottom boundaries
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        // spawn location
        bodyDef.position.set(xPixel / PPM, 1/PPM);
        bottomBody = world.createBody(bodyDef);

        bodyBox = new PolygonShape();
        //Random randomGen = new Random();
        //int randomHeight = randomGen.nextInt(5);// generates 0-4
        bodyBox.setAsBox(10 / PPM, 60 / PPM); // should be of height 20,40,60,80 or
        // 100.
        topBottomFixtureDef = new FixtureDef();
        topBottomFixtureDef.shape = bodyBox;
        topBottomFixtureDef.density = 0f;
        topBottomFixtureDef.friction = 1f;
        topBottomFixtureDef.restitution = 0;
        topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
        topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
        bottomBody.setUserData(buildingSprite);
        bottomBody.createFixture(topBottomFixtureDef).setUserData("building60");
        bottomBody.setLinearVelocity(-8, 0);
        bodyBox.dispose();

        return bottomBody;

      case 4:
        switch (randomVersion) {
          case 0:
            buildingTexture = house80a;
            break;
          case 1:
            buildingTexture = house80b;
            break;
          case 2:
            buildingTexture = house80c;
            break;
        }

        buildingSprite = new Sprite(buildingTexture);
        buildingSprite.setScale(1 / PPM);

        // first top and bottom boundaries
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        // spawn location
        bodyDef.position.set(xPixel / PPM, 1/PPM);
        bottomBody = world.createBody(bodyDef);

        bodyBox = new PolygonShape();
        //Random randomGen = new Random();
        //int randomHeight = randomGen.nextInt(5);// generates 0-4
        bodyBox.setAsBox(10 / PPM, 80 / PPM); // should be of height 20,40,60,80 or
        // 100.
        topBottomFixtureDef = new FixtureDef();
        topBottomFixtureDef.shape = bodyBox;
        topBottomFixtureDef.density = 0f;
        topBottomFixtureDef.friction = 1f;
        topBottomFixtureDef.restitution = 0;
        topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
        topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
        bottomBody.setUserData(buildingSprite);
        bottomBody.createFixture(topBottomFixtureDef).setUserData("building80");
        bottomBody.setLinearVelocity(-8, 0);
        bodyBox.dispose();

        return bottomBody;
      default:
        break;
    }

    buildingSprite = new Sprite(buildingTexture);
    buildingSprite.setScale(1 / PPM);

    // first top and bottom boundaries
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.KinematicBody;
    // spawn location
    bodyDef.position.set(xPixel / PPM, 1);
    Body bottomBody = world.createBody(bodyDef);

    PolygonShape bodyBox = new PolygonShape();
    //Random randomGen = new Random();
    //int randomHeight = randomGen.nextInt(5);// generates 0-4
    bodyBox.setAsBox(10 / PPM, ((randomHeight) * 2 / PPM)); // should be of height 20,40,60,80 or
    // 100.
    FixtureDef topBottomFixtureDef = new FixtureDef();
    topBottomFixtureDef.shape = bodyBox;
    topBottomFixtureDef.density = 0f;
    topBottomFixtureDef.friction = 1f;
    topBottomFixtureDef.restitution = 0;
    topBottomFixtureDef.filter.categoryBits = B2DVars.BIT_BUILDING;
    topBottomFixtureDef.filter.maskBits = BIT_HUMAN | B2DVars.BIT_DESPAWN | BIT_UFO;
    bottomBody.setUserData(buildingSprite);
    bottomBody.createFixture(topBottomFixtureDef).setUserData("building");
    bottomBody.setLinearVelocity(-8, 0);
    bodyBox.dispose();

    return bottomBody;
  }

}
