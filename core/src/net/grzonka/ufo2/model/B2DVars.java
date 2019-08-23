package net.grzonka.ufo2.model;

import com.badlogic.gdx.math.Vector2;

public class B2DVars {

  public static final Vector2 GRAVITY = new Vector2(0,-9.81f);

  // pixel per meter ratio (default = 1px = 1meter)
  public static final float PPM = 10;

  // category bits
  public static final short BIT_BORDER = 2;
  public static final short BIT_HUMAN = 4;
  public static final short BIT_UFO = 8;
  public static final short BIT_UFO_LASER = 16;

}
