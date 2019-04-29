package ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowworrm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.states.Play;
import lombok.Builder;

@Builder
public class SnowWorm extends Boss {

    private Body body;
    private SpriteBatch spriteBatch;
    private String type;
    private Play play;
    private SnowWorm.Part part;
    private float size;
    private float xOffset;
    private float yOffset;

    public enum MagmaWormAnimation {
        DEFAULT ("default");

        private final String name;

        MagmaWormAnimation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public enum Part {
        HEAD ("magmawormhead"),
        BODY ("magmawormbody"),
        TAIL ("magmawormtail");

        private final String name;

        Part(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    public SnowWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size) {
        this(body, sb, type, play, part, size, 0, 0);
    }

    public SnowWorm(Body body, SpriteBatch sb, String type, Play play, Part part, float size, float x, float y) {
        super(body, sb, play, "images/bosses/snowworm/magmaworm.scml", part.name, size, x, y);
        body.setUserData(type + type);
        setAnimation(MagmaWormAnimation.DEFAULT.name, false);
        setAnimationSpeed(50);
        setScale(size);
    }
}
