package ee.taltech.iti0202.gui.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ee.taltech.iti0202.gui.game.desktop.handlers.Content;
import ee.taltech.iti0202.gui.game.desktop.handlers.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.MyInputProcessor;

import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.B2DVars.V_WIDTH;


public class Game extends ApplicationAdapter {
    public static final String TITLE = "Alone at Night";

    public static final float STEP = 1 / 60f;
    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;
    public static Content res;

    public void create() {

        Gdx.input.setInputProcessor(new MyInputProcessor());

        // load textures
        res = new Content();

        res.loadTexture("android/res/images/menu.png", "newGameTextureActive");
        res.loadTexture("android/res/images/menu.png", "newGameTextureInactive");
        res.loadTexture("android/res/images/menu.png", "loadGameTextureActive");
        res.loadTexture("android/res/images/menu.png", "loadGameTextureInactive");
        res.loadTexture("android/res/images/menu.png", "settingsTextureActive");
        res.loadTexture("android/res/images/menu.png", "settingsTextureInactive");
        res.loadTexture("android/res/images/menu.png", "exitTextureActive");
        res.loadTexture("android/res/images/menu.png", "exitTextureInactive");
        res.loadTexture("android/res/images/menu.png", "newGameTextureActive");
        res.loadTexture("android/res/images/menu.png", "Logo");

        res.loadTexture("android/res/images/Player/Player.png", "Player");
        res.loadTexture("android/res/maps/Flag.png", "Checkpoint");

        sb = new SpriteBatch();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);
    }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

    @Override
    public void render() {

        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            MyInput.update();
            //addSystem.out.println(Gdx.graphics.getFramesPerSecond());
        }

    }

    @Override
    public void dispose() {
    }

    public void resize(int w, int h) {
    }

    public void pause() {
    }

    public void resume() {
    }
}
