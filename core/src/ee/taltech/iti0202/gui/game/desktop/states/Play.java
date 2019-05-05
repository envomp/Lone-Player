package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.BossHander;
import ee.taltech.iti0202.gui.game.desktop.entities.player.PlayerLoader;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.EndMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.PauseMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.layers.Draw;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.BossData;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static ee.taltech.iti0202.gui.game.desktop.handlers.sound.Sound.playSoundOnce;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SPEEDS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.CHECKPOINTS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DEBUG;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DMG_MULTIPLIER;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DMG_ON_LANDING;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAX_LOAD_TIME;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MIN_FPS_EXPECTED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.UPDATE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;

@EqualsAndHashCode(callSuper = true)
@Data
public class Play extends GameState {

    // Play is the root node for most of the action

    public enum pauseState {
        PAUSE,
        RUN,
        RESUME,
        SETTINGS,
        END,
        DEFAULT,
    }

    // LibGdx variables
    private World world = new World(new Vector2(0, GRAVITY), true);
    @ToString.Exclude
    private Draw draw;
    private MyContactListener cl = new MyContactListener();
    private Box2DDebugRenderer b2dr = new Box2DDebugRenderer();
    private OrthographicCamera b2dcam = new OrthographicCamera();
    private OrthographicCamera hudCam = new OrthographicCamera();
    private Hud hud;
    private OrthogonalTiledMapRenderer renderer;

    // Player based variables
    @ToString.Exclude
    private PlayerHandler playerHandler;
    @ToString.Exclude
    private BossHander bossHander;
    private GameProgress progress;

    // States
    private PauseMenu pauseMenu;
    private pauseState playState = pauseState.DEFAULT;
    private SettingsMenu settingsMenu;
    private EndMenu endMenu;
    private boolean executeEnd = true;
    private Stage stage = new Stage(new ScreenViewport());
    private String act;
    private String map;
    private B2DVars.gameDifficulty difficulty;

    // Background based variables
    private Texture backgroundTexture;
    private ParallaxBackground parallaxBackground;
    private float backgroundSpeed;

    // Boss logic, helpful variables
    private Vector2 camSpeed = new Vector2(0, 0);
    private float playTime = 0;
    private boolean loading = true;

    ////////////////////////////////////////////////////////////////////         Set up game        ////////////////////////////////////////////////////////////////////

    private Play(String act, String map, B2DVars.gameDifficulty difficulty, GameProgress progress) {
        this.act = act;
        this.map = map;
        this.difficulty = difficulty;

        // set the difficulty
        System.out.println(difficulty);

        switch (difficulty) {
            case EASY:
                DMG_MULTIPLIER = 1;
                DMG_ON_LANDING = 10;
                CHECKPOINTS = true;
                BOSSES = false;
                break;

            case HARD:
                DMG_MULTIPLIER = 1.5f;
                DMG_ON_LANDING = 9;
                CHECKPOINTS = true;
                BOSSES = true;
                break;

            case BRUTAL:
                DMG_MULTIPLIER = 2;
                DMG_ON_LANDING = 8;
                CHECKPOINTS = false;
                BOSSES = true;
                break;
        }

        game.getSound().stop();
        switch (act) {
            case "Desert":
                break;
            case "Plains":
                break;
            case "Snow":
                game.setSound(Gdx.audio.newMusic(Gdx.files.internal(PATH + "sounds/wind1.wav")));
                game.getSound().setLooping(true);
                game.getSound().play();
                game.getSound().setVolume(0.2f);
                break;
        }

        // sey up world
        world.setContactListener(cl);

        //set up cameras
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        hudCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        // create pause state
        pauseMenu = new PauseMenu(act, map, hudCam, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.RUN;
                draw.setGameFadeOut(false);
                draw.setGameFadeDone(false);
                UPDATE = true;
            }
        }, new Runnable() {
            @Override
            public void run() {
                saveGame();
            }
        }, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.SETTINGS;
            }
        });

        settingsMenu = new SettingsMenu(hudCam, game, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.PAUSE;
            }
        });

        endMenu = new EndMenu(act, map, hudCam, difficulty, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.SETTINGS;
            }
        });
        hud = new Hud(hudCam, this);

        playState = pauseState.RUN;

        // set up background
        String backgroundPath = MAIN_SCREENS[BACKGROUND_SCREENS.get(act)];
        backgroundTexture = new Texture(Gdx.files.internal(PATH + backgroundPath + "backgroundLayer.png"));

        backgroundSpeed = BACKGROUND_SPEEDS.get(act);
        Array<Texture> textures = new Array<>();
        int layersCount = Gdx.files.internal(PATH + backgroundPath).list().length;
        for (int i = 1; i < layersCount; i++) {
            textures.add(new Texture(Gdx.files.internal(PATH + backgroundPath + "backgroundLayer" + i + ".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0f);
        stage.addActor(parallaxBackground);

        ////////////////////////////////    Tiled stuff here    ///////////////////////

        draw = new Draw(this, sb, world); // first create the "canvas" to draw onto
        playerHandler = new PlayerHandler(this, sb, progress, cl, draw); // then create a handler for player
        bossHander = new BossHander();
        draw.setPlayerHandler(playerHandler);
        draw.setBossHander(bossHander);
        PlayerLoader playerLoader = new PlayerLoader(this, sb, playerHandler); // and then load the player in

        if (progress != null) {
            draw.setDimension(progress.dimension);
            draw.drawLayers(false, progress.bosses);
            playerHandler.setPlayer(playerLoader.initPlayer(progress));
        } else {

            draw.drawLayers(true, null);
            playerHandler.setPlayer(playerLoader.initPlayer());
        }

        cam.position.set(
                playerHandler.getPlayer().getPosition().x * PPM,
                playerHandler.getPlayer().getPosition().y * PPM,
                0);
        UPDATE = true;
        cam.zoom = 1;
    }

    public Play(String act, String map, B2DVars.gameDifficulty difficulty) {
        this(act, map, difficulty, null);
    }

    public Play(GameProgress progress) {
        this(progress.act, progress.map, progress.difficulty, progress);
    }

    ////////////////////////////////////////////////////////////////////      Handle I/O devices    ////////////////////////////////////////////////////////////////////

    public void handleInput() {

        playerHandler.handlePlayerInput(playState);

        //pause screen
        if (MyInput.isPressed(Game.settings.ESC)) {
            if (playState == pauseState.RUN) {
                UPDATE = false;
                parallaxBackground.setSpeed(0);
                playState = pauseState.PAUSE;
                draw.setGameFadeOut(true);
                draw.setGameFadeDone(false);
            } else {
                UPDATE = true;
                playState = pauseState.RUN;
                draw.setGameFadeOut(false);
                draw.setGameFadeDone(false);
            }
        }
    }

    public void update(float dt) {
        if (loading && playTime < MAX_LOAD_TIME && dt > 1 / MIN_FPS_EXPECTED) {
            playTime += dt;
            return;
        } else if (loading) {
            loading = false;
        }

        if (playerHandler.isNewPlayer()) {
            if (Math.abs(playerHandler.getPlayer().getPosition().x - cam.position.x / PPM) < 1 && Math.abs(playerHandler.getPlayer().getPosition().y - cam.position.y / PPM) < 1)
                playerHandler.setNewPlayer(false);
        } else handleInput();

        if (UPDATE) world.step(dt, 10, 2); // recommended values

        if (cl.isEnd() && (playState == pauseState.RUN || playState == pauseState.PAUSE)) {
            UPDATE = false;
            draw.setGameFadeOut(true);
            draw.setGameFadeDone(false);
            playState = pauseState.END;
        }

        draw.updateGameFade(dt);
        draw.updateDimensionFade(dt);

        switch (playState) {
            case RUN:
                updateProps(dt);
                hud.update(dt);
                break;

            case PAUSE:
                pauseMenu.update(dt);
                break;

            case RESUME:
                break;

            case SETTINGS:
                settingsMenu.update(dt);
                break;

            case END:
                // if (cam.zoom < 5)
                //     cam.zoom += 0.01; //TODO: Fix this
                if (executeEnd) playSoundOnce("sounds/end.ogg");
                executeEnd = false;
                //gameFadeOut = true;
                //gameFadeDone = false;
                //drawAndSetCamera();
                cam.update();
                endMenu.update(dt);

            default:
                break;
        }
    }

    private void updateProps(float dt) {


        //update camera
        if (DEBUG) {

            b2dcam.position.set(
                    playerHandler.getPlayer().getPosition().x,
                    playerHandler.getPlayer().getPosition().y,
                    0);

            cam.position.set(
                    playerHandler.getPlayer().getPosition().x * PPM,
                    playerHandler.getPlayer().getPosition().y * PPM,
                    0);

            b2dcam.update();
            cl.setDoubleJump(true);

        } else {

            camSpeed = new Vector2((playerHandler.getPlayer().getPosition().x - cam.position.x / PPM) * 2 * PPM,
                    (playerHandler.getPlayer().getPosition().y - cam.position.y / PPM) * 4 * PPM);

            cam.position.x += camSpeed.x * dt;
            cam.position.y += camSpeed.y * dt;
        }

        cam.position.x = Math.round(cam.position.x);
        cam.position.y = Math.round(cam.position.y);

        // System.out.println(cam.position.x);
        // System.out.println(cam.position.y);
        // System.out.println();

        cam.update();

        playerHandler.updatePlayer(dt);

        //calculate falling dmg
        playerHandler.getPlayer().onLanded(playerHandler.getPlayer().getBody().getLinearVelocity(), cl.isPlayerOnGround());

        //update bosses
        bossHander.updateBosses(dt);

        //update animated cells
        draw.update(dt);
    }

    public void render() {
        if (loading) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //TODO: some loading screen?
            return;
        }

        switch (playState) {
            case RUN:
                drawAndSetCamera();
                break;

            case PAUSE:
                pauseMenu.handleInput();
                drawPauseScreen();
                break;

            case RESUME:
                break;

            case SETTINGS:
                settingsMenu.handleInput();
                drawPauseScreen();
                break;
            case END:
                drawPauseScreen();
                endMenu.handleInput();
                endMenu.render(sb);
                break;
            default:
                break;
        }
    }

    private void drawPauseScreen() {
        //render pauseMenu

        drawAndSetCamera();

        if (playState == pauseState.SETTINGS) settingsMenu.render(sb);
        else if (playState == pauseState.END) endMenu.render(sb);
        else pauseMenu.render(sb);
    }

    private void drawAndSetCamera() {

        //clear screen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        parallaxBackground.setSpeed(backgroundSpeed + camSpeed.x / 10);
        stage.act();
        stage.draw();

        //draw tilemap
        draw.render(cam);

        //draw bosses
        bossHander.renderBosses(sb);

        //draw player and bullets
        playerHandler.renderPlayer(sb);

        hud.render(sb);

        draw.renderFade();
    }

    private void saveGame() {
        GameProgress progress = new GameProgress();
        /*progress.playerLocationX = player.getPosition().x;
        progress.playerLocationY = player.getPosition().y;
        progress.playerVelocityX = player.getBody().getLinearVelocity().x;
        progress.playerVelocityY = player.getBody().getLinearVelocity().y;*/
        progress.checkpointX = playerHandler.getActiveCheckpoint().getBody().getPosition().x;
        progress.checkpointY = playerHandler.getActiveCheckpoint().getBody().getPosition().y;
        progress.act = act;
        progress.map = map;
        progress.dimension = draw.isDimension();
        progress.difficulty = difficulty;

        if (bossHander.getSnowManArray() != null)
            for (Boss boss : bossHander.getSnowManArray()) {
                BossData bossData = new BossData("3", 1, boss.getPosition().x,
                        boss.getPosition().y, boss.isDecider());
                progress.bosses.add(bossData);
            }

        if (bossHander.getMagmaBossArray() != null) {
            for (Array<Boss> bossArray : bossHander.getMagmaBossArray()) {
                Boss boss = bossArray.get(1);
                BossData bossData = new BossData(act.equals("Snow") ? "1_snow" : "1", bossArray.size, boss.getPosition().x,
                        boss.getPosition().y, boss.isDecider());
                progress.bosses.add(bossData);
            }
        }

        if (bossHander.getPlantBossArray() != null) {
            for (Array<Boss> bossArray : bossHander.getPlantBossArray()) {
                Boss boss = bossArray.get(1);
                BossData bossData = new BossData("2", bossHander.getPlantBossSize(), boss.getPosition().x,
                        boss.getPosition().y, boss.isDecider());
                progress.bosses.add(bossData);
            }
        }

        progress.save(B2DVars.PATH + "saves/" + new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss", Locale.ENGLISH).format(new Date()) + ".json");
    }

    public void dispose() {
        stage.dispose();
        System.gc();
    }
}
