package ee.taltech.iti0202.gui.game.desktop.entities.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.handler.BossHander;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.joints.BossHelper;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWormBuilder;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.plantworm.PlantWormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowMan;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowManBuilder;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.snowman.SnowManProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm.MagmaWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm.MagmaWormBuilder;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm.SnowWorm;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm.SnowWormBuilder;
import ee.taltech.iti0202.gui.game.desktop.entities.bosses.worm.WormProperties;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.BodyEditorLoader;
import ee.taltech.iti0202.gui.game.desktop.states.gameprogress.BossData;

import java.util.List;

import static ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars.*;

public class BossLoader {

    private SpriteBatch spriteBatch;
    private Vector2 tempPosition;
    private BodyEditorLoader bossLoader;
    private World world;
    private BossHander bossHander;
    private PlayerHandler playerHandler;
    private FixtureDef fdef;
    private BodyDef bdef;

    public BossLoader(
            World world,
            SpriteBatch spriteBatch,
            FixtureDef fdef,
            BodyDef bdef,
            BossHander bossHander,
            PlayerHandler playerHandler) {
        this.world = world;
        this.spriteBatch = spriteBatch;
        this.fdef = fdef;
        this.bdef = bdef;
        this.bossHander = bossHander;
        this.playerHandler = playerHandler;
    }

    public void createAllBosses(List<BossData> bosses) {
        for (BossData boss : bosses) {
            createBosses(
                    new Vector2(boss.locationX, boss.locationY),
                    boss.type,
                    boss.decider,
                    boss.size,
                    true);
        }
    }

    public void createBosses(Vector2 position, String type, boolean decider, int size) {
        createBosses(position, type, decider, size, false);
    }

    public void createBosses(
            Vector2 position, String type, boolean decider, int size, boolean reload) {

        /////////////////////////////////////////////////////////////////////////
        //                                                                     //
        //   TYPE 1: MAGMA BOSS, can flu through walls n shit                  //
        //   TYPE 2: COLOSSEOS, net.dermetfan.gdx.physics.box2d.Breakable      //
        //   TYPE 3: idk                                                       //
        //                                                                     //
        /////////////////////////////////////////////////////////////////////////
        boolean snowTheme = false;
        if (type.equals("1_snow")) {
            type = "1";
            snowTheme = true;
        }

        float scale = decider ? 1f : 0.5f;
        tempPosition = position;
        bossLoader = new BodyEditorLoader(Gdx.files.internal(PATH + "bosses" + type + ".json"));

        switch (type) {
            case "1":
                Array<Boss> tempArray = new Array<>();
                initSnakePart(Boss.Part.HEAD, scale, tempArray, decider, snowTheme);

                if (!reload) {
                    tempPosition.y -= 60 * scale / PPM;
                }

                for (int i = 0; i < size; i++) {
                    if (i == size - 1) {
                        initSnakePart(Boss.Part.TAIL, scale, tempArray, decider, snowTheme);
                    } else {
                        initSnakePart(Boss.Part.BODY, scale, tempArray, decider, snowTheme);
                    }
                    // createRopeJointBetweenLinks(tempArray, -1f);
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.42f, scale, world);
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.50f, scale, world);
                    BossHelper.createDistanceJointBetweenLinks(tempArray, 0.58f, scale, world);
                    // createRopeJointGEOCordBetweenLinksPlantWorm(tempArray, 0.50f);

                }
                BossHelper.filterTextures(tempArray);
                bossHander.addWorm(tempArray);
                break;

            case "2":
                Array<Array<Boss>> tempArray2 = new Array<>();
                for (int i = 0; i < size; i++) {
                    tempArray2.add(new Array<Boss>());
                }
                initPlantPart(tempArray2, PlantWorm.Part.FLOWER_HEAD, 0, 100, 100);
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).add(tempArray2.get(0).get(0));
                }
                if (!reload) {
                    tempPosition.x += 50 / PPM;
                    tempPosition.y -= 50 / PPM;
                }

                int vine = size * 5;
                for (int i = 0; i < vine; i++) {
                    for (int j = 0; j < size; j++) {
                        if (i == vine - 1) {
                            initPlantPart(tempArray2, PlantWorm.Part.CLAW_HEAD, j, 100, 100);
                        } else {
                            initPlantPart(tempArray2, PlantWorm.Part.BODY, j, 50, 50);
                        }
                        if (i == 0 || i == vine - 1) {
                            BossHelper.createRopeJointGEOCordBetweenLinksPlantWorm(
                                    tempArray2.get(j), i, world);
                        } else {
                            BossHelper.createDistanceJointBetweenLinks(
                                    tempArray2.get(j), 0.4f, scale, world);
                            BossHelper.createDistanceJointBetweenLinks(
                                    tempArray2.get(j), 0.5f, scale, world);
                            BossHelper.createDistanceJointBetweenLinks(
                                    tempArray2.get(j), 0.6f, scale, world);
                        }
                    }
                    tempPosition.y -= 10 / PPM;
                }

                BossHelper.filterTextures(tempArray2.get(0));
                for (int i = 1; i < size; i++) {
                    tempArray2.get(i).reverse();
                }
                bossHander.addHydra(tempArray2);
                break;

            case "3":
                SnowManProperties alias = new SnowManProperties(this.bdef, this.fdef, tempPosition);
                Body body = world.createBody(alias.getBdef());
                body.createFixture(alias.getFdef());
                bossLoader.attachFixture(body, "snowman", alias.getFdef(), 1f * size);
                Boss boss =
                        new SnowManBuilder()
                                .setPlayerHandler(playerHandler)
                                .setBody(body)
                                .setSb(spriteBatch)
                                .setType(type)
                                .setX(0)
                                .setY(0)
                                .createSnowMan();
                boss.getBody().setUserData(BOSS);
                for (Fixture fixture : boss.getBody().getFixtureList()) fixture.setUserData(BOSS);
                bossHander.addSnowMan(boss);
                break;
        }
    }

    private void initPlantPart(
            Array<Array<Boss>> tempArray2, PlantWorm.Part part, int size, float x, float y) {
        PlantWormProperties alias = new PlantWormProperties(this.bdef, this.fdef, tempPosition);
        Body body = world.createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(
                body, part.toString(), alias.getFdef(), part.equals(PlantWorm.Part.BODY) ? 1f : 2f);
        Boss boss =
                new PlantWormBuilder()
                        .setPlayerHandler(playerHandler)
                        .setBody(body)
                        .setSb(spriteBatch)
                        .setType(BOSS)
                        .setPart(part)
                        .setX(x)
                        .setY(y)
                        .setTime(0)
                        .createPlantWorm();
        for (Fixture fixture : boss.getBody().getFixtureList())
            fixture.setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? BOSS + BOSS : BOSS);
        boss.getBody().setUserData(part.equals(PlantWorm.Part.CLAW_HEAD) ? BOSS + BOSS : BOSS);
        tempArray2.get(size).add(boss);
    }

    private void initSnakePart(
            Boss.Part part, float size, Array<Boss> tempArray, boolean decider, boolean snowTheme) {
        WormProperties alias = new WormProperties(this.bdef, this.fdef, tempPosition);
        Body body = world.createBody(alias.getBdef());
        body.createFixture(alias.getFdef());
        bossLoader.attachFixture(body, part.toString() + size, alias.getFdef(), size);
        Boss boss =
                snowTheme
                        ? new SnowWormBuilder()
                                .setPlayerHandler(playerHandler)
                                .setBody(body)
                                .setSb(spriteBatch)
                                .setType(BOSS)
                                .setPart(part)
                                .setSize(size)
                                .setX(50 * size)
                                .setY(50 * size)
                                .createSnowWorm()
                        : new MagmaWormBuilder()
                                .setPlayerHandler(playerHandler)
                                .setBody(body)
                                .setSb(spriteBatch)
                                .setType(BOSS)
                                .setPart(part)
                                .setSize(size)
                                .setX(50 * size)
                                .setY(50 * size)
                                .createMagmaWorm();
        boss.setDecider(decider);
        boss.getBody().setUserData(BOSS);
        for (Fixture fixture : boss.getBody().getFixtureList()) fixture.setUserData(BOSS);
        tempArray.add(boss);
        playerHandler.getTempPlayerLocation().y -= 50 * size / PPM;
    }
}
