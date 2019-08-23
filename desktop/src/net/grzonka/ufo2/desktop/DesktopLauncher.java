package net.grzonka.ufo2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.grzonka.ufo2.Game;

public class DesktopLauncher {

  public static void main(String[] arg) {
    LwjglApplicationConfiguration config =
        new LwjglApplicationConfiguration();

    config.title = Game.TITLE;

    config.width = Game.V_WIDTH * Game.SCALE;
    config.height = Game.V_HEIGHT * Game.SCALE;
    config.useGL30 = true;

    new LwjglApplication(new Game(), config);
  }
}
