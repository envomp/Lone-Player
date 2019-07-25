package ee.taltech.iti0202.gui.game.desktop.entities.weapons2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;

public abstract class Weapon extends SpriteAnimation {

    public boolean isDropped;

    public Weapon(Body body, SpriteBatch sb, String path) {
        super(body, sb, path);
    }

    public void setAnimation(MultiplayerAnimation animation) {
        setAnimation(animation.getName(), animation.isToPlayOnce());
    }

    @Override
    public void render(SpriteBatch sb) {
        if (isDropped) {
            super.render(sb);
        }
    }

    public void forceRender(SpriteBatch sb) {
        super.render(sb);
    }

    public enum Type implements Serializable {
        DEAGLE ("images/bullets/deagle/deagle.scml", 50),
        SHOTGUN ("images/bullets/shotgun/shotgun.scml", 50),
        M4 ("images/bullets/m4/m4.scml", 50);

        private final String animationFile;
        private final int animationSpeed;

        Type(String t, int speed) {
            animationFile = t;
            animationSpeed = speed;
        }

        public String getAnimationFile() {
            return animationFile;
        }

        public int getAnimationSpeed() {
            return animationSpeed;
        }
    }
}
