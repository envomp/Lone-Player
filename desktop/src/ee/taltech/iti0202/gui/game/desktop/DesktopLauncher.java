package ee.taltech.iti0202.gui.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.SCALE;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

public class DesktopLauncher {
    public static void main(String[] arg) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        Settings settings = new Settings().load("android/assets/settings/settings.json");

        new LwjglApplication(new Game(settings) {
            @Override
            public void setForegroundFPS(int value) {
                config.foregroundFPS = value;
            }
        }, config);

        config.title = Game.TITLE;
        config.width = V_WIDTH * SCALE;
        config.samples = 3;
        config.forceExit = true;
        config.fullscreen = false;
        config.height = V_HEIGHT * SCALE;
        //config.foregroundFPS = 300; // <- limit when focused
        config.backgroundFPS = 60; // <- limit when minimized

    }
}
