package ee.taltech.iti0202.gui.game.networking.server.entity;

import java.io.Serializable;

import ee.taltech.iti0202.gui.game.desktop.entities.animations.loader.MultiplayerAnimation;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;

public class BulletEntity extends Entity implements Serializable {
    public WeaponProjectile.Type type;
    public int shooterId;
    public float angle;
    public boolean isHit = false;
    public MultiplayerAnimation animation;
}
