package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.FlappyMan;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.width = FlappyMan.width;
		configuration.height = FlappyMan.height;
		configuration.title = FlappyMan.title;
		configuration.addIcon("icon.png", Files.FileType.Internal);
		new LwjglApplication(new FlappyMan(), configuration);
	}
}
