package net.grzonka.ufo2.controller;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

  private boolean humanSpotted = false;

  // called when two fixtures collide
  @Override
  public void beginContact(Contact c) {

    Fixture fa = c.getFixtureA();
    Fixture fb = c.getFixtureB();

    if (fa.getUserData() != null && fa.getUserData().equals("sensor") && fb.getUserData() != null
        && fb.getUserData().equals(
        "human")) {
      System.out.println("SPOTTED HUMAN!");
      humanSpotted = true;
    }
    if (fb.getUserData() != null && fb.getUserData().equals("sensor") && fa.getUserData() != null
        && fa.getUserData().equals(
        "human")) {
      System.out.println("SPOTTED HUMAN!");
      humanSpotted = true;
    }

  }

  // called when to fixtures no longer collide.
  @Override
  public void endContact(Contact c) {

    Fixture fa = c.getFixtureA();
    Fixture fb = c.getFixtureB();

    if (fa.getUserData() != null && fa.getUserData().equals("sensor") && fb.getUserData() != null
        && fb.getUserData().equals(
        "human")) {
      System.out.println("LOST HUMAN!");
      humanSpotted = false;

    }
    if (fb.getUserData() != null && fb.getUserData().equals("sensor") && fa.getUserData() != null
        && fa.getUserData().equals(
        "human")) {
      System.out.println("LOST HUMAN!");
      humanSpotted = false;

    }

  }

  public boolean isHumanSpotted() {
    return humanSpotted;
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
