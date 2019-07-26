package ee.taltech.iti0202.gui.game.desktop.entities.projectile2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.bullet.Bullet;

public class WeaponProjectileBuilder {
    private Body body;
    private SpriteBatch spriteBatch;
    private WeaponProjectile.Type type;

    public WeaponProjectileBuilder setBody(Body body) {
        this.body = body;
        return this;
    }

    public WeaponProjectileBuilder setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        return this;
    }

    public WeaponProjectileBuilder setType(WeaponProjectile.Type type) {
        this.type = type;
        return this;
    }

    public WeaponProjectile createWeaponProjectile() {
        switch (type) {
            case BULLET:
                return new Bullet(body, spriteBatch);
        }
        return null;  // Never get here
    }
}