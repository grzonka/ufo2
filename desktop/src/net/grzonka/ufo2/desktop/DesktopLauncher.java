package net.grzonka.ufo2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.grzonka.ufo2.Ufo2;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ufo2 someTitle";
		config.useGL30 = true;
		config.width = 160;
		config.height = 144;

		new LwjglApplication(new Ufo2(), config);
	}
}
