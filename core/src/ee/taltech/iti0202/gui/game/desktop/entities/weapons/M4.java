package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class M4 extends Weapon {

    public M4(World world, SpriteBatch sb, Body body) {
        super(world, sb, body, "images/bullets/m4/m4.scml");
        setScale(0.1f);
        setAnimationSpeed(50);
        coolDown = 0.1f;
    }
}