package ee.taltech.iti0202.gui.game.desktop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.Boss;
import ee.taltech.iti0202.gui.game.desktop.entities.MagmaWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.MagmaWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.PlantWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.PlantWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.animated.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.animated.Player;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.handlers.gdx.input.MyInput;
import ee.taltech.iti0202.gui.game.desktop.handlers.hud.Hud;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.EndMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.PauseMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.SettingsMenu;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.Animation;
import ee.taltech.iti0202.gui.game.desktop.handlers.scene.animations.ParallaxBackground;
import ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.GameProgress;

import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BACKGROUND_SPEEDS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_BOSSES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.BIT_WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DEBUG;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.FRICTION;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.GRAVITY;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAIN_SCREENS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.MAX_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.NONE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PATH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_SIDE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_DASH_FORCE_UP;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PLAYER_SPEED;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.PPM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.SQUARE_CORNERS;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_1;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_DIMENTSION_2;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.TERRA_SQUARES;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.UPDATE;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_HEIGHT;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.V_WIDTH;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.WORM;
import static ee.taltech.iti0202.gui.game.desktop.handlers.variables.B2DVars.gotHitBySnek;

public class Play extends GameState {

    public enum pauseState {
        PAUSE,
        RUN,
        RESUME,
        SETTINGS,
        END,
        DEFAULT,
    }

    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    private OrthographicCamera hudCam;
    private MyContactListener cl;
    private TiledMap tiledMap;
    private Map<TiledMapTileLayer.Cell, Animation> animatedCells;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private boolean dimension;
    private Array<Array<Boss>> MagmabossArray;
    private Array<Array<Boss>> PlantbossArray;
    private aurelienribon.bodyeditor.BodyEditorLoader bossLoader;
    private Checkpoint checkpoint;
    private Vector2 tempPlayerLocation;
    private Vector2 initPlayerLocation;
    private Vector2 tempPosition;
    private BodyDef bdef;
    private PolygonShape polyShape;
    private CircleShape circle;
    private FixtureDef fdef;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;
    private EndMenu endMenu;
    private Hud hud;
    private Stage stage;
    private Texture backgroundTexture;
    private ParallaxBackground parallaxBackground;
    private Vector2 current_force = new Vector2(0, 0);
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;
    private TiledMapTileLayer dimension_2;
    private TiledMapTileLayer dimension_1;
    private pauseState playState = pauseState.DEFAULT;
    private boolean gameFadeOut = false;
    private boolean gameFadeDone = true;
    private boolean dimensionFadeDone = false;
    private boolean newPlayer;
    private float currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;
    private float currentMenuFade = 0;
    private float backgroundSpeed;
    private String act;
    private String map;
    private float scale = 1f;
    private final int takingTurnsBase = 5; // how long one boss attacks
    private int curtentlyActiveBoss = 0;
    private int timeElapsed = 0;
    private int PlantBossSize = 1;
    private boolean executeEnd = true;


    ////////////////////////////////////////////////////////////////////         Set up game        ////////////////////////////////////////////////////////////////////

    private Play(String act, String map, GameProgress progress) {
        this.act = act;
        this.map = map;
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
        world = new World(new Vector2(0, GRAVITY), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        if (DEBUG) b2dr = new Box2DDebugRenderer();

        // create shapes
        bdef = new BodyDef();
        polyShape = new PolygonShape();
        circle = new CircleShape();
        fdef = new FixtureDef();

        // create array for bosses
        dimension = true;
        tempPlayerLocation = new Vector2();
        MagmabossArray = new Array<>();
        PlantbossArray = new Array<>();

        //set up cameras
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);

        // create pause state
        pauseMenu = new PauseMenu(act, map, hudCam, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.RUN;
                UPDATE = true;
                gameFadeOut = false;
                gameFadeDone = false;
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

        endMenu = new EndMenu(act, map, hudCam, new Runnable() {
            @Override
            public void run() {
                playState = pauseState.SETTINGS;
            }
        });
        hud = new Hud(hudCam, this);

        //ShapeRenderer shapeRenderer = new ShapeRenderer();
        playState = pauseState.RUN;

        // set up background
        stage = new Stage(new ScreenViewport());

        String backgroundPath = MAIN_SCREENS[BACKGROUND_SCREENS.get(act)];
        backgroundTexture = new Texture(Gdx.files.internal(PATH + backgroundPath + "backgroundLayer.png"));

        backgroundSpeed = BACKGROUND_SPEEDS.get(act);
        Array<Texture> textures = new Array<>();
        int layersCount = Gdx.files.internal(PATH + backgroundPath).list().length;
        for (int i = 1; i < layersCount; i++) {
            textures.add(new Texture(Gdx.files.internal(PATH + backgroundPath + "backgroundLayer" + i + ".png")));
            textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        stage = new Stage();

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0f);
        stage.addActor(parallaxBackground);
        ////////////////////////////////    Tiled stuff here    ///////////////////////


        // load tiled map
        String path = PATH + "maps/levels/" + act + "/" + map;
        tiledMap = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        animatedCells = new HashMap<>();

        if (progress != null) {
            initPlayerLocation = new Vector2(progress.playerLocationX, progress.playerLocationY);
            dimension = progress.dimension;
            drawLayers();
            initPlayer();
        } else {
            initPlayer();
            drawLayers();
        }

        cam.position.set(
                player.getPosition().x * PPM,
                player.getPosition().y * PPM,
                0);
        newPlayer = true;
        UPDATE = true;
        cam.zoom = 1;
    }

    public Play(String act, String map) {
        this(act, map, null);
    }

    public Play(GameProgress progress) {
        this(progress.act, progress.map, progress);
    }


    ////////////////////////////////////////////////////////////////////   Create Animated bodies   ////////////////////////////////////////////////////////////////////

    private void initPlayer() {
        //TODO: Better logic for loading saved game


        if (player != null) world.destroyBody(player.getBody());
        bdef = new BodyDef();
        fdef = new FixtureDef();
        circle = new CircleShape();
        polyShape = new PolygonShape();

        if (checkpoint == null) {
            if (initPlayerLocation == null) {
                bdef.position.set(0, 0); // hopefully never get here
            } else {
                bdef.position.set(initPlayerLocation);
            }
        } else if (cl.isInitSpawn()) {
            bdef.position.set(initPlayerLocation);
        } else {
            bdef.position.set(new Vector2(checkpoint.getPosition()));
        }

        short mask;
        if (dimension) {
            mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1;
        } else {
            mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_2;
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(-20 / PPM, 20 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("side_l");

        polyShape.setAsBox(2 / PPM, 8 / PPM, new Vector2(20 / PPM, 20 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setUserData("side_r");


        fdef.isSensor = false;
        circle.setRadius(9 / PPM);
        fdef.shape = circle;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setFriction(FRICTION);
        body.setUserData("playerBody");

        polyShape.setAsBox(8 / PPM, 18 / PPM, new Vector2(0, 12 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        body.createFixture(fdef).setUserData("playerBody");

        polyShape.setAsBox(4 / PPM, 1 / PPM, new Vector2(0, -15 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = mask;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");
        player = new Player(body, sb);

    }

    private void createBosses(Vector2 position, String type, boolean decider, int size) {

        /////////////////////////////////////////////////////////////////////////
        //                                                                     //
        //   TYPE 1: MAGMA WORM, can flu through walls n shit                  //
        //   TYPE 2: COLOSSEOS, net.dermetfan.gdx.physics.box2d.Breakable      //
        //   TYPE 3: idk                                                       //
        //                                                                     //
        /////////////////////////////////////////////////////////////////////////

        scale = decider ? 1f : 0.5f;

        switch (type) {

            case "1":
                aurelienribon.bodyeditor.BodyEditorLoader loader = new aurelienribon.bodyeditor.BodyEditorLoader(Gdx.files.internal(PATH + "bosses.json"));
                this.tempPosition = position;
                this.bossLoader = loader;
                Array<Boss> tempArray = new Array<>();
                initSnakePart(MagmaWorm.Part.HEAD, scale, tempArray);
                tempPosition.y -= 60 * scale / PPM;

                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        initSnakePart(MagmaWorm.Part.TAIL, scale, tempArray);
                    } else {
                        initSnakePart(MagmaWorm.Part.BODY, scale, tempArray);
                    }
                    //createRopeJointBetweenLinks(tempArray, -1f);
                    createDistanceJointBetweenLinks(tempArray, 0.40f);
                    createDistanceJointBetweenLinks(tempArray, 0.50f);
                    createDistanceJointBetweenLinks(tempArray, 0.60f);
                    //createRopeJointGEOCordBetweenLinksPlantWorm(tempArray, 0.50f);

                }
                refilterTextures(tempArray);

                MagmabossArray.add(tempArray);
                break;

            case "2":

                PlantBossSize = size;
                aurelienribon.bodyeditor.BodyEditorLoader loader2 = new aurelienribon.bodyeditor.BodyEditorLoader(Gdx.files.internal(PATH + "bosses2.json"));
                this.tempPosition = position;
                this.bossLoader = loader2;
                Array<Array<Boss>> tempArray2 = new Array<>();
                for (int i = 0; i < size; i++) {
                    tempArray2.add(new Array<Boss>());
                }
                initPlantPart(tempArray2, PlantWorm.Part.FLOWER_HEAD, 0, 100, 100);
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).add(tempArray2.get(0).get(0));
                }
                tempPosition.x += 50 / PPM;
                tempPosition.y -= 50 / PPM;

                int vine = size * 5;
                for (int i = 0; i < vine; i++) {
                    for (int j = 0; j < size; j++) {
                        if (i == vine - 1) {
                            initPlantPart(tempArray2, PlantWorm.Part.CLAW_HEAD, j, 100, 100);
                        } else {
                            initPlantPart(tempArray2, PlantWorm.Part.BODY, j, 50, 50);
                        }
                        if (i == 0 || i == vine - 1) {
                            createRopeJointGEOCordBetweenLinksPlantWorm(tempArray2.get(j), i);
                        } else {
                            createDistanceJointBetweenLinks(tempArray2.get(j), 0.4f);
                            createDistanceJointBetweenLinks(tempArray2.get(j), 0.5f);
                            createDistanceJointBetweenLinks(tempArray2.get(j), 0.6f);
                        }
                    }
                    tempPosition.y -= 10 / PPM;
                }

                refilterTextures(tempArray2.get(0));
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).reverse();
                }

                for (Array<Boss> bosses : tempArray2)
                    PlantbossArray.add(bosses);
                break;
        }

    }

    private void refilterTextures(Array<Boss> tempArray) {
        Fixture brokenFixture = tempArray.get(0).getBody().getFixtureList().removeIndex(0); //.get(0);
        brokenFixture.setSensor(true);
        brokenFixture.setUserData(WORM + WORM + WORM);
        brokenFixture.getBody().setUserData(WORM + WORM + WORM);
        brokenFixture.getFilterData().maskBits = NONE;
        brokenFixture.refilter();
        for (Fixture fixture : tempArray.get(0).getBody().getFixtureList()) {
            fixture.setUserData(WORM + WORM);
        }
        tempArray.reverse();
    }

    private void initPlantPart(Array<Array<Boss>> tempArray2, PlantWorm.Part part, int size, float x, float y) {
        PlantWormProperties alias = new PlantWormProperties(bdef, fdef, tempPosition);
        Body body = world.createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString(), alias.getFdef(), part.equals(PlantWorm.Part.BODY) ? 1f : 2f);
        Boss boss = new PlantWorm(body, sb, WORM, this, part, 2f, x, y);
        for (Fixture fixture : boss.getBody().getFixtureList())
            fixture.setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? WORM + WORM : WORM);
        boss.getBody().setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? WORM + WORM : WORM);
        tempArray2.get(size).add(boss);
    }

    private void createDistanceJointBetweenLinks(Array<Boss> tempArray, float lock) {
        // create joint between bodies
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = tempArray.get(tempArray.size - 1).getBody();
        distanceJointDef.bodyB = tempArray.get(tempArray.size - 2).getBody();
        distanceJointDef.length = MagmabossArray.size == 2 ? 40 * scale / PPM : 20 * scale / PPM;
        distanceJointDef.collideConnected = true;
        distanceJointDef.localAnchorA.set(lock * scale, 0.95f * scale);
        distanceJointDef.localAnchorB.set(lock * scale, 0.05f * scale);
        distanceJointDef.length = 0.05f * scale;
        world.createJoint(distanceJointDef);
    }


    private void createRopeJointGEOCordBetweenLinksPlantWorm(Array<Boss> tempArray, int p) {
        // create joint between bodies
        float split = p == 0 ? 1f : 0f;
        float delta = p == 0 ? 0f : 0.4f;
        for (int i = 0; i < 3; i++) {
            DistanceJointDef distanceJointDef = new DistanceJointDef();
            distanceJointDef.bodyA = tempArray.get(tempArray.size - 1).getBody();
            distanceJointDef.bodyB = tempArray.get(tempArray.size - 2).getBody();
            distanceJointDef.collideConnected = false;
            distanceJointDef.localAnchorA.set(0.5f + i * 0.1f + delta, 0.95f); // done
            distanceJointDef.localAnchorB.set((95 + i * 5) / PPM, 0.05f + split);
            distanceJointDef.length = 0.05f;
            world.createJoint(distanceJointDef);
        }
    }

    private void createRopeJointBetweenLinks(Array<Boss> tempArray, float lock) {
        // create joint between bodies
        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.bodyA = tempArray.get(tempArray.size - 1).getBody();
        ropeJointDef.bodyB = tempArray.get(tempArray.size - 2).getBody();
        //ropeJointDef.length = MagmabossArray.size == 2 ? 40 * scale / PPM : 20 * scale / PPM;
        ropeJointDef.collideConnected = true;
        ropeJointDef.maxLength = 0.02f * scale;
        ropeJointDef.localAnchorA.set(lock * scale, 1f * scale);
        ropeJointDef.localAnchorB.set(lock * scale, 0f * scale);
        world.createJoint(ropeJointDef);
    }

    private void initSnakePart(MagmaWorm.Part part, float size, Array<Boss> tempArray) {
        MagmaWormProperties alias = new MagmaWormProperties(bdef, fdef, tempPosition);
        Body body = world.createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString() + size, alias.getFdef(), scale);
        Boss boss = new MagmaWorm(body, sb, WORM, this, part, size, 50 * scale, 50 * scale);
        boss.getBody().setUserData(WORM);
        for (Fixture fixture : boss.getBody().getFixtureList()) fixture.setUserData(WORM);
        tempArray.add(boss);
        tempPosition.y -= 50 * scale / PPM;
    }

    private void createCheckpoints(Vector2 pos) {
        System.out.println("new checkpoint");
        bdef = new BodyDef();
        fdef = new FixtureDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        polyShape = new PolygonShape();
        polyShape.setAsBox(4 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = B2DVars.BIT_ALL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("checkpoint");
        checkpoint = new Checkpoint(body, sb);
        //checkpoint.onReached();
    }

    private void createEndPoint(Vector2 pos) {
        System.out.println("new endpoint");
        bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);
        polyShape = new PolygonShape();
        polyShape.setAsBox(64 / PPM, 32 / PPM, new Vector2(0, 4 / PPM), 0);
        fdef.shape = polyShape;
        fdef.filter.categoryBits = DIMENTSION_1 | DIMENTSION_2;
        fdef.filter.maskBits = B2DVars.BIT_ALL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("end");
        checkpoint = new Checkpoint(body, sb);
    }

    ////////////////////////////////////////////////////////////////////    Read and draw the map   ////////////////////////////////////////////////////////////////////

    private void drawLayers() {
        for (MapLayer layer : tiledMap.getLayers()) {
            switch (layer.getName()) {
                case "barrier":
                    fdef.filter.categoryBits = BACKGROUND;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes_1":
                    fdef.filter.categoryBits = TERRA_DIMENTSION_1;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1;
                    determineMapObject(layer);
                    break;
                case "hitboxes_2":
                    fdef.filter.categoryBits = TERRA_DIMENTSION_2;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                case "hitboxes":
                    fdef.filter.categoryBits = TERRA_SQUARES;
                    fdef.filter.maskBits = BIT_BOSSES | DIMENTSION_1 | DIMENTSION_2;
                    determineMapObject(layer);
                    break;
                default:
                    ReadVertices((TiledMapTileLayer) layer);
            }
        }
    }

    private void determineMapObject(MapLayer layer) {
        Shape shape;
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject)
                shape = getRectangle((RectangleMapObject) object);
            else if (object instanceof PolygonMapObject)
                shape = getPolygon((PolygonMapObject) object);
            else if (object instanceof PolylineMapObject)
                shape = getPolyline((PolylineMapObject) object);
            else if (object instanceof EllipseMapObject)
                shape = getCircle((EllipseMapObject) object);
            else continue;
            bdef.type = BodyDef.BodyType.StaticBody;
            fdef.isSensor = false;
            fdef.shape = shape;
            world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());
        }
    }

    private void ReadVertices(TiledMapTileLayer layer) {
        int[] corner_coords = SQUARE_CORNERS;
        String type = layer.getName();
        boolean isSensor = false;
        float tileSize = layer.getTileWidth();

        switch (type) {

            case "dimension_1":
                dimension_1 = layer;
                isSensor = true;
                layer.setVisible(true);
                layer.setOpacity(1f);
                background = layer;
                break;

            case "dimension_2":
                dimension_2 = layer;
                isSensor = true;
                layer.setVisible(true);
                layer.setOpacity(0.5f);
                background = layer;
                break;

            case "background":
                isSensor = true;
                layer.setVisible(true);
                background = layer;
                break;

            case "foreground":
                isSensor = true;
                layer.setVisible(true);
                foreground = layer;
                break;

            default:
                layer.setVisible(false);
                break;
        }

        bdef.type = BodyDef.BodyType.StaticBody;

        for (int row = 0; row <= layer.getHeight(); row++) {

            List<Vector2[]> polygonVertices = new ArrayList<>();
            Vector2[] v = new Vector2[4];
            boolean lastWasThere = false;

            for (int col = 0; col <= layer.getWidth(); col++) {
                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                if (cell == null) {
                    lastWasThere = false;
                    if (v[0] != null) {
                        polygonVertices.add(v);
                    }
                    v = new Vector2[4];
                    continue;
                }
                fixBleeding(cell.getTile().getTextureRegion());
                if (cell.getTile().getProperties().containsKey("animation")) {
                    Texture tex = Game.res.getTexture("Player");
                    TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
                    animatedCells.put(cell, new Animation(sprites, 1 / 12f));
                }

                float corner = tileSize / 2 / PPM;
                float mapPosCol = (col + 0.5f) * tileSize / PPM;
                float mapPosRow = (row + 0.5f) * tileSize / PPM;

                // writing vertices for hit boxes
                if (lastWasThere) {

                    v[2] = new Vector2(mapPosCol + corner_coords[4] * corner, mapPosRow + corner_coords[5] * corner);
                    v[3] = new Vector2(mapPosCol + corner_coords[6] * corner, mapPosRow + corner_coords[7] * corner);

                } else {
                    v[0] = new Vector2(mapPosCol + corner_coords[0] * corner, mapPosRow + corner_coords[1] * corner);
                    v[1] = new Vector2(mapPosCol + corner_coords[2] * corner, mapPosRow + corner_coords[3] * corner);
                    v[2] = new Vector2(mapPosCol + corner_coords[4] * corner, mapPosRow + corner_coords[5] * corner);
                    v[3] = new Vector2(mapPosCol + corner_coords[6] * corner, mapPosRow + corner_coords[7] * corner);

                }
                lastWasThere = true;

            }

            for (Vector2[] polygon : polygonVertices) {

                polyShape.set(polygon);
                fdef.filter.categoryBits = NONE;
                fdef.filter.maskBits = NONE;
                fdef.isSensor = isSensor;
                switch (layer.getName()) {
                    case "checkpoints":
                        if ((polygon[0].x - polygon[3].x) / (polygon[0].y - polygon[1].y) > 1.8) {
                            createEndPoint(new Vector2(polygon[1].x + tileSize / PPM, polygon[0].y));
                        } else {
                            createCheckpoints(new Vector2(polygon[1].x + (polygon[3].x - polygon[1].x) / 2, polygon[0].y));
                        }
                        break;

                    case "bosses_small":
                        createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), false, (Integer) layer.getProperties().get("size"));
                        break;

                    case "bosses_big":
                        createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), true, (Integer) layer.getProperties().get("size"));
                        break;
                    case "bosses":
                        createBosses(new Vector2(polygon[2].x - (tileSize / 2) / PPM, polygon[2].y), layer.getProperties().get("type").toString(), true, (Integer) layer.getProperties().get("size"));
                        break;

                    case "player":
                        if (initPlayerLocation == null) {
                            initPlayerLocation = new Vector2(polygon[2].x + (tileSize / 2) / PPM, polygon[2].y);
                            initPlayer();
                        }
                        break;

                    default:
                        world.createBody(bdef).createFixture(fdef).setUserData(layer.getName());
                        break;
                }
            }
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                (rectangle.y + rectangle.height * 0.5f) / PPM);
        polygon.setAsBox(rectangle.width * 0.5f / PPM,
                rectangle.height * 0.5f / PPM,
                size,
                0.0f);
        return polygon;
    }

    private static CircleShape getCircle(EllipseMapObject circleObject) {
        Ellipse circle = circleObject.getEllipse();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.width / PPM);
        circleShape.setPosition(new Vector2(circle.x / PPM, circle.y / PPM));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / PPM;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    // private static void fixBleeding(TextureRegion[][] region) {
    //     for (TextureRegion[] array : region) {
    //         for (TextureRegion texture : array) {
    //             fixBleeding(texture);
    //         }
    //     }
    // }

    private static void fixBleeding(TextureRegion region) {
        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight); // Trims
        // region
    }


    ////////////////////////////////////////////////////////////////////      Handle I/O devices    ////////////////////////////////////////////////////////////////////

    public void handleInput() {

        current_force = player.getBody().getLinearVelocity();

        //pause screen
        if (MyInput.isPressed(Game.settings.ESC)) {
            if (playState == pauseState.RUN) {
                UPDATE = false;
                playState = pauseState.PAUSE;
                gameFadeOut = true;
                gameFadeDone = false;
            } else {
                UPDATE = true;
                playState = pauseState.RUN;
                gameFadeOut = false;
                gameFadeDone = false;
            }
        }

        if (playState == pauseState.RUN) {
            //change dimension
            if (MyInput.isPressed(Game.settings.CHANGE_DIMENTION)) {
                System.out.println("changed dimension");
                dimensionFadeDone = false;
                dimension = !dimension;

                short mask;
                if (dimension) {
                    mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_1;
                } else {
                    mask = BIT_BOSSES | BIT_WORM | DIMENTSION_1 | DIMENTSION_2 | TERRA_SQUARES | BACKGROUND | TERRA_DIMENTSION_2;
                }

                Filter filter = new Filter();
                for (Fixture playerFixture : player.getBody().getFixtureList()) {
                    filter.groupIndex = playerFixture.getFilterData().groupIndex;
                    filter.categoryBits = playerFixture.getFilterData().categoryBits;
                    filter.maskBits = mask;
                    playerFixture.setFilterData(filter);
                }

            }

            //player jump / double jump / dash
            if (MyInput.isPressed(Game.settings.JUMP)) {
                player.setAnimation(Player.PlayerAnimation.JUMP);
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);//.applyForceToCenter(0, PLAYER_DASH_FORCE_UP, true);
                } else if (cl.isWallJump() != 0) {
                    player.getBody().applyLinearImpulse(new Vector2(cl.isWallJump() * PLAYER_DASH_FORCE_UP, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                    cl.setWallJump(0);
                } else if (cl.hasDoubleJump()) {
                    player.getBody().applyLinearImpulse(new Vector2(0, PLAYER_DASH_FORCE_UP), tempPlayerLocation, true);
                    cl.setDoubleJump(false);
                    player.setAnimation(Player.PlayerAnimation.ROLL);
                }
            }

            //player move left
            if (MyInput.isDown(Game.settings.MOVE_LEFT)) {
                if (current_force.x > -MAX_SPEED) {
                    if (cl.isPlayerOnGround()) {
                        player.getBody().applyForceToCenter(-PLAYER_SPEED, 0, true);
                        player.setAnimation(Player.PlayerAnimation.RUN);
                    } else {
                        player.getBody().applyForceToCenter(-PLAYER_SPEED * 1.25f, 0, true);
                    }

                }
                player.setFlipX(true);
            }

            //player dash left
            if (MyInput.isPressed(Game.settings.MOVE_LEFT)) {
                if (!cl.isPlayerOnGround() && cl.hasDash()) {
                    current_force = player.getBody().getLinearVelocity();
                    if (current_force.x > 0) {
                        player.getBody().applyLinearImpulse(new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                    } else {
                        player.getBody().applyLinearImpulse(new Vector2(-PLAYER_DASH_FORCE_SIDE, 0), tempPlayerLocation, true);
                    }
                    cl.setDash(false);
                    player.setAnimation(Player.PlayerAnimation.DASH);
                }
                player.setFlipX(true);
            }

            //player move right
            if (MyInput.isDown(Game.settings.MOVE_RIGHT)) {
                if (current_force.x < MAX_SPEED) {
                    if (cl.isPlayerOnGround()) {
                        player.setAnimation(Player.PlayerAnimation.RUN);
                        player.getBody().applyForceToCenter(PLAYER_SPEED, 0, true);
                    } else {
                        player.getBody().applyForceToCenter(PLAYER_SPEED * 1.25f, 0, true);
                    }
                }
                player.setFlipX(false);
            }

            //player dash right
            if (MyInput.isPressed(Game.settings.MOVE_RIGHT)) {
                if (!cl.isPlayerOnGround() && cl.hasDash()) {
                    if (current_force.x < 0) {
                        player.getBody().applyLinearImpulse(new Vector2(-current_force.x, 0), tempPlayerLocation, true);
                    } else {
                        player.getBody().applyLinearImpulse(new Vector2(PLAYER_DASH_FORCE_SIDE, 0), tempPlayerLocation, true);
                    }
                    cl.setDash(false);
                    player.setAnimation(Player.PlayerAnimation.DASH);
                }
                player.setFlipX(false);
            }

            if (!MyInput.isDown(-1) && cl.isPlayerOnGround()) {
                player.setAnimation(Player.PlayerAnimation.IDLE);
            }

            //TODO: roll on land -> need to detect landing
        }
    }

    public void update(float dt) {

        if (newPlayer) {
            if (Math.abs(player.getPosition().x - cam.position.x / PPM) < 1 && Math.abs(player.getPosition().y - cam.position.y / PPM) < 1)
                newPlayer = false;
        } else handleInput();

        if (UPDATE) world.step(dt, 10, 2); // recommended values

        if (cl.isEnd() && (playState == pauseState.RUN || playState == pauseState.PAUSE)) {
            UPDATE = false;
            gameFadeOut = true;
            gameFadeDone = false;
            playState = pauseState.END;
        }

        updateGameFade(dt);
        updateDimensionFade(dt);

        switch (playState) {
            case RUN:
                UpdateProps(dt);
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

    private void UpdateProps(float dt) {
        //update camera
        if (DEBUG) {

            b2dcam.position.set(
                    player.getPosition().x,
                    player.getPosition().y,
                    0);

            cam.position.set(
                    player.getPosition().x * PPM,
                    player.getPosition().y * PPM,
                    0);
            b2dcam.update();

        } else {

            cam.position.x += (player.getPosition().x - cam.position.x / PPM) * 2 * PPM * dt;
            cam.position.y += (player.getPosition().y - cam.position.y / PPM) * 2 * PPM * dt;
        }

        cam.update();

        //call update animation
        if (player.getHealth() == 0) {
            cl.setDeathState((short) 3);
        }
        if (cl.getDeathState() == 0) {
            player.update(dt);
        } else {
            if (cl.getDeathState() == 2) {
                player.setHealth(player.getHealth() - gotHitBySnek * 10);
                playSoundOnce("sounds/sfx_deathscream_alien1.wav");
            }
            if (cl.getDeathState() == 3) {
                player.setHealth(0);
            } else {
                playSoundOnce("sounds/sfx_damage_hit2.wav", 0.1f);
                player.setHealth(player.getHealth() - gotHitBySnek);
            }
            if (player.getHealth() <= 0) {
                playSoundOnce("sounds/sfx_sound_shutdown1.wav");
                initPlayer();
            }
            cl.setDeathState((short) 0);
            player.update(dt);
        }

        //calculate falling dmg
        player.onLanded(cl.isImpact(), cl.isPlayerOnGround());


        //update boss
        if (MagmabossArray.size != 0) {
            for (Array<Boss> bossList : MagmabossArray)
                for (int i = 0; i < bossList.size; i++) {
                    if (bossList.size > 110) {
                        if (i == bossList.size - 1) {
                            bossList.get(i).updateHeadBig(dt);
                        } else {
                            bossList.get(i).update(dt);
                        }
                    } else {
                        if (i == bossList.size - 1) {
                            bossList.get(i).updateHeadSmall(dt);
                        } else {
                            bossList.get(i).update(dt);
                        }
                    }
                }
        }


        if (PlantbossArray.size != 0) {

            int takingTurns = takingTurnsBase * Gdx.graphics.getFramesPerSecond();
            timeElapsed++;
            if (timeElapsed > takingTurns) {
                timeElapsed = 0;
                curtentlyActiveBoss = (curtentlyActiveBoss + 1) % PlantBossSize;
            }
            int j = 0;
            for (Array<Boss> bossList : PlantbossArray) {
                for (int i = 0; i < bossList.size; i++) {
                    if (i == 0) {
                        if (j == curtentlyActiveBoss) {
                            bossList.get(i).updateHeadBig(dt);
                        } else {
                            bossList.get(i).updateCircularMotion(dt);
                        }
                        bossList.get(i).updateRotation(dt);
                    } else if (i == bossList.size - 1) {
                        bossList.get(i).updateRotation(dt);
                    } else {
                        bossList.get(i).update(dt);
                    }

                }
                j++;
            }
        }

        //draw tilemap animations
        if (animatedCells != null) {
            for (Animation animation : animatedCells.values()) {
                animation.update(dt);
            }
        }

        // create a new checkpoint if needed
        if (checkpoint != null) {
            if (cl.isNewCheckpoint()) {
                cl.setNewCheckpoint(false);
                createCheckpoints(cl.getCurCheckpoint());
                playSoundOnce("sounds/checkpoint.ogg");
            }
            checkpoint.update(dt);
        }


        // dispose of old bodies
        Body temp = cl.removeOldCheckpoint();
        if (temp != null) {
            world.destroyBody(temp);
            cl.resetOldCheckpoint();
        }
    }

    public void render() {
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

    private void updateGameFade(float dt) {
        if (!gameFadeDone) {
            if (gameFadeOut) {
                if (currentMenuFade < B2DVars.MENU_FADE_AMOUNT) {
                    currentMenuFade += (B2DVars.MENU_FADE_AMOUNT / B2DVars.MENU_FADE_TIME) * dt;
                } else {
                    currentMenuFade = B2DVars.MENU_FADE_AMOUNT;
                    gameFadeDone = true;
                }
            } else {
                if (currentMenuFade > 0) {
                    currentMenuFade -= (B2DVars.MENU_FADE_AMOUNT / B2DVars.MENU_FADE_TIME) * dt;
                } else {
                    currentMenuFade = 0;
                    gameFadeDone = true;
                }
            }

            /*parallaxBackground.setColor(1, 1, 1, 1 - currentMenuFade);
            player.setOpacity(1 - currentMenuFade);
            if (checkpoint != null) checkpoint.setOpacity(1 - currentMenuFade);*/
        }
    }

    private void updateDimensionFade(float dt) {
        if (!dimensionFadeDone) {
            if (dimension) {
                if (currentDimensionFade > 0) {
                    currentDimensionFade -= (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = 0;
                    dimensionFadeDone = true;
                }
            } else {
                if (currentDimensionFade < B2DVars.DIMENSION_FADE_AMOUNT) {
                    currentDimensionFade += (B2DVars.DIMENSION_FADE_AMOUNT / B2DVars.DIMENSION_FADE_TIME) * dt;
                } else {
                    currentDimensionFade = B2DVars.DIMENSION_FADE_AMOUNT;
                    dimensionFadeDone = true;
                }
            }
            if (dimension_1 != null) dimension_1.setOpacity(1 - currentDimensionFade);
            if (dimension_2 != null)
                dimension_2.setOpacity((1 - B2DVars.DIMENSION_FADE_AMOUNT) + currentDimensionFade);
        }
    }

    private void drawAndSetCamera() {

        //clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClear(GL20.GL_ALPHA_BITS);

        //set camera to follow player
        /*if (current_force.x < -1) parallaxBackground.setSpeed(-60f);
        else if (current_force.x > 1) parallaxBackground.setSpeed(60f);
        else parallaxBackground.setSpeed(0);
        System.out.println(current_force.x);*/
        sb.begin();
        sb.draw(backgroundTexture, 0, 0);
        sb.end();
        parallaxBackground.setSpeed(backgroundSpeed * (current_force.x * 5 + 4)); //TODO: more advance stuff here, move with camera...
        stage.act();
        stage.draw();

        //render animations
        if (animatedCells != null) for (TiledMapTileLayer.Cell cell : animatedCells.keySet())
            cell.setTile(new StaticTiledMapTile(animatedCells.get(cell).getFrame()));

        //draw tilemap
        renderer.setView(cam);
        renderer.getBatch().begin();
        if (background != null) renderer.renderTileLayer(background);
        if (foreground != null) renderer.renderTileLayer(foreground);
        if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
        if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
        renderer.getBatch().end();

        if (DEBUG) b2dr.render(world, b2dcam.combined);

        sb.setProjectionMatrix(cam.combined);

        // draw checkpoint
        if (checkpoint != null)
            checkpoint.render(sb);

        //draw player
        if (player != null) player.render(sb);

        if (MagmabossArray != null) {
            for (Array<Boss> bossList : MagmabossArray)
                for (Boss boss : bossList) boss.render(sb);
        }

        if (PlantbossArray != null) {
            for (Array<Boss> bossList : PlantbossArray) {
                for (Boss boss : bossList) boss.render(sb);
                bossList.get(0).render(sb);
            }
        }


        hud.render(sb);

        if (currentMenuFade > 0) {
            /*renderer.getBatch().setColor(0, 0, 0, 1);
            if (background != null) renderer.renderTileLayer(background);
            if (foreground != null) renderer.renderTileLayer(foreground);
            if (dimension_1 != null) renderer.renderTileLayer(dimension_1);
            if (dimension_2 != null) renderer.renderTileLayer(dimension_2);
            renderer.getBatch().setColor(1, 1, 1, 1 - currentMenuFade);*/
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, currentMenuFade);
            shapeRenderer.rect(0, 0, B2DVars.V_WIDTH, B2DVars.V_HEIGHT);
            shapeRenderer.end();
        }
    }

    private void saveGame() {
        GameProgress progress = new GameProgress();
        progress.playerLocationX = player.getPosition().x;
        progress.playerLocationY = player.getPosition().y;
        progress.playerVelocityX = player.getBody().getLinearVelocity().x;
        progress.playerVelocityY = player.getBody().getLinearVelocity().y;
        progress.act = act;
        progress.map = map;
        progress.dimension = dimension;

        progress.save(B2DVars.PATH + "saves/" + new SimpleDateFormat("dd-MM-YYYY_HH-mm-ss", Locale.ENGLISH).format(new Date()) + ".json");
    }

    public void dispose() {
        stage.dispose();
        System.gc();
    }

    public Player getPlayer() {
        return player;
    }
}
