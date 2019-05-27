package ee.taltech.iti0202.gui.game.desktop.settings;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SettingsSerializer implements JsonSerializer<Settings> {
    public JsonElement serialize(
            final Settings settings, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("MOVE_LEFT", new JsonPrimitive(settings.MOVE_LEFT));
        result.add("MOVE_RIGHT", new JsonPrimitive(settings.MOVE_RIGHT));
        result.add("CHANGE_DIMENSION", new JsonPrimitive(settings.CHANGE_DIMENSION));
        result.add("JUMP", new JsonPrimitive(settings.JUMP));
        result.add("SHOOT", new JsonPrimitive(settings.SHOOT));
        result.add("MENU", new JsonPrimitive(settings.MENU));
        result.add("ESC", new JsonPrimitive(settings.ESC));
        result.add("ENABLE_DEV_MAPS", new JsonPrimitive(settings.ENABLE_DEV_MAPS));
        result.add("SHOW_FPS", new JsonPrimitive(settings.SHOW_FPS));
        result.add("MAX_FPS", new JsonPrimitive(settings.MAX_FPS));
        result.add("ENABLE_VSYNC", new JsonPrimitive(settings.ENABLE_VSYNC));
        result.add("NAME", new JsonPrimitive(settings.NAME));
        return result;
    }
}
