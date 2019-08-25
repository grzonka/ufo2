package net.grzonka.ufo2.controller;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import net.grzonka.ufo2.model.Play;

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

    // handles ufoSensor - human interaction
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


    // handles humans exiting screen
    if (fa.getUserData() != null && fa.getUserData().equals("despawn") && fb.getUserData() != null
        && fb.getUserData().equals(
        "human")) {
      System.out.println("HUMAN REACHED THE LIGHT!"); // TODO: remove debug
      // TODO: maybe even handle human despawn here.
      Play.addToGarbageCollector(fb.getBody());
    }
    if (fb.getUserData() != null && fb.getUserData().equals("despawn") && fa.getUserData() != null
        && fa.getUserData().equals(
        "human")) {
      System.out.println("HUMAN REACHED THE LIGHT!"); // TODO: remove debug
      //humanSpotted = true;
      Play.addToGarbageCollector(fa.getBody());

    }

    // handle buildings exiting screen
    if (fa.getUserData() != null && fa.getUserData().equals("despawn") && fb.getUserData() != null
        && fb.getUserData().equals(
        "building")) {
      System.out.println("BUILDING EXITED SCREEN!"); // TODO: remove debug
      // TODO: maybe even handle human despawn here.
      Play.addToGarbageCollector(fb.getBody());
    }
    if (fb.getUserData() != null && fb.getUserData().equals("despawn") && fa.getUserData() != null
        && fa.getUserData().equals(
        "building")) {
      System.out.println("BUILDING EXITED SCREEN!"); // TODO: remove debug
      //humanSpotted = true;
      Play.addToGarbageCollector(fa.getBody());

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

    // handles ufoSensor - human interaction
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

    // human exiting screen interaction
    if (fa.getUserData() != null && fa.getUserData().equals("despawn") && fb.getUserData() != null
        && fb.getUserData().equals(
        "human")) {
      System.out.println("HUMAN SURVIVED!"); // TODO: remove debug
    }
    if (fb.getUserData() != null && fb.getUserData().equals("human") && fa.getUserData() != null
        && fa.getUserData().equals(
        "despawn")) {
      System.out.println("HUMAN SURVIVED!"); // TODO: remove debug
    }

    // handles buildings exiting screen
    if (fa.getUserData() != null && fa.getUserData().equals("despawn") && fb.getUserData() != null
        && fb.getUserData().equals(
        "building")) {
      System.out.println("BUILDING DELETED!"); // TODO: remove debug
    }
    if (fb.getUserData() != null && fb.getUserData().equals("despawn") && fa.getUserData() != null
        && fa.getUserData().equals(
        "building")) {
      System.out.println("BUILDING DELETED!"); // TODO: remove debug
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
