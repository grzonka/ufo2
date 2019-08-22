package net.grzonka.ufo2.controller;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

  private boolean humanSpotted = false;
  private Fixture fa;
  private Fixture fb;

  /**
   * Called when two fixtures start collidoing .
   *
   * @param c contact point issued by box2d engine
   */
  @Override
  public void beginContact(Contact c) {

    fa = c.getFixtureA();
    fb = c.getFixtureB();

    if (fa.getUserData() != null && fa.getUserData().equals("sensor") && fb.getUserData() != null
        && fb.getUserData().equals(
        "human")) {
      System.out.println("SPOTTED HUMAN!"); // TODO: remove debug
      humanSpotted = true;
    }
    if (fb.getUserData() != null && fb.getUserData().equals("sensor") && fa.getUserData() != null
        && fa.getUserData().equals(
        "human")) {
      System.out.println("SPOTTED HUMAN!"); // TODO: remove debug
      humanSpotted = true;
    }

  }

  /**
   * Called when fixtures no longer collide.
   *
   * @param c contact point issued by box2d engine.
   */
  @Override
  public void endContact(Contact c) {

    fa = c.getFixtureA();
    fb = c.getFixtureB();

    if (fa.getUserData() != null && fa.getUserData().equals("sensor") && fb.getUserData() != null
        && fb.getUserData().equals(
        "human")) {
      System.out.println("LOST HUMAN!"); // TODO: remove debug
      humanSpotted = false;
    }

    if (fb.getUserData() != null && fb.getUserData().equals("sensor") && fa.getUserData() != null
        && fa.getUserData().equals(
        "human")) {
      System.out.println("LOST HUMAN!"); // TODO: remove debug
      humanSpotted = false;
    }
  }

  /**
   * checks whether or not a human has been spotted by the sensor.
   */
  public boolean isHumanSpotted() {
    return humanSpotted;
  }

  /**
   * get human that was spotted by sensor.
   *
   * @return humand that was spotted, null if spotted object does not have "human" property.
   */
  public Body getHuman() {
    if (humanSpotted) {
      if (fa.getUserData() != null && fa.getUserData().equals("human")) {
        return fa.getBody();
      } else if (fb.getUserData() != null && fb.getUserData().equals("human")) {
        return fb.getBody();
      }
    }
    return null;
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
