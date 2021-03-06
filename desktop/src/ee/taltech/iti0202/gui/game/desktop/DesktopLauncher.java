package ee.taltech.iti0202.gui.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        if (JAR) {
            B2DVars.PATH = "assets/";
        }

        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        Settings settings = new Settings().load(PATH + "settings/settings.json");

        new LwjglApplication(
                new Game(settings) {
                    @Override
                    public void setForegroundFPS(int value) {
                        config.foregroundFPS = value;
                    }
                },
                config);

        config.title = Game.TITLE;
        config.width = V_WIDTH * SCALE;
        config.samples = 3;
        config.forceExit = true;
        List<String> arguments = new ArrayList<>((Arrays.asList(arg)));
        config.fullscreen = !(arguments.contains("-w") || arguments.contains("--windowed"));
        config.height = V_HEIGHT * SCALE;
        // config.foregroundFPS = 300; // <- limit when focused
        config.backgroundFPS = 60; // <- limit when minimized
        config.addIcon(PATH + "images/logos/logo-32.png", Files.FileType.Local);

        DEBUG = arguments.contains("-d") || arguments.contains("--debug");
    }
}
