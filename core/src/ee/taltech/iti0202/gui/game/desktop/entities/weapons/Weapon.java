package ee.taltech.iti0202.gui.game.desktop.entities.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.iti0202.gui.game.desktop.entities.animations.SpriteAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.Bullet;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile.bullet.loader.BulletLoader;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.canvas.Draw;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.V_WIDTH;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Weapon extends SpriteAnimation {

    protected World world;
    protected Draw draw;
    protected float coolDown = 0;
    protected float bulletHeat = 0;
    protected double offRadius = 0;
    private SpriteBatch sb;

    public Weapon(World world, SpriteBatch sb, Body body, String path, Draw draw) {
        super(body, sb, path);
        this.sb = sb;
        this.world = world;
        this.draw = draw;
        setAnimation(Animation.DEFAULT.name, false);
        setAnimationSpeed(50);
    }

    @Override
    public void update(float dt) {
        if (bulletHeat > 0) {
            bulletHeat -= dt;
        } else {
            setAnimation(Animation.DEFAULT.name, false);
        }
        super.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public void fire(Vector2 destination) {
        bulletHeat = coolDown;
        setAnimation(Animation.FIRE.name, true);
        Bullet bullet =
                BulletLoader.bulletLoader(
                        sb,
                        world,
                        new Vector2(V_WIDTH >> 1, V_HEIGHT >> 1),
                        destination,
                        body.getPosition(),
                        offRadius);

        draw.getBulletHandler().getBulletArray().add(bullet);
    }

    public boolean canFire() {
        return bulletHeat <= 0;
    }

    public enum Animation {
        DEFAULT("default"),
        FIRE("fire");

        private final String name;

        Animation(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }
}
