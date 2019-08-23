package net.grzonka.ufo2.model;

import static net.grzonka.ufo2.model.B2DVars.BIT_BORDER;
import static net.grzonka.ufo2.model.B2DVars.BIT_UFO_LASER;
import static net.grzonka.ufo2.model.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class HumanMachine {

  public Body createHuman(int xPixel, int yPixel, World world) {

    Texture boyTexture = new Texture(Gdx.files.internal("small_boy_transparent.png"));
    Sprite boySprite = new Sprite(boyTexture);
    boySprite.setScale(0.1f);

    BodyDef boyBodyDef = new BodyDef();
    boyBodyDef.type = BodyType.DynamicBody;
    boyBodyDef.position.set(xPixel / PPM, yPixel / PPM);
    Body boyBody = world.createBody(boyBodyDef);

    PolygonShape boyShape = new PolygonShape();
    boyShape.setAsBox(8 / PPM, 9.5f / PPM);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = boyShape;
    fixtureDef.density = 0.00002f; // human density
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0f;
    fixtureDef.filter.categoryBits = B2DVars.BIT_HUMAN;
    fixtureDef.filter.maskBits = BIT_BORDER | BIT_UFO_LASER;
    boyBody.setUserData(boySprite);
    boyBody.createFixture(fixtureDef).setUserData("human");
    boyShape.dispose();

    return boyBody;


  }

}
