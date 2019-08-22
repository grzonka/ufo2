package net.grzonka.ufo2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.grzonka.ufo2.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Game someTitle";
		config.useGL30 = true;
		config.width = 160;
		config.height = 144;

		new LwjglApplication(new Game(), config);
	}
}
