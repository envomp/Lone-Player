package ee.taltech.iti0202.gui.game.desktop.entities.animated;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.LibGdx.LibGdxDrawer;
import com.brashmonkey.spriter.LibGdx.LibGdxLoader;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.SCMLReader;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;

public class SpriteAnimation {

    private MyPlayerTweener playerTweener;
    private LibGdxLoader loader;

    private Drawer<Sprite> drawer;

    protected Body body;
    private float opacity = 1;

    SpriteAnimation(Body body, SpriteBatch sb, String path) {
        System.out.println("New body: " + body.toString());
        this.body = body;
        //animation = new Animation();
        FileHandle handle = Gdx.files.internal(PATH + path);
        Data data = new SCMLReader(handle.read()).getData();

        loader = new LibGdxLoader(data);
        loader.load(handle.file());

        drawer = new LibGdxDrawer(loader, sb, null); // no shape rendering
        playerTweener = new MyPlayerTweener(data.getEntity(0));
        playerTweener.setPivot(0, 0);
    }

    public void update(float dt) {
        //animation.update(dt);
        playerTweener.update(dt);
        playerTweener.setPosition(body.getPosition().x * PPM, body.getPosition().y * PPM + 24);
        playerTweener.setAngle((float) Math.toDegrees(body.getAngle()));
    }

    public void render(SpriteBatch sb) {
        sb.setColor(1, 1, 1, opacity);
        sb.begin();

        drawer.draw(playerTweener);

        sb.setColor(1, 1, 1, 1);
        sb.end();
    }

    protected void setAnimation(String animation) {
        playerTweener.setAnimation(animation);
    }

    public void setFlipX(boolean flipX) {
        if ((playerTweener.flippedX() == -1) != flipX)
            playerTweener.flipX();
    }

    public void setScale(float scale) {
        playerTweener.setScale(scale);
    }

    public void setAnimationSpeed(int speed) {
        playerTweener.speed = speed;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

}