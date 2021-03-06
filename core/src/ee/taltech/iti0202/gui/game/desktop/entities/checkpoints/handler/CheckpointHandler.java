package ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0202.gui.game.desktop.entities.Handler;
import ee.taltech.iti0202.gui.game.desktop.entities.checkpoints.Checkpoint;
import ee.taltech.iti0202.gui.game.desktop.entities.player.handler.PlayerHandler;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.MyContactListener;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.sound.Sound;

public class CheckpointHandler implements Handler {
    private Array<Checkpoint> checkpointList = new Array<>();

    public CheckpointHandler() {}

    @Override
    public void update(float dt) {
        for (Checkpoint checkpoint : checkpointList) {
            checkpoint.update(dt);
        }
    }

    public void setPlayerNewCheckpoint(MyContactListener cl, PlayerHandler playerHandler) {
        if (cl.isNewCheckpoint()) {
            Checkpoint curTemp = checkpointList.get(0); // just in case
            for (Checkpoint checkpoint : checkpointList) {
                if (Math.pow(
                                        checkpoint.getPosition().x
                                                - playerHandler
                                                        .getPlayer()
                                                        .getBody()
                                                        .getPosition()
                                                        .x,
                                        2)
                                + Math.pow(
                                        checkpoint.getPosition().y
                                                - playerHandler
                                                        .getPlayer()
                                                        .getBody()
                                                        .getPosition()
                                                        .y,
                                        2)
                        <= Math.pow(
                                        curTemp.getPosition().x
                                                - playerHandler
                                                        .getPlayer()
                                                        .getBody()
                                                        .getPosition()
                                                        .x,
                                        2)
                                + Math.pow(
                                        curTemp.getPosition().y
                                                - playerHandler
                                                        .getPlayer()
                                                        .getBody()
                                                        .getPosition()
                                                        .y,
                                        2)) {
                    curTemp = checkpoint;
                }
            }
            cl.setCurCheckpoint(curTemp.getBody());
            curTemp.onReached();
            if (playerHandler.getCheckpoint() != null) playerHandler.getCheckpoint().dispose();
            playerHandler.setCheckpoint(curTemp);

            Sound.playSoundOnce("sounds/checkpoint.ogg", 0.1f);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // draw checkpoint
        if (checkpointList.size != 0)
            for (Checkpoint checkpoint : checkpointList) {
                checkpoint.render(spriteBatch);
            }
    }

    public Array<Checkpoint> getCheckpointList() {
        return checkpointList;
    }
}
